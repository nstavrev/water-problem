package com.water;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

public interface Algorithm {

	void setData(DataSet data);

	Map<Entry<Double, Double>, Double> predict(Date endDate);

}
