package com.origami.sgr.util;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class DateUtilRpp implements Serializable {
	
	
	public Date addDays(Date d, int days){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		calendar.add(Calendar.DAY_OF_MONTH, days);
		return calendar.getTime();
	}
	
}
