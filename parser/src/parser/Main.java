package parser;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Main {

	
	private static String[][] iterateTable(String siteNo, String startDate,
			String endDate) throws IOException {
		Document document = Jsoup
				.connect(
						"http://waterdata.usgs.gov/ca/nwis/uv?cb_00054=on&cb_00065=on&format=html&site_no="
								+ siteNo
								+ "&period=&begin_date="
								+ startDate
								+ "&end_date=" + endDate).get();
		Element table = document.select("table").get(1);
		Elements rows = table.select("tr");

		String[][] mainArr = new String[rows.size()][];

		for (int i = 0; i < rows.size(); i++) {

			Elements data = rows.get(i).select("td");
			String[] arr = new String[data.size()];

			for (int j = 0; j < data.size(); j++) {
				arr[j] = data.get(j).text();
			}

			mainArr[i] = arr;
		}

		return mainArr;
	}
	
	private  static String[] getAllStates() throws IOException {
		Document document = (Document) Jsoup.connect("http://waterwatch.usgs.gov/wqwatch/map").get();
		Element select = document.select("select").get(0);
		Elements options = select.getAllElements();
		
		
		String[] states = new String[options.size()];
		for (int i = 0; i < options.size(); i++) {
			String value = options.get(i).attr("value");
			if(!"".equals(value)) {
				states[i] = value;
			}
		}
		
		return states;
	}

	public static void main(String[] args) {
		Parser parser = new ParserImpl("http://waterwatch.usgs.gov", "http://waterdata.usgs.gov");
		
		try {
			List<String> allStates = parser.getAllStates();
			List<String> allMeasurements = parser.getAllMeasurements();
			List<String> allStationNumbers = parser.getAllStationNumbers(allStates.get(1), allMeasurements.get(1));
			parser.crawlData(allStates.get(1), allStationNumbers.get(1), parser.getAllParameters(allStates.get(1), allStationNumbers.get(1)));
//			for (String state : allStates) {
//				if(state.equals("us")){
//					continue;
//				}
//				for (String measurement : allMeasurements) {
//					List<String> allStationNumbers = parser.getAllStationNumbers(state, measurement);
//					for (String stationNumber : allStationNumbers) {
//						List<String> allParameters = parser.getAllParameters(state, stationNumber);
//
//						Map<String, Map<String, String>> crawledData = parser.crawlData(state, stationNumber, allParameters);
//						System.out.println(crawledData);
//					}
//				}
//			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
