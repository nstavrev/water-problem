package com.water;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import parser.model.Measurement;

public class SimpleRegression implements Algorithm {

	private List<Measurement> pastData;
	
	@Override
	public void setData(DataSet data) { // DataSet
		
		pastData = data.getData();
	}

	@Override
	public Map<Entry<Double, Double>, Double> predict(Date endDate) {
		
		for(int i = 0; i < pastData.size() - 1; i++) {
			if(pastData.get(i).getLat() == pastData.get(i + 1).getLat() &&
					pastData.get(i).getLongitude() == pastData.get(i + 1).getLongitude()) {
						
					}
		}
		
		for(int i = 0; i < pastData.size() - 1; i++) {
			if(pastData.get(i).getLat() == pastData.get(i + 1).getLat() &&
			pastData.get(i).getLongitude() == pastData.get(i + 1).getLongitude()) {
				//qualityPredictionByDay[j] += pastData.getQuality();
			}
			//qualityPredictionByDay[j++] /= i;
			
		}
		return null;
	}

}
