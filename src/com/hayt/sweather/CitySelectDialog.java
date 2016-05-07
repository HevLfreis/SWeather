package com.hayt.sweather;


import java.io.InputStream;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class CitySelectDialog extends Activity{
	
	
private TextView ok,cancel=null;
private RadioGroup citys =null;
private RadioButton base, city1, city2, city3 =null;
private EditText editText;
private String cityName, namemem1, namemem2, namemem3;
private String cityNamefromRB=null;
private int id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.city_select);
		ok=(TextView) findViewById(R.id.dialogLeftBtn);
		cancel=(TextView) findViewById(R.id.dialogRightBtn);
		citys=(RadioGroup) findViewById(R.id.radioGroup_cityselect);
		base=(RadioButton) findViewById(R.id.radio_base);
		city1=(RadioButton) findViewById(R.id.radio_city1);
		city2=(RadioButton) findViewById(R.id.radio_city2);
		city3=(RadioButton) findViewById(R.id.radio_city3);
		
		Intent intent = getIntent();

		id = intent.getIntExtra("page", 0);
		
//		if (intent.getIntExtra("type", 0) == 0) {
//			ok.setOnClickListener(new ok0listener());
//			cancel.setOnClickListener(new cancellistener());
//		}
//		else {
//			ok.setOnClickListener(new ok1listener());
//			cancel.setOnClickListener(new cancellistener());
//		}
		
		ok.setOnClickListener(new oklistener());
		cancel.setOnClickListener(new cancellistener());

		//得到设置数据
		SharedPreferences sp=getSharedPreferences("SETTING_CITY", MODE_PRIVATE);
 
		namemem1=sp.getString("city_weather_name1", "无");
		namemem2=sp.getString("city_weather_name2", "无");
		namemem3=sp.getString("city_weather_name3", "无");

		//初始化radiobutton文字
		city1.setText(namemem1);
		city2.setText(namemem2);
		city3.setText(namemem3);

        editText=(EditText) findViewById(R.id.edit_city);
   	    editText.setOnEditorActionListener(new OnEditorActionListener(){

		@Override
		public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
			// TODO Auto-generated method stub
			//Toast.makeText(MainActivity.this, "正在查询", Toast.LENGTH_SHORT).show(); 
			return false;
		}			
   	   });

	   base.setChecked(true);

	   citys.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
			

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				
                if (base.getId()==checkedId) {
	        	editText.setVisibility(0);
					
				}
                else if (city1.getId()==checkedId) {

                	cityNamefromRB=namemem1;
                	editText.setVisibility(8);
                	

					
				}
                else if (city2.getId()==checkedId) {

                	cityNamefromRB=namemem2;
                	editText.setVisibility(8);

					
				}
                else if (city3.getId()==checkedId) {

                	cityNamefromRB=namemem3;
                	editText.setVisibility(8);

					
				}
               
			}
	
		});
		
	}
	
	class oklistener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub

			if (base.isChecked()) {
				InputStream inputStream = getResources().openRawResource(R.raw.citycode);
				cityName = editText.getText().toString();
				System.out.println("cityname type 0 :"+cityName);
				if (!CityChecked.existed(inputStream, cityName)) {
					Toast.makeText(CitySelectDialog.this, "请正确输入", Toast.LENGTH_SHORT).show();
					finish();
					return;
					
				}
				//Toast.makeText(CityWeatherSelectDialog.this, cityCode, Toast.LENGTH_SHORT).show();
			
                namemem3=namemem2;
                namemem2=namemem1;
                namemem1=cityName;
                
                //更新selector设置缓存
			    SharedPreferences sp =getSharedPreferences("SETTING_CITY", MODE_PRIVATE);
		        Editor editor = sp.edit();
		        
		        editor.putString("city_weather_name1", namemem1);
		        editor.putString("city_weather_name2", namemem2);
		        editor.putString("city_weather_name3", namemem3);
		        editor.commit();
		        
		        //更新主setting设置
		        SharedPreferences sp2 =getSharedPreferences("SETTINGS", MODE_PRIVATE);
		        Editor editor2 = sp2.edit();
		        editor2.putString("city_weather"+String.valueOf(id+1), namemem1);
		        editor2.commit();
		        Toast.makeText(CitySelectDialog.this,"正在更新...",Toast.LENGTH_SHORT ).show();
		        Application.NEED_FRESH=true;
			
			}
			else {
				cityName=cityNamefromRB;
				SharedPreferences sp2 =getSharedPreferences("SETTINGS", MODE_PRIVATE);
		        Editor editor2 = sp2.edit();
		        editor2.putInt("city"+String.valueOf(id+1), 1);
		        editor2.putString("city_weather"+String.valueOf(id+1), cityName);
		        editor2.commit();
		        Toast.makeText(CitySelectDialog.this,"正在更新...",Toast.LENGTH_SHORT ).show();
		        Application.NEED_FRESH=true;
          	
				
			}
			System.out.println("page new city update : "+cityName);
			MainActivity.CURRINDEX = id;
			System.out.println("page selcted: "+MainActivity.CURRINDEX);
			finish();
			
		}
		
	}
	

	class cancellistener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			finish();
		}
		
	}

}
			
			
			

