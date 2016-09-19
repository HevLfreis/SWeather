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

    private JsonAsync j;

    public static int CURINDEX = 0;

    private boolean net = false;
    private boolean deleting = false;

    private LinkedList<WFragment> fragmentList;

    private boolean resume = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        instance = this;


        mPager = (ViewPager) findViewById(R.id.tabpager);


        updateCitiesFromSettings();

        System.out.println("Main start");


        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragmentList);
        mPager.setAdapter(mPagerAdapter);
//		mPager.setOffscreenPageLimit(3);
        mPager.setCurrentItem(Application.INT_INDEX);
        Animation imagenter = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        mPager.startAnimation(imagenter);
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        Boolean isInternetPresent = cd.isConnectingToInternet();

        if (isInternetPresent) {
            System.out.println("Update all page");
            getWeather(Application.PAGE_NUM);
        } else {
            Toast.makeText(MainActivity.this, "没有网络连接，无法更新天气", Toast.LENGTH_SHORT).show();
            net = false;
        }

        //gesture
        rl = (RelativeLayout) findViewById(R.id.title);

        final GestureDetector gd = new GestureDetector(instance, new GestureListener());

        rl.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub

                if (CURINDEX != Application.PAGE_NUM && !deleting && gd.onTouchEvent(event)) {
                    deleting = true;
                    System.out.println("Delete page : " + CURINDEX);
                    startDeleteAnim();
                    return false;
                }
                return true;
            }
        });


    }

    public void startDeleteAnim() {

        Animation imagenter = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
        mPager.startAnimation(imagenter);


        System.out.println("Page index deleted: " + CURINDEX);

        cities.remove(CURINDEX);
        fragmentList.remove(CURINDEX);
        Application.PAGE_NUM--;
        for (int i = 0; i < fragmentList.size() - 1; i++) {
            fragmentList.get(i).setIndexId(i);

        }
        fragmentList.removeLast();
        fragmentList.add(new WFragment(Application.PAGE_NUM, instance, 0));
        System.out.println(fragmentList.toString());


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                CURINDEX = CURINDEX - 1 < 0 ? 0 : CURINDEX - 1;
                System.out.println(CURINDEX);
                mPager.setCurrentItem(CURINDEX);
                deleting = false;
                mPagerAdapter.notifyDataSetChanged();

            }
        }, 700);
    }


    private void getWeather(int index) {

        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());

        Boolean isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {

            j = new JsonAsync();
            j.execute(String.valueOf(index));

        }
    }

    private class JsonAsync extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPostExecute(String result) {

            mPagerAdapter.notifyDataSetChanged();
            if (resume) {
                mPager.setCurrentItem(CURINDEX);
                resume = false;
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
            } else {
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

                DataBaseHelper helper = new DataBaseHelper(MainActivity.this, "cache.db");
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues values = new ContentValues();

                String json = HttpUtil.getJson(WeatherProvider.wrap(cities.get(index)));

                System.out.println("Update weather from: " + WeatherProvider.wrap(cities.get(index)));

                JSONObject jsonObject = new JSONObject(json);

                if (jsonObject.getInt("error") != 0) {
                    System.out.println("NetError");
                    Toast.makeText(MainActivity.this, "错误，更新失败", Toast.LENGTH_SHORT).show();
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
                    JSONObject data = (JSONObject) weatherData.opt(j - 1);
                    values.put("temp" + String.valueOf(j), data.getString("temperature"));
                    values.put("weather" + String.valueOf(j), data.getString("weather"));
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
        int cur = CURINDEX;

        if (Application.NEED_FRESH) {
            if (cur == Application.PAGE_NUM) {
                Application.PAGE_NUM++;
                fragmentList.add(cur, new WFragment(cur, instance, 1));
                fragmentList.removeLast();
                fragmentList.add(new WFragment(Application.PAGE_NUM, instance, 0));
            }
            System.out.println("Need Add : " + fragmentList.toString());

            getWeather(cur);
        }

        super.onResumeFragments();
    }


    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        System.out.println("Main start");
        super.onStart();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        System.out.println("Main pause");
        saveCities2Settings();
        super.onPause();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        System.out.println("Main resume");
        super.onResume();
    }


    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        System.out.println("Main stop");
        saveCities2Settings();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        if (net) {
            j.cancel(true);
        }
        System.out.println("Main destroy");
        super.onDestroy();
    }

    public class MyOnPageChangeListener implements OnPageChangeListener {

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
            CURINDEX = arg0;
        }

    }

    public void updateCities(int index, String city) {
        if (cities.size() == 0 || index == Application.PAGE_NUM) cities.add(city);
        else cities.set(index, city);
        System.out.println(cities.toString());
    }

    private void updateCitiesFromSettings() {
        cities = new ArrayList<String>();
        fragmentList = new LinkedList<WFragment>();
        //read setting data for create fragments

        String[] tmp = new String[Application.PAGE_NUM];
        Map<String, ?> cityMap = getSharedPreferences("CITIES_LIST", 0).getAll();

        for (Map.Entry<String, ?> entry : cityMap.entrySet()) {

            tmp[(Integer) entry.getValue()] = entry.getKey();

        }

        for (int i = 0; i < tmp.length; i++) {
            cities.add(tmp[i]);
            fragmentList.add(new WFragment(i, instance, 1));
            System.out.println("City added : " + tmp[i]);

        }

        fragmentList.add(new WFragment(Application.PAGE_NUM, instance, 0));
    }

    private void saveCities2Settings() {
        SharedPreferences settings = getSharedPreferences("SETTINGS", MODE_PRIVATE);
        Editor editor1 = settings.edit();

        editor1.putInt("page-num", Application.PAGE_NUM);
        editor1.putInt("cur-page", CURINDEX);
        editor1.commit();

        SharedPreferences cityList = getSharedPreferences("CITIES_LIST", MODE_PRIVATE);
        Editor editor2 = cityList.edit();
        editor2.clear().commit();
        for (int i = 0; i < cities.size(); i++) {
            editor2.putInt(cities.get(i), i);

        }

        editor2.commit();


    }

    public String getCity(int index) {
        return cities.get(index);
    }

}
