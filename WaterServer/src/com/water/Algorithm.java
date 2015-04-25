package com.water;

import java.util.Date;

public interface Algorithm {

	void setData(DataSet data);

	double[] predict(Date startDate, Date endDate);

}
