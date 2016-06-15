package com.techpalle.poc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class CredDataBase {
	private Context con;
	private MyHelper mh;
	private SQLiteDatabase sdb;
	
	public CredDataBase(Context con){
		this.con = con;
		mh = new MyHelper(con, "techpalle.db", 
				null, 1);
	}
	public void open(){
		sdb = mh.getWritableDatabase();
	}
	public void close(){
		sdb.close();
	}
	public void insert(String android_id, 
			String build_serial_no, 
			String imei_meid_esn,
			String simserialnumber, 
			String phonenumber){
		ContentValues cv = new ContentValues();
		cv.put("android_id", android_id);
		cv.put("build_serial_no", build_serial_no);
		cv.put("imei_meid_esn", imei_meid_esn);
		cv.put("simserialnumber", simserialnumber);
		cv.put("phonenumber", phonenumber);
		sdb.insert("credentials", null, cv);
	}
	public Cursor getCredentials(){
		Cursor c = null;
		c = sdb.query("credentials", null, null, null, null, null, null);
		return c;
	}
	
	private class MyHelper extends SQLiteOpenHelper{

		public MyHelper(Context context, String name, CursorFactory factory,
				int version) {
			super(context, name, factory, version);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL("create table credentials("
					+ "_id integer primary key, "
					+ "android_id text, "
					+ "build_serial_no text, "
					+ "imei_meid_esn text, "
					+ "simserialnumber text, "
					+ "phonenumber text);");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
