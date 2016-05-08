package com.hayt.sweather;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;



import android.R.integer;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
	
	public static MainActivity instance = null;
	private ViewPager mPager;
	private MyPagerAdapter mPagerAdapter;
	private RelativeLayout rl;
	
	private ArrayList<String> cities;
	
//	private String[] cities = {"","",""};
//	private String[] citiesUrls = {"","",""};
	private JsonAsync j;
	
	public static int CURRINDEX = 0;
	
	private boolean net = false;
	
	private LinkedList<WFragment> fragmentList;
	private WFragment wf1, wf2, wf3;
	private boolean resume = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
        instance = this;
        cities = new ArrayList<String>();
        fragmentList = new LinkedList<WFragment>();
//        wf1 = new WFragment(0, instance, 0);
//        wf2 = new WFragment(1, instance, 0);
//        wf3 = new WFragment(2, instance, 0);
//        fragmentList.add(wf1);
//        fragmentList.add(wf2);
//        fragmentList.add(wf3);
      
        mPager = (ViewPager)findViewById(R.id.tabpager);
        
        //read setting data for create fragments
        Map<String, ?> cityMap = getSharedPreferences("CITIES_LIST", 0).getAll();
        int i = 0;
        for(Map.Entry<String, ?>  entry : cityMap.entrySet()) {
        	cities.add(entry.getKey());
        	fragmentList.add(new WFragment(i++, instance, 1));
        	System.out.println("City added : "+entry.getKey());
        }
        
        if (i != Application.PAGE_NUM) {
        	System.out.println("Wrong ! ");
			
		}
        fragmentList.add(new WFragment(Application.PAGE_NUM, instance, 0));
        
//        int[] cityState = {sp.getInt("cityState0",0),sp.getInt("cityState1",0),sp.getInt("cityState2",0)};
//        cities[0] = sp.getString("cityName0", "");
//        cities[1] = sp.getString("cityName1", "");
//        cities[2] = sp.getString("cityName2", "");
        
        System.out.println("Main start");
//        System.out.println("city1 is : "+cities[0]+" state: "+cityState[0]);
//        System.out.println("city2 is : "+cities[1]+" state: "+cityState[1]);
//        System.out.println("city3 is : "+cities[2]+" state: "+cityState[2]);
        
        
//        if (cityState[0] == 1) {
//			wf1.changeState(1);
//		}
//        if (cityState[1] == 1) {
//			wf2.changeState(1);
//		}
//        if (cityState[2] == 1) {
//			wf3.changeState(1);
//		}
        
