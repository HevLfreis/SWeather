package com.hayt.sweather;

import java.util.Calendar;



public class TimeCal {

	private int second;
	private int minute;
	private int hour;
	private int dayofyear;
	private int year;
	private int month;
	private int dayofmonth;

	private int getYear, getDayofMonth, getMonth = 0;
	
	public TimeCal(){
	     Calendar calendar =Calendar.getInstance();
         this.second =calendar.get(Calendar.SECOND);
         this.minute =calendar.get(Calendar.MINUTE);
         this.hour =calendar.get(Calendar.HOUR_OF_DAY);
         this.dayofyear =calendar.get(Calendar.DAY_OF_YEAR);
         this.year =calendar.get(Calendar.YEAR);
         this.month =calendar.get(Calendar.MONTH);
         this.dayofmonth =calendar.get(Calendar.DAY_OF_MONTH);
	}

	public int[] getCurrentDate() {
		return new int[]{month+1, dayofmonth};
	}

	public int getCurrentHour() {
		return hour;
	}
	
	public String getCurrentDateString(){
		return String.valueOf(year)+"年"+String.valueOf(month+1)+"月"+String.valueOf(dayofmonth)+"日";
	}

}
