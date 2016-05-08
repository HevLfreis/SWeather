package com.hayt.sweather;


import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;

public class Application extends Activity{
	
	private SharedPreferences settings, cityList, cityDialog;
	private int count;
	public static int PAGE_NUM = 0;
//	public static boolean UPDATE_OVER = false;  //weather update finished
	public static boolean NEED_FRESH = false;
//	public static String[] CITYLIST = {"","",""};;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);

		
		settings = getSharedPreferences("SETTINGS", MODE_PRIVATE);
//		cityList = getSharedPreferences("CITIES_LIST", MODE_PRIVATE);
		cityDialog = getSharedPreferences("CITIES_DIALOG", MODE_PRIVATE);
		count = settings.getInt("count", 0);
		PAGE_NUM = settings.getInt("page-num", 0);
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				if(count == 0){

			        Editor editor = settings.edit();
//			        editor.putInt("city-state-0", 0);
//			        editor.putInt("cityState1", 0);
//			        editor.putInt("cityState2", 0);
//			        editor.putString("city-name-0", "");
//			        editor.putString("cityName1", "");
//			        editor.putString("cityName2", "");
			        
			        editor.putInt("count", 1);
			        editor.putInt("page-num", 0);
			        editor.commit();
			        
			        Editor editor2 = cityDialog.edit();
			        //dialog写入，主activity读出
			        //dialog用作设置缓存
			        editor2.putString("temp0", "上海");
			        editor2.putString("temp1", "杭州");
			        editor2.putString("temp2", "北京");
//			        editor2.putString("city-name-0", "上海");
//			        editor2.putString("city-name-1", "杭州");
//			        editor2.putString("city-name-2", "北京");
			        editor2.commit();

			        Intent intent =new Intent();
					intent.setClass(Application.this, MainActivity.class);	
					startActivity(intent);			
					Application.this.finish();

				}
				else if(count != 0){
					Intent intent = new Intent ();	
					intent.setClass(Application.this, MainActivity.class);
					startActivity(intent);			
					Application.this.finish();
				}
			}
		}, 2000);
		

	}

}
