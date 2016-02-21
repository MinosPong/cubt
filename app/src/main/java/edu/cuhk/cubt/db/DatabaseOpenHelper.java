package edu.cuhk.cubt.db;

import edu.cuhk.cubt.db.DbStopPassed.StopPassedColumns;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseOpenHelper extends SQLiteOpenHelper{

	private static final String TAG = "DatabaseHelper";
	
	public static final String DATABASE_NAME = "CUBT_DATABASE";
	
	public static final int DATABASE_VERSION = 6;
	
	
	public DatabaseOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		DbStopPassed.createTable(db);
    	DbTravelLocation.createTable(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        if(oldVersion<4){
        	db.execSQL("ALTER TABLE " + DbStopPassed.TABLE_NAME + " ADD " + StopPassedColumns.TRAVEL_ID + " integer ");
        }
        if(oldVersion<6){
        	DbTravelLocation.deleteTable(db);
        	DbTravelLocation.createTable(db);
        }
	}

}
