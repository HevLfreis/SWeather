package com.hayt.sweather;

import java.util.Calendar;



public class TimeCal {
	
	//ϵͳ����ʱ��
	private int second;
	private int minute;
	private int hour;
	private int dayofyear;
	private int year;
	private int month;
	private int dayofmonth;
	
	//�û�����ʱ��
	private int getYear,getDayofMonth,getMonth=0;
	
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
	
	//���ص�������
	public int[] getCurrentDate() {
		int result[]={month+1,dayofmonth};
		return result;
		
	}
	public int getCurerentHour() {
		return hour;
	}
	
	public String getCurrentDateString(){
		String result=String.valueOf(year)+"��"+String.valueOf(month+1)+"��"+String.valueOf(dayofmonth)+"��";
		return result;
		
		
	}

}
