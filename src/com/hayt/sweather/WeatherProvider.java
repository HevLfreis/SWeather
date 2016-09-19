package com.hayt.sweather;

public class WeatherProvider {
	
	private final static String api = "http://api.map.baidu.com/telematics/v3/weather?location=   &output=json&ak=QdzoydNb3Ix9Qfik2sbRrOfm";	
	public static String wrap(String cityName) {
		return api.replace("   ", cityName);
	}
}
