package com.water;

import java.util.List;

import parser.model.Measurement;

public class DataSet {

	private List<Measurement> measurements;
	
	public DataSet(List<Measurement> list) {
		
		measurements = list;
	}

	public List<Measurement> getData() {
		
		
		return measurements;
	}
}
