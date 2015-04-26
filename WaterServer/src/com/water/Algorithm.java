package com.water;

import java.util.Date;
import java.util.Map;

import org.apache.commons.math3.util.Pair;

public interface Algorithm {

	void setData(DataSet dSet);

	Map<Pair<Double, Double>, Map<Date, Double> > predict(Date endDate);

}
