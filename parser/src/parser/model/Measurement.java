package parser.model;

import java.util.Date;
import java.util.Map;

import parser.Utils;

public class Measurement {

	private double lat;
	private double longitude;
	private Date date;
	private double waterQuality;
	private Map<String, String> measurements;

	public Measurement(double lat, double longitude, Date date,
			Map<String, String> measurements) {

		this.lat = lat;
		this.longitude = longitude;
		this.date = date;
		this.measurements = measurements;
		this.waterQuality = calculateQuality();

	}

	public Measurement(Date date, double waterQuality, double latitude, double longitude){
	
		setDate(date);
		setQuality(waterQuality);
		setLat(latitude);
		setLongitude(longitude);
	}
	
	public double calculateQuality() {

		double result = 0.0;
		/*
		 * if(!measurements.containsKey("ph")) { result += 8 * 0.11; }
		 */
		if (!measurements.containsKey("Dis- solved oxygen, mg/L,")) {
			result += 5 * 0.17;
		}
		if (!measurements.containsKey("Temper- ature, water, deg C,")) {
			result += 10 * 0.11;
		}
		for (Map.Entry<String, String> measurement : measurements.entrySet()) {

			/*
			 * if(measurement.getKey().equals("ph")) {
			 * phValue = Double.parseDouble(measurement.getValue()); if(phValue
			 * <= 2 && phValue >= 12) { result += 0; } result +=
			 * phQuality(phValue) * 0.11; }
			 */

			if (measurement.getKey().equals("Dis- solved oxygen, mg/L,")) {

				String str = measurement.getValue();
				result += oxygenQuality(Double.parseDouble(str.substring(0,
						str.length() - 1))) * 0.17;
			}

			if (measurement.getKey().equals("Temper- ature, water, deg C,")) {
				String str = measurement.getValue();
				result += tempQuality(Double.parseDouble(str.substring(0,
						str.length() - 1))) * 0.11;
			}
		}

		return result;
	}

	public double phQuality(Double x) {

		double result;
		if (x <= 0 || x >= 12)
			result = 0;
		else if (x > 0 && x <= 4) {
			result = x / 4.0;
		} else if (x > 4 && x <= 7.5) {
			result = x + 0.7;
		} else {
			result = x - 0.7;
		}

		return result;
	}

	public double oxygenQuality(Double x) {

		Double index;
		if (x < 10) {
			index = x;
		} else {
			index = 10 - (x - 10);
		}
		return index;
	}

	public double tempQuality(Double x) {

		Double index;
		if (x < 0) {
			index = (-0.3) * x;
		} else {
			index = 0.3 * x;
		}
		return index;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getQuality() {
		return calculateQuality();
	}
	
	public void setQuality(double quality) {
		this.waterQuality = quality;
	}
	
	public Map<String, String> getMeasurements() {
		return measurements;
	}

	public void setMeasurements(Map<String, String> measurements) {
		this.measurements = measurements;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (String key : measurements.keySet()) {
			str.append("," + key + ":" + measurements.get(key));
		}

		return "{lat:" + lat + ",long:" + longitude + str.toString() + "}";
	}

	public String getUrl() {
		StringBuilder serverUrl = new StringBuilder(
				"http://192.168.1.2:1337/measurement/create?latitude="
						+ this.lat + "&longitude=" + this.longitude
						+ "&quality=" + this.waterQuality);
		for (String key : this.measurements.keySet()) {
			serverUrl.append("&" + Utils.encodeURIComponent(key) + "="
					+ Utils.encodeURIComponent(measurements.get(key)));
		}
		return serverUrl.toString();
	}
}
