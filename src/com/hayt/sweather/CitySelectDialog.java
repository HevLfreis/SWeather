package com.hayt.sweather;


import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;

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

public class CitySelectDialog extends Activity {

    private TextView ok, cancel = null;
    private RadioGroup cities = null;
    private RadioButton base, city1, city2, city3 = null;
    private EditText editText;
    private String cityName;
    private String[] namemem;
    private String cityNamefromRB = null;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.city_select);
        ok = (TextView) findViewById(R.id.dialogLeftBtn);
        cancel = (TextView) findViewById(R.id.dialogRightBtn);
        cities = (RadioGroup) findViewById(R.id.radioGroup_cityselect);
        base = (RadioButton) findViewById(R.id.radio_base);
        city1 = (RadioButton) findViewById(R.id.radio_city1);
        city2 = (RadioButton) findViewById(R.id.radio_city2);
        city3 = (RadioButton) findViewById(R.id.radio_city3);

        Intent intent = getIntent();

        id = intent.getIntExtra("page", 0);


        ok.setOnClickListener(new okListener());
        cancel.setOnClickListener(new cancelListener());


        Map<String, ?> cityMap = getSharedPreferences("CITIES_DIALOG", 0).getAll();

        namemem = new String[3];

        int i = 0;
        for (Map.Entry<String, ?> entry : cityMap.entrySet()) {
            namemem[i++] = (String) entry.getValue();
        }

        city1.setText(namemem[2]);
        city2.setText(namemem[1]);
        city3.setText(namemem[0]);

        editText = (EditText) findViewById(R.id.edit_city);
        editText.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                // TODO Auto-generated method stub
                return false;
            }
        });

        base.setChecked(true);

        cities.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub

                if (base.getId() == checkedId) {
                    editText.setVisibility(0);
                } else if (city1.getId() == checkedId) {
                    cityNamefromRB = namemem[2];
                    editText.setVisibility(8);
                } else if (city2.getId() == checkedId) {
                    cityNamefromRB = namemem[1];
                    editText.setVisibility(8);
                } else if (city3.getId() == checkedId) {
                    cityNamefromRB = namemem[0];
                    editText.setVisibility(8);
                }

            }

        });

    }

    class okListener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub

            if (base.isChecked()) {
                InputStream inputStream = getResources().openRawResource(R.raw.citycode);
                cityName = editText.getText().toString();

                if (!CityChecked.existed(inputStream, cityName)) {
                    Toast.makeText(CitySelectDialog.this, "请正确输入", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                //Toast.makeText(CityWeatherSelectDialog.this, cityCode, Toast.LENGTH_SHORT).show();
                namemem[0] = namemem[1];
                namemem[1] = namemem[2];
                namemem[2] = cityName;

                SharedPreferences sp = getSharedPreferences("CITIES_DIALOG", MODE_PRIVATE);
                Editor editor = sp.edit();

                System.out.println(Arrays.toString(namemem));

                for (int i = 0; i < 3; i++) {
                    editor.putString("temp" + String.valueOf(i), namemem[i]);
                }

                editor.commit();

                MainActivity.instance.updateCities(id, cityName);

                Toast.makeText(CitySelectDialog.this, "正在更新...", Toast.LENGTH_SHORT).show();
                Application.NEED_FRESH = true;

            } else {
                cityName = cityNamefromRB;
                System.out.println("ID : " + id + " city : " + cityName + " update");
                MainActivity.instance.updateCities(id, cityName);

                Toast.makeText(CitySelectDialog.this, "���ڸ���...", Toast.LENGTH_SHORT).show();
                Application.NEED_FRESH = true;


            }
            System.out.println("Page new city update : " + cityName);
            MainActivity.CURINDEX = id;
            System.out.println("Page selected: " + MainActivity.CURINDEX);
            finish();

        }

    }


    class cancelListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            finish();
        }
    }

}

			
			

