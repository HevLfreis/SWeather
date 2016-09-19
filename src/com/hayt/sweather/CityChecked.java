package com.hayt.sweather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import android.app.Activity;

public class CityChecked{
	
	
	public static boolean existed(InputStream inputStream,String et) {
		if (et.length() == 0) return false;
		InputStreamReader inputStreamReader = null;
		try {
			inputStreamReader = new InputStreamReader(inputStream, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		BufferedReader reader = new BufferedReader(inputStreamReader);

		String line;
		try {
			while ((line = reader.readLine()) != null) {
				if(line.endsWith(et)){
					return true;

				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
