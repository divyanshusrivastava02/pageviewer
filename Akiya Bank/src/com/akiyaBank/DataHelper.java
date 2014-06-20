package com.akiyaBank;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.os.Environment;
import android.util.Log;

/*
 * This Activity is used to store the property information in database
 */
public class DataHelper {


	public static final String DATABASE_NAME = "akiyaDatabase.db";
	public static final int DATABASE_VERSION = 1;
	public static final String TABLE_NAME = "akiyaDatabaseTable";	
	public static final String id = "id";
	DataHelper objDataHelper;
	public Context context;
	public SQLiteDatabase db;

	public SQLiteStatement insertStmt;
	public static final String INSERT = "INSERT INTO "
		+ TABLE_NAME + "(propertyTitle,propertyImage,propertyPlace,propertyId) values (?,?,?,?)";

	public DataHelper(Context context) {
		this.context = context;
		OpenHelper openHelper = new OpenHelper(this.context);
		this.db = openHelper.getWritableDatabase();		
		this.insertStmt = this.db.compileStatement(INSERT);
	}

	public long insert(String propertyTitle,String propertyImage, String propertyPlace, String propertyId) {
		
		
		this.insertStmt.bindString(1, propertyTitle);
		this.insertStmt.bindString(2, propertyImage);
		this.insertStmt.bindString(3, propertyPlace);
		this.insertStmt.bindString(4, propertyId);		
		return this.insertStmt.executeInsert();
	}

	public void deleteAll() {
		this.db.delete(TABLE_NAME, null, null);
	}
	public boolean deleteNote(int rowId) {		
		return db.delete(TABLE_NAME, id + "=" + rowId, null) > 0;

	}

	public ArrayList<Akiya> listSelectAll() {

		ArrayList<Akiya> list = new ArrayList<Akiya>();
		Cursor cursor = this.db.query(TABLE_NAME, new String[] {"id","propertyTitle","propertyImage","propertyPlace","propertyId"}, 
				null, null, null, null, null);		
		if (cursor.moveToFirst())
		{
			do
			{	
				
				Akiya akiyaObj = new Akiya();
				akiyaObj.id=cursor.getString(0)+"";
				akiyaObj.propertyTitle = cursor.getString(1);
				akiyaObj.propertyImage = cursor.getString(2);
				akiyaObj.propertyPlace = cursor.getString(3);
				akiyaObj.propertyId = cursor.getString(4);
				list.add(akiyaObj);  

			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) 
		{
			cursor.close();
		}
		return list;
	}

	public static class OpenHelper extends SQLiteOpenHelper 
	{

		OpenHelper(Context context) {
		
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "+" propertyTitle TEXT,"+" propertyImage TEXT,"+"propertyPlace TEXT,"+"propertyId TEXT UNIQUE"+");");

		}


		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w("Example", "Upgrading database, this will drop tables and recreate.");
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			onCreate(db);
		}
	}

	/**
	 * Helper Method to Test if external Storage is read only
	 */
	public boolean isExternalStorageReadOnly() {
		boolean state = false;
		String extStorageState = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
			state = true;
		}
		return state;
	}


}
