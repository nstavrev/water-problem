package parser;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface Parser {
	
	List<String> getAllStates() throws IOException;
	
	List<String> getAllMeasurements() throws IOException;
	
	List<String> getAllStationNumbers(String state, String measurement) throws IOException;
	
	List<String> getAllParameters(String state, String stationNumber) throws IOException;
	
	Map<String, Map<String, String>> crawlData(String state, String stationNumber, List<String> parameters) throws IOException, ParseException;
	
	Map<String, Double> getLocation(String state, String stateNumber) throws IOException;
	
}
