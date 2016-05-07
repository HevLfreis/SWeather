package com.hayt.sweather;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper{

	public DataBaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	public DataBaseHelper(Context context, String name) {
		this(context, name, 1);
		// TODO Auto-generated constructor stub
	}
	public DataBaseHelper(Context context, String name,
			int version) {
		this(context, name, null, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("create TABLE cache(timestamp long,city varchar(50),temp1 varchar(50),temp2 varchar(50)," +
				"temp3 varchar(50),temp4 varchar(50),weather1 varchar(50)" +
				",weather2 varchar(50),weather3 varchar(50),weather4 varchar(50),index_d varchar(100))");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
