package parser;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import parser.model.Measurement;

public class ParserImpl implements Parser {

	private String firstSiteUrl;

	private String secondSiteUrl;

	DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:MM");

	public ParserImpl(String firstSiteUrl, String secondSiteUrl) {
		this.firstSiteUrl = firstSiteUrl;
		this.secondSiteUrl = secondSiteUrl;
	}

	private Document getDocument(String url) throws IOException {
		// return Jsoup.connect(url).get();
		return Jsoup.connect(url).timeout(0).get();
	}

	private List<String> getSelectOptionsByIndex(int index) throws IOException {
		Document document = this.getDocument(firstSiteUrl + "/wqwatch/map");

		Element select = document.select("select").get(index);

		Elements options = select.getAllElements();

		List<String> optionsList = new ArrayList<String>();
		for (int i = 0; i < options.size(); i++) {
			String value = options.get(i).attr("value");
			if (!"".equals(value)) {
				optionsList.add(value);
			}
		}

		return optionsList;
	}

	private Element getTableByIndex(Document document, int index) {
		Element table = document.select("table").get(index);
		return table;
	}

	private double convert(String coordinate) {

		double degrees = Double.parseDouble(coordinate.replaceAll("[a-zA-Z]",
				"").split("°")[0].substring(2));
		double minutes = Double.parseDouble(coordinate.replaceAll("[a-zA-Z]",
				"").split("°")[1].split("'")[0]);
		double seconds = Double.parseDouble(coordinate.replaceAll("[a-zA-Z]",
				"").split("°")[1].split("'")[1].split("\"")[0]);
		return degrees + minutes / 60.0 + seconds / 3600.0;
	}

	@Override
	public Map<String, Double> getLocation(String state, String stationNumber)
			throws IOException {
		String url = "http://waterdata.usgs.gov/" + state
				+ "/nwis/nwismap/?site_no=" + stationNumber + "&agency_cd=USGS";
		Document document = this.getDocument(url);

		Elements divs = document.select("div");

		String location = "";

		for (Element div : divs) {
			if (div.text().contains("Latitude")) {
				location = div.text();
				break;
			}
		}
		
		if(!"".equals(location)){
			String[] splitted = location.split(",");
			Map<String, Double> result = new HashMap<String, Double>();
			result.put("latitude", this.convert(splitted[0]));
			result.put("longtitude", (-1) * this.convert(splitted[1]));
			return result;
		}
		return null;
	}

	public Map<String, String> getLocationAsString(String state, String stationNumber)
			throws IOException {
		String url = "http://waterdata.usgs.gov/" + state
				+ "/nwis/nwismap/?site_no=" + stationNumber + "&agency_cd=USGS";
		Document document = this.getDocument(url);

		Elements divs = document.select("div");

		String location = "";

		for (Element div : divs) {
			if (div.text().contains("Latitude")) {
				location = div.text();
				break;
			}
		}
		
		if(!"".equals(location)){
			String[] splitted = location.split(",");
			Map<String, String> result = new HashMap<String, String>();
			result.put("latitude", splitted[0]);
			result.put("longtitude", splitted[1]);
			return result;
		}
		return null;
	}

	@Override
	public List<String> getAllStates() throws IOException {
		return this.getSelectOptionsByIndex(0);
	}

	@Override
	public List<String> getAllMeasurements() throws IOException {
		return this.getSelectOptionsByIndex(1);
	}

	@Override
	public List<String> getAllStationNumbers(String state, String measurement)
			throws IOException {
		// System.out.println(secondSiteUrl + "/" + state +
		// "/nwis/current?type=qw&PARAmeter_cds=STATION_NM,DATETIME," +
		// measurement);
		Document document = this.getDocument(secondSiteUrl + "/" + state
				+ "/nwis/current?type=qw&PARAmeter_cds=STATION_NM,DATETIME,"
				+ measurement);

		Element table = this.getTableByIndex(document, 1);

		Elements rows = table.select("tr");

		List<String> stationNumbers = new ArrayList<String>();
		
		for (int i = 0; i < rows.size(); i++) {
			Elements data = rows.get(i).select("td");

			for (int j = 0; j < data.size(); j++) {
				Elements anchors = data.select("a");
				if (anchors.size() > 0
						&& anchors.get(0).text().matches("[0-9]+")) {
					stationNumbers.add(anchors.get(0).text());
					this.getAllParameters(state, anchors.get(0).text());
				}
			}

		}

		return stationNumbers;
	}

