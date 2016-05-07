package com.hayt.sweather;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import android.R.integer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class WFragment extends Fragment{
	
	private int id = 0;
	private int type = 0;
	private TextView textCity,textWeather,textIndex,textTemp,textTemp2,textTemp3,textTemp4=null;
	private ImageView tq;
	private ImageView imageView = null;
	private String index;
	private Context context = null;
	private ArrayList<HashMap<String, String>> list;
	
	public WFragment(int id, Context context, int type) {
		// TODO Auto-generated constructor stub
		this.id = id;
		this.context = context;
		this.type = type;
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		
		if (type == 0) {
			View fragmentView = inflater.inflate(R.layout.frag_new, container, false);
			imageView = (ImageView) fragmentView.findViewById(R.id.image_add);
			imageView.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//imageView.setBackground(getResources().getDrawable(R.drawable.add_pressed));
					Intent intent = new Intent ();	
					intent.setClass(getActivity(), CitySelectDialog.class);
					intent.putExtra("type", 0);
					intent.putExtra("page", id);
					startActivity(intent);			
				}
			});
			return fragmentView;
		}
		else {
			View fragmentView = inflater.inflate(R.layout.frag_weather, container, false);
			list = new ArrayList<HashMap<String,String>>();
	        
			textCity=(TextView) fragmentView.findViewById(R.id.city_text);
			textWeather=(TextView) fragmentView.findViewById(R.id.weatherinfo_text);
			textTemp=(TextView) fragmentView.findViewById(R.id.temp_text);
			textTemp2=(TextView) fragmentView.findViewById(R.id.temp2_text);
			textTemp3=(TextView) fragmentView.findViewById(R.id.temp3_text);
			textTemp4=(TextView) fragmentView.findViewById(R.id.temp4_text);
			
			textIndex=(TextView) fragmentView.findViewById(R.id.index_text);
			
			
			
			tq=(ImageView) fragmentView.findViewById(R.id.image_tq);

			textCity.setOnClickListener(new textCitylistener());

			DataBaseHelper dbHelper=new DataBaseHelper(context,"cache.db");  
	        SQLiteDatabase db=dbHelper.getReadableDatabase();  
	        
	        Cursor cursor=db.query("cache", new String[]{"timestamp","city" ,"temp1","temp2","temp3","temp4","weather1",
	        		"weather2","weather3","weather4","index_d"}
	        , "city like ?",new String[]{Application.CITYLIST[id]}, null, null, "timestamp desc");  
	        
	        if (cursor == null) {
	        	return fragmentView;
				
			}
	        cursor.moveToNext();
	       
	        
	        
	        String temp1 = null,temp2 = null,  temp3 = null ,temp4 = null,
	        weather1 = null ,weather2 = null ,weather3 = null ,weather4 = null, index="...";
	        
	        if (cursor.getColumnIndex("temp1")!=-1) {
	        	 temp1 = cursor.getString(cursor.getColumnIndex("temp1"));
	             temp2 = cursor.getString(cursor.getColumnIndex("temp2"));
	             temp3 = cursor.getString(cursor.getColumnIndex("temp3"));
	             temp4 = cursor.getString(cursor.getColumnIndex("temp4"));
	        
	             weather1 = cursor.getString(cursor.getColumnIndex("weather1"));
	             weather2 = cursor.getString(cursor.getColumnIndex("weather2"));
	             weather3 = cursor.getString(cursor.getColumnIndex("weather3"));
	             weather4 = cursor.getString(cursor.getColumnIndex("weather4"));
	      
	             index = cursor.getString(cursor.getColumnIndex("index_d"));
			}
	        cursor.close();
	        
	    	db.close();
	    	
			String city = Application.CITYLIST[id];
	    	textCity.setText(city);
	    	textTemp.setText(temp1);
	    	textTemp2.setText(dow(1)+temp2+"    "+weather2);
	    	textTemp3.setText(dow(2)+temp3+"    "+weather3);
	    	textTemp4.setText(dow(3)+temp4+"    "+weather4);
	    	
	    	textWeather.setText(weather1);
	    	textIndex.setText(index);
	    	setTqImage(weather1);
	    		
	    	if(Application.NEED_FRESH==true){
		    	Animation imagenter=AnimationUtils.loadAnimation(getActivity(), R.anim.toast_enter);
		 		tq.startAnimation(imagenter);
		 		Application.NEED_FRESH=false;
		    }
			
			return fragmentView;
		}
		
	}
	
	private String dow(int a) {
		String[] s = {"������","����һ","���ڶ�","������","������","������","������"};
    	Calendar calendar = Calendar.getInstance();
    	if (calendar.get(Calendar.DAY_OF_WEEK)+a>7) {
    		return s[calendar.get(Calendar.DAY_OF_WEEK)+a-8]+"      ";
		}
    	else {
    		return s[calendar.get(Calendar.DAY_OF_WEEK)+a-1]+"      ";
		}
		
		
	}
	
	public void changeState(int type) {
		this.type = type;
	}
	
	class textCitylistener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent =new Intent();
			intent.putExtra("type", 1);
			intent.putExtra("page", id);
			intent.setClass(getActivity(), CitySelectDialog.class);
			startActivity(intent);
		}
		
	}
	
	class exitanimlistener implements AnimationListener{

		@Override
		public void onAnimationEnd(Animation animation) {
			// TODO Auto-generated method stub
			setTqImage(index);
			Animation imagenter=AnimationUtils.loadAnimation(getActivity(), R.anim.toast_enter);
			tq.startAnimation(imagenter);
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
	
	@SuppressWarnings("deprecation")
	private void setTqImage(String info){
		TimeCal thour=new TimeCal();
		int time2=thour.getCurerentHour();
		
		if(info.indexOf("��")!=-1){
		
				if (time2>19||time2<6) {
					tq.setBackgroundDrawable(getResources().getDrawable(R.drawable.tq_sunny_night));
				}
				else {
					tq.setBackgroundDrawable(getResources().getDrawable(R.drawable.tq_sunny_day));
				}
		}
		else if (info.indexOf("�Ե�")!=-1) {
			tq.setBackgroundDrawable(getResources().getDrawable(R.drawable.tq_unknown));
		}
		else if(info.indexOf("ѩ")!=-1){
			if (info.indexOf("��")!=-1) {
				tq.setBackgroundDrawable(getResources().getDrawable(R.drawable.tq_sonwrain));
			}
			else {
				tq.setBackgroundDrawable(getResources().getDrawable(R.drawable.tq_snowy));
			}
			
		}
		else if(info.indexOf("��")!=-1){
			if (info.indexOf("��")!=-1) {
				tq.setBackgroundDrawable(getResources().getDrawable(R.drawable.tq_lightning));
			}
			else if (info.indexOf("С")!=1) {
				tq.setBackgroundDrawable(getResources().getDrawable(R.drawable.tq_rainy_small));
			}
			else if (info.indexOf("��")!=1) {
				tq.setBackgroundDrawable(getResources().getDrawable(R.drawable.tq_rainy_mid));
			}
			else if (info.indexOf("��")!=1) {
				tq.setBackgroundDrawable(getResources().getDrawable(R.drawable.tq_rainy_big));
			}
			else if (info.indexOf("��")!=1) {
				tq.setBackgroundDrawable(getResources().getDrawable(R.drawable.tq_rainy_big));
			}
			else {
				tq.setBackgroundDrawable(getResources().getDrawable(R.drawable.tq_rainy_small));
			}
			
		}
		else if(info.indexOf("��")!=-1){
			if (time2>19||time2<6) {
				tq.setBackgroundDrawable(getResources().getDrawable(R.drawable.tq_cloudy_night));
			}
			else {
				tq.setBackgroundDrawable(getResources().getDrawable(R.drawable.tq_cloudy_day));
			}
		}
		else if(info.indexOf("����")!=-1){
			if (time2>19||time2<6) {
				tq.setBackgroundDrawable(getResources().getDrawable(R.drawable.tq_cloudy_night));
			}
			else {
				tq.setBackgroundDrawable(getResources().getDrawable(R.drawable.tq_cloudy_day));
			}
		}	
		else  {
			tq.setBackgroundDrawable(getResources().getDrawable(R.drawable.tq_unknown));
		}
		
	}
}
