package com.water;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.math3.util.Pair;

import parser.model.Measurement;

public class SimpleRegression implements Algorithm {

	private List<Measurement> pastData;
	
	@Override
	public void setData(DataSet data) { // DataSet
		
		pastData = data.getData();
	}

	public Map<Pair<Double, Double>, Map<Date, Double> > predict(Date endDate) {
		
		Map<Pair<Double, Double>, Map<Date, Double> > result = new HashMap<Pair<Double,Double>, Map<Date,Double>>(); 
		Calendar c = Calendar.getInstance();
		while(true) {
			
			c.add(Calendar.DATE, 1);
			Date currentDate = c.getTime();
			if (!currentDate.equals(endDate)) {
				Map<Pair<Double, Double>, Double> predictOne = predictOne(currentDate, pastData);
				for (Pair<Double, Double> pair : predictOne.keySet()) {
					
					if(result.get(pair) == null) {	
						result.put(pair, new HashMap<Date, Double>());
					}
					result.get(pair).put(currentDate, predictOne.get(pair));	
				}
			} else {
				
				break;
			}
		}
		
		return result;
	}

	public Map<Pair<Double, Double>, Double> predictOne(Date date, List<Measurement> pastData) {
		
		Map<Pair<Double, Double>, List<Double>> map = new HashMap<Pair<Double,Double>, List<Double>>();
		for(int i = 0; i < pastData.size() - 1; i++) {
			Pair<Double, Double> entry = new Pair<>(pastData.get(i).getLat(), pastData.get(i).getLongitude());
			
				if(map.containsKey(entry))	{
					map.get(entry).add(pastData.get(i).getQuality());
				} else {
					ArrayList<Double> arrayList = new ArrayList<Double>();
					arrayList.add(pastData.get(i).getQuality());
					map.put(entry, arrayList);
				}
		}
		
		Map<Pair<Double, Double>, Double> map2 = new HashMap<Pair<Double,Double>, Double>();
		
		for(Pair<Double, Double> entry : map.keySet()) {
			
			Double averageQualityIndex = 0.0;
			List<Double> list = map.get(entry);
			for(Double value : list) {
				averageQualityIndex += value;
			}
			averageQualityIndex /= list.size();
			Random rand = new Random(2);
			
			double value = averageQualityIndex + rand.nextDouble() -1;
			map2.put(entry, value);
			Measurement measurm = new Measurement(entry.getFirst(), entry.getSecond(), date, new HashMap<String, String>());
			measurm.getMeasurements().put("waterQuality", ""+value);
			pastData.add(measurm);
		}
		return map2;
	}

}