	@Override
	public List<String> getAllParameters(String state, String stationNumber)
			throws IOException {
		Document document = this.getDocument(secondSiteUrl + "/" + state
				+ "/nwis/uv/?site_no=" + stationNumber);

		Element table = this.getTableByIndex(document, 0);

		Elements tbody = table.select("tbody");

		List<String> allParameters = new ArrayList<String>();

		for (int i = 0; i < tbody.size(); i++) {
			Elements rows = tbody.get(i).select("tr");

			for (int j = 1; j < rows.size(); j++) {
				String param = rows.get(j).select("td").get(1).text()
						.replaceAll("[^0-9]", "").trim();
				allParameters.add(param);
			}

		}

		return allParameters;
	}

	@Override
	public Map<String, Map<String, String>> crawlData(String state,
			String stationNumber, List<String> parameters) throws IOException,
			ParseException {

		// String[][] mainArr = new String[rows.size()][];
		Map<String, Map<String, String>> mainArr = new ConcurrentHashMap<String, Map<String, String>>();
		
		ParserImpl that = this;
		
		ExecutorService service = Executors.newFixedThreadPool(10);
		service.execute(new Runnable() {

			@Override
			public void run() {
				StringBuilder url = new StringBuilder(secondSiteUrl + "/" + state
						+ "/nwis/uv?format=html&site_no=" + stationNumber);
				System.out.println("asdsad " + url);

				for (String param : parameters) {
					url.append("&cb_" + param);
				}

				url.append("&period=");
				System.out.println(url.toString());
				Document document;
				try {
					document = that.getDocument(url.toString());

					Element table = that.getTableByIndex(document, 1);

					Elements tbody = table.select("tbody");

					Elements metrics = table.select("thead");
					Element metricsRow = metrics.select("tr").get(0);

					Elements ths = metricsRow.select("th");
					System.out.println(ths);

					Elements rows = tbody.select("tr");

					// String[][] mainArr = new String[rows.size()][];

					// String date = "";
					for (int i = 0; i < rows.size(); i++) {
						Map<String, String> map = new HashMap<String, String>();

						String date = rows.get(i).select("td").get(0).text();

						Elements data = rows.get(i).select("td");

						// String[] arr = new String[data.size()];
						for (int j = 0; j < data.size(); j++) {
							// arr[j] = data.get(j).text();
							String key = ths.get(j).text();
							String val = data.get(j).text();
							map.put(key, val);
						}

						// mainArr[i] = arr;
						// mainArr.put(i, arr);

					
						// http://192.168.1.2:1337/measurement/create?lat=:measurement.getLat()&long=measurement.getLong()
						// http://192.168.3.146:1337/measurement/create?latitude=43.0032&longitude=23.23232&temperature=18&airRelativeHumidity=23&atmosphericPressure=989.34&soilHumidity=789.85&luminance=33838&waterQuality=97
						Map<String, Double> location = that.getLocation(state,
								stationNumber);
						
						if(location != null) {
							Measurement measurement = new Measurement(location.get("latitude"), location.get("longtitude"),
									that.dateFormat.parse(date), map);
							
							System.out.println(measurement.getUrl());
							Utils.sendRequest(measurement.getUrl());
						} else {
							System.err.println("locatio not found");
						}

						mainArr.put(date, map);
					}
				} catch (IOException e) {
					System.exit(9);
				} catch (ParseException e) {
					e.printStackTrace();
				}

			}
		});

		
		//
		// int i = 0;
		// for (String[] strings : mainArr) {
		// for (String string : strings) {
		// i++;
		// }
		// }
		// System.out.println(i);

		return mainArr;
	}

}