//        for (int i = 0; i < 3; i++) {
//			if (cityState[i] == 1) {
//				Application.CITYLIST[i] = cities[i];
//				
//				// Important
//				// the weather interface is deprecated, you can change it to a new interface
//				citiesUrls[i] = "http://api.map.baidu.com/telematics/v3/weather?location="+cities[i]+"&output=json&ak=QdzoydNb3Ix9Qfik2sbRrOfm";
//				System.out.println("WFragment added :" + citiesUrls[i]);
//			}
//		}
        
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());	
		mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragmentList);
        mPager.setAdapter(mPagerAdapter);
		mPager.setOffscreenPageLimit(3);
		mPager.setCurrentItem(1);
		Animation imagenter=AnimationUtils.loadAnimation(this, R.anim.fade_in);
 		mPager.startAnimation(imagenter);
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        Boolean isInternetPresent = cd.isConnectingToInternet(); 
		 if (isInternetPresent==true) {
			 System.out.println("Update all page");
			 
			 //
			 getWeather(Application.PAGE_NUM);
		 }
		 else {
			 Toast.makeText(MainActivity.this,"没有网络连接，无法更新天气",Toast.LENGTH_SHORT).show();
			 net = false;
		}
		 
		 //gesture
		 rl = (RelativeLayout) findViewById(R.id.title);

		final
		GestureDetector gd = new GestureDetector(instance, new GestureListener());
		 
		 rl.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (gd.onTouchEvent(event)) {
					startDeleteAnim();
				}
				
				return true;
			}
		});
   
        
	}
	
	public void startDeleteAnim() {
		Animation imagenter=AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
 		mPager.startAnimation(imagenter);
 		SharedPreferences sp2 =getSharedPreferences("SETTINGS", MODE_PRIVATE);
        Editor editor2 = sp2.edit();
        editor2.putInt("cityState"+String.valueOf(CURRINDEX), 0);
        editor2.putString("cityName"+String.valueOf(CURRINDEX), "");
        editor2.commit();
        if (CURRINDEX == 0) {
			wf1.changeState(0);
		}
        if (CURRINDEX == 1) {
			wf2.changeState(0);
		}
        if (CURRINDEX == 2) {
			wf3.changeState(0);
		}
        System.out.println("Page index deleted： " + CURRINDEX);
        
 		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mPagerAdapter.notifyDataSetChanged();
		        //Application.NEED_FRESH=true;
		        mPager.setCurrentItem(CURRINDEX);
		 		
			}
		}, 700);
        
        
	}

	
	private void getWeather(int index){
		
		 ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
		 
		 Boolean isInternetPresent = cd.isConnectingToInternet(); 
		 if (isInternetPresent==true) {

			 j=new JsonAsync();
			 j.execute(String.valueOf(index));	
		
			  
		}
		 else {
			 return;
		 }
	}

	private class JsonAsync extends AsyncTask<String, Integer, String> {

		@Override
		protected void onPostExecute(String result) {

			
			
			mPagerAdapter.notifyDataSetChanged();
			if (resume) {
				mPager.setCurrentItem(CURRINDEX);
				resume =false;
			}
			System.out.println("Updated weather");

		}

		protected String doInBackground(String... params) {
			if (params.length != 1) return "";
			
			
			int index = Integer.parseInt(params[0]);	
					
			if (index == Application.PAGE_NUM) {
				for (int i = 0; i < index; i++) {
					updateDb(i);					
				}
			}
			
			else {
				updateDb(index);
			}
				
			
			return "";
		}


		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}
		
		private void updateDb(int index) {
			try {
				
				DataBaseHelper helper =new DataBaseHelper(MainActivity.this, "cache.db");
				SQLiteDatabase db=helper.getWritableDatabase();
				ContentValues values=new ContentValues();

				String json = HttpUtil.getJson(WeatherProvider.wrap(cities.get(index)));
				
				System.out.println("Update weather from: " + WeatherProvider.wrap(cities.get(index))); 
				
				JSONObject jsonObject = new JSONObject(json);
				
				if (jsonObject.getInt("error") != 0) {
					System.out.println("NetError");
					Toast.makeText(MainActivity.this,"错误，更新失败",Toast.LENGTH_SHORT).show();
					return;
				}
				
				JSONObject results = jsonObject.getJSONArray("results").getJSONObject(0);
				JSONArray weatherData = results.getJSONArray("weather_data");
				
				//System.out.println(cities[i]);
				values.put("pageid", index);
				values.put("timestamp", System.currentTimeMillis());
				 
				 // Danger !!!!!!!!!!
				String city = results.getString("currentCity");
				values.put("city", city);
				 
				for (int j = 1; j < weatherData.length() + 1; j++) {
					JSONObject data = (JSONObject)weatherData.opt(j - 1);
					values.put("temp"+String.valueOf(j), data.getString("temperature"));
					values.put("weather"+String.valueOf(j), data.getString("weather"));
				}
				
				JSONObject index_d = results.getJSONArray("index").getJSONObject(0);
				values.put("index_d", index_d.getString("des"));
				db.insert("cache", null, values);
				
				db.close();
				 					
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Except founded");
				
			}
			
		}
		

	} 
	
	
	protected void onResumeFragments() {
		
		
		// TODO Auto-generated method stub
		int cur = CURRINDEX;
		
		if (Application.NEED_FRESH == true) {
			if (cur == Application.PAGE_NUM) {
				Application.PAGE_NUM ++;
				fragmentList.add(cur, new WFragment(cur, instance, 1));
				fragmentList.removeLast();
				fragmentList.add(new WFragment(Application.PAGE_NUM, instance, 0));
			}
		

//			mPagerAdapter.update(fragmentList);
			
			for (WFragment f: fragmentList) {
				System.out.println(f.getType());
			}
			getWeather(cur);
		}
//			SharedPreferences sp=getSharedPreferences("SETTINGS", 0);
//	        int state = sp.getInt("cityState" + String.valueOf(cur), 0);                                                        bindService( , null, state)
//	        
//	        cities[cur] = sp.getString("cityName" + String.valueOf(cur), "");
//	        
//	       
//	        if (state == 1) {
//				((WFragment) fragmentList.get(cur)).changeState(1);
//			}	        
//	        
//			Application.CITYLIST[cur] = cities[cur];
//
//			citiesUrls[cur] = "http://api.map.baidu.com/telematics/v3/weather?location="+cities[cur]+"&output=json&ak=QdzoydNb3Ix9Qfik2sbRrOfm";
//			System.out.println("Resume WFragment added :" + cities[cur]);
//			
//	     
//	        resume = true;
//	        
//	        fragmentList.add(new WFragment(3, instance, 0));
//			mPagerAdapter.update(fragmentList);
//			getWeather(cur);
//
//		}
        super.onResumeFragments();
    }
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (net == true) {
			j.cancel(true);
		}
		System.out.println("Main Destroyed");
		super.onDestroy();	
	}
	
	public class MyOnPageChangeListener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			CURRINDEX=arg0;
		}
		
	}	
	
	public void updateCities(int index, String city) {
		if (cities.size() == 0||index == Application.PAGE_NUM) cities.add(city);
		else cities.set(index, city);
	}


}
