package com.akiyaBank;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/*
 * This Activity is used to fetch database values
 */
public class DataBaseHelper extends SQLiteOpenHelper{

	private static String DB_PATH = "/data/data/com.akiyaBank/databases/";    
	private static String DB_NAME = "akiyaDatabase.db";
	public SQLiteDatabase myDataBase; 
	private final Context myContext;

	/**
	 * Constructor
	 * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
	 * @param context
	 */
	public DataBaseHelper(Context context)
	{
		super(context, DB_NAME, null, 1);
		this.myContext = context;
	} 

	//************ Opening the database *************//
	public void openDataBase() throws SQLException
	{
		//Open the database     
		String myPath = DB_PATH + DB_NAME;       
		myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);     
	}

	//*********** Closing the database *********************//
	@Override
	public synchronized void close() 
	{

		if(this.myDataBase != null)
		{
			if(this.myDataBase.isOpen())
				this.myDataBase.close();
		}
		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
}