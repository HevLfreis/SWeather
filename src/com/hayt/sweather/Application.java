package com.hayt.sweather;


import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;

public class Application extends Activity{
	
	private SharedPreferences setting1, setting2;
	private int count = 0;
	public static int TAB_COUNT = 3;
	public static boolean UPDATE_OVER = false;  //weather update finished
	public static boolean NEED_FRESH = false;
	public static String[] CITYLIST = {"","",""};;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);

		
		setting1=getSharedPreferences("SETTINGS", MODE_PRIVATE);
		setting2=getSharedPreferences("SETTING_CITY", MODE_PRIVATE);
		count =setting1.getInt("count", 0);
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				if(count == 0){

			        Editor editor = setting1.edit();
			        editor.putInt("city1", 0);
			        editor.putInt("city2", 0);
			        editor.putInt("city3", 0);
			        editor.putString("city_weather1", "");
			        editor.putString("city_weather2", "");
			        editor.putString("city_weather3", "");
			        
			        editor.putInt("count", 1);
			        editor.commit();
			        
			        Editor editor2 = setting2.edit();
			        //dialog写入，主activity读出
			        //dialog用作设置缓存
			        editor2.putString("city_weather_name1", "上海");
			        editor2.putString("city_weather_name2", "杭州");
			        editor2.putString("city_weather_name3", "北京");
			        editor2.commit();

			        Intent intent =new Intent();
					intent.setClass(Application.this, MainActivity.class);	
					startActivity(intent);			
					Application.this.finish();

				}
				else if(count!=0){
					Intent intent = new Intent ();	
					intent.setClass(Application.this, MainActivity.class);
					startActivity(intent);			
					Application.this.finish();
				}
			}
		}, 2000);
		

	}

}
