package com.hayt.sweather;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;



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
	private ViewPager mTabPager;
	private RelativeLayout rl;

	private String[] cities = {"","",""};
	private String[] citiesUrls = {"","",""};
	private JsonAsync j;
	
	public static int CURRINDEX = 0;
	
	private boolean net = false;
	
	private isLoadDataListener loadLisneter;
	
	private List<Fragment> fragmentList;
	private WFragment wf1, wf2, wf3;
	private boolean resume = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
        instance = this;
        
        
        fragmentList = new ArrayList<Fragment>();
        wf1 = new WFragment(0, instance, 0);
        wf2 = new WFragment(1, instance, 0);
        wf3 = new WFragment(2, instance, 0);
        fragmentList.add(wf1);
        fragmentList.add(wf2);
        fragmentList.add(wf3);
      
        mTabPager = (ViewPager)findViewById(R.id.tabpager);
        
        //read setting data for create fragments
        SharedPreferences sp = getSharedPreferences("SETTINGS", 0);
        int[] cityState = {sp.getInt("city1",0),sp.getInt("city2",0),sp.getInt("city3",0)};
        cities[0] = sp.getString("city_weather1", "");
        cities[1] = sp.getString("city_weather2", "");
        cities[2] = sp.getString("city_weather3", "");
       
        System.out.println("city1 is : "+cities[0]+cityState[0]);
        System.out.println("city2 is : "+cities[1]+cityState[1]);
        System.out.println("city3 is : "+cities[2]+cityState[2]);
        
        
        if (cityState[0] == 1) {
			wf1.changeState(1);
		}
        if (cityState[1] == 1) {
			wf2.changeState(1);
		}
        if (cityState[2] == 1) {
			wf3.changeState(1);
		}
        
        for (int i = 0; i < 3; i++) {
			if (cityState[i] == 1) {
				Application.CITYLIST[i] = cities[i];
				//fragmentList.add(new WFragment(i, instance, 1));
				
				// Important
				// the weather interface is deprecated, you can change it to a new interface
				citiesUrls[i] = "http://api.map.baidu.com/telematics/v3/weather?location="+cities[i]+"+&output=json&ak=QdzoydNb3Ix9Qfik2sbRrOfm";
				System.out.println("WFragment added :" + citiesUrls[i]);
			}
		}
        
		mTabPager.setOnPageChangeListener(new MyOnPageChangeListener());	
        mTabPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), fragmentList));
		mTabPager.setOffscreenPageLimit(3);
		mTabPager.setCurrentItem(1);
		Animation imagenter=AnimationUtils.loadAnimation(this, R.anim.fade_in);
 		mTabPager.startAnimation(imagenter);
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        Boolean isInternetPresent = cd.isConnectingToInternet(); 
		 if (isInternetPresent==true) {
			 
			 getWeather();
		 }
		 else {
			 Toast.makeText(MainActivity.this,"没有网络连接，无法更新天气",Toast.LENGTH_SHORT).show();
			 net=false;
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
 		mTabPager.startAnimation(imagenter);
 		SharedPreferences sp2 =getSharedPreferences("SETTINGS", MODE_PRIVATE);
        Editor editor2 = sp2.edit();
        editor2.putInt("city"+String.valueOf(CURRINDEX+1), 0);
        editor2.putString("city_weather_code"+String.valueOf(CURRINDEX+1), "");
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
 		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mTabPager.getAdapter().notifyDataSetChanged();
		        //Application.NEED_FRESH=true;
		        mTabPager.setCurrentItem(CURRINDEX);
		 		System.out.println(CURRINDEX);
		 		
			}
		}, 700);
        
        
	}

	
	private void getWeather(){
		
		 ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
		 
		 Boolean isInternetPresent = cd.isConnectingToInternet(); 
		 if (isInternetPresent==true) {
			 setLoadDataComplete(new isLoadDataListener() {
				
				@Override
				public void loadComplete() {
					// TODO Auto-generated method stub
					mTabPager.getAdapter().notifyDataSetChanged();
					if (resume) {
						mTabPager.setCurrentItem(CURRINDEX);
						resume =false;
					}
					//mTabPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), fragmentList));
					System.out.println("update weather");
				}
			});
			 j=new JsonAsync();
			 System.out.println("Urls prepared : "+Arrays.toString(citiesUrls));
			 j.execute(citiesUrls[CURRINDEX]);	 
		}
		 else {
			 return;
		 }
	}

	private class JsonAsync extends AsyncTask<String, Integer, String> {

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			  if (loadLisneter != null) {
	                loadLisneter.loadComplete();
	            }
			  Application.UPDATE_OVER=true;

		}

		@Override
		protected String doInBackground(String... params) {
			
			for (int i = 0; i < params.length; i++) {
				if (!params[i].equals("")) {
					
					try {
						JSONObject jsonObject;
						
						DataBaseHelper helper =new DataBaseHelper(MainActivity.this, "cache.db");
						SQLiteDatabase db=helper.getWritableDatabase();
						ContentValues values=new ContentValues();

						String json = HttpUtil.getJson(params[i]);
						
						jsonObject = new JSONObject(json);
						
						if (jsonObject.getInt("error") != 0) {
							Toast.makeText(MainActivity.this,"错误，更新失败",Toast.LENGTH_SHORT).show();
							break;
						}
						
						JSONObject results = jsonObject.getJSONArray("results").getJSONObject(0);
						JSONArray weatherData = results.getJSONArray("weather_data");
						
						//System.out.println(cities[i]);
						
						 values.put("timestamp", System.currentTimeMillis());
						 values.put("city", cities[CURRINDEX]);
						 
						 for (int j = 1; j < weatherData.length() + 1; j++) {
							 JSONObject data = (JSONObject)weatherData.opt(j - 1); 
							 System.out.println(data);
							 values.put("temp"+String.valueOf(j), data.getString("temperature"));
							 values.put("weather"+String.valueOf(j), data.getString("weather"));
						 }
			
						 JSONObject index = results.getJSONArray("index").getJSONObject(0);
						 values.put("index_d", index.getString("des"));
						 db.insert("cache", null, values);
						 
						 System.out.println(values.toString());
						
						 db.close();
						 					
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("Except founded");
						
					}
				}
	
			}
				
			
			return "";
		}


		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}
		

	} 
	
	
	protected void onResumeFragments() {
		// TODO Auto-generated method stub
		if (Application.NEED_FRESH==true) {
			SharedPreferences sp=getSharedPreferences("SETTINGS", 0);
	        int[] cityState={sp.getInt("city1",0),sp.getInt("city2",0),sp.getInt("city3",0)};
	        cities[0] = sp.getString("city_weather1", "");
	        cities[1] = sp.getString("city_weather2", "");
	        cities[2] = sp.getString("city_weather3", "");
	        
	        System.out.println("city1 is : "+cityState[0]);
	        System.out.println("city2 is : "+cityState[1]);
	        System.out.println("city3 is : "+cityState[2]);
	        
	        if (cityState[0] == 1) {
				wf1.changeState(1);
			}
	        if (cityState[1] == 1) {
				wf2.changeState(1);
			}
	        if (cityState[2] == 1) {
				wf3.changeState(1);
			}
	        
	        for (int i = 0; i < 3; i++) {
				if (cityState[i] == 1) {
					Application.CITYLIST[i] = cities[i];
					//fragmentList.add(new WFragment(i, instance, 1));
					cities[i] = cities[i];
					citiesUrls[i] = "http://api.map.baidu.com/telematics/v3/weather?location="+cities[i]+"+&output=json&ak=QdzoydNb3Ix9Qfik2sbRrOfm";
					System.out.println("Resume WFragment added :" + citiesUrls[i]);
				}

			}
	        System.out.println("resume:"+CURRINDEX);
	        resume = true;
			getWeather();
//			Constants.NEED_FRESH=false;
		}
        super.onResumeFragments();
    }
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (net==true) {
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
	
	
	private interface isLoadDataListener {
        public void loadComplete();
    }
	
	public void setLoadDataComplete(isLoadDataListener dataComplete) {
        this.loadLisneter = dataComplete;
    }


}
