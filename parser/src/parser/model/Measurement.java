package parser.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import parser.Utils;

import com.google.gson.Gson;

public class Measurement {
	
	private double lat;
	private double longitude;
	private Date date;
	private Map<String, String> measurements;
	
	public Measurement(double lat, double longitude, Date date,
			Map<String, String> measurements) {

		this.lat = lat;
		this.longitude = longitude;
		this.date = date;
		this.measurements = measurements;
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
			str.append("," + key+":"+measurements.get(key));
		}
		
		return "{lat:" + lat + ",long:" + longitude + 
				str.toString() +
				"}";
	}
	
	public String getUrl() {
		StringBuilder serverUrl = new StringBuilder("http://192.168.1.2:1337/measurement/create?latitude=" + this.lat + "&longitude=" + this.longitude);
		for(String key : this.measurements.keySet()){
			serverUrl.append("&" + Utils.encodeURIComponent(key) + "=" + Utils.encodeURIComponent(measurements.get(key)));
		}
		return serverUrl.toString();
	}
}
