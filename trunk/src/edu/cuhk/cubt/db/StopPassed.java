package edu.cuhk.cubt.db;

import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;

public class StopPassed {

	private static final String TAG = "StopPassedRecord";
	
    private final DatabaseOpenHelper mDatabaseOpenHelper;
    
    private static final String TABLE_NAME = "stoppassed";    

    private static final HashMap<String,String> mColumnMap = buildColumnMap();
    
    public static final String AUTHORITY = "edu.cuhk.cubt";
  

    public static final int ACTION_TYPE_UNKNOWN = 0;
    public static final int ACTION_TYPE_ENTER = 1;
    public static final int ACTION_TYPE_LEAVE = 2;
    public static final int ACTION_TYPE_PASSBYWALK = 3;
    public static final int ACTION_TYPE_PASSBYBUS = 4;
    
    public StopPassed(Context context){
    	mDatabaseOpenHelper = new DatabaseOpenHelper(context);
    }   
    
    private static HashMap<String,String> buildColumnMap(){
    	HashMap<String,String> map = new HashMap<String,String>();
    	map.put(StopPassedColumns._ID, StopPassedColumns._ID);
    	map.put(StopPassedColumns.ENTER_TIME, StopPassedColumns.ENTER_TIME);
    	map.put(StopPassedColumns.LEAVE_TIME, StopPassedColumns.LEAVE_TIME);
    	map.put(StopPassedColumns.STAY_PERIOD, StopPassedColumns.STAY_PERIOD);
    	map.put(StopPassedColumns.STOPS, StopPassedColumns.STOPS);
    	map.put(StopPassedColumns.ACTION_TYPE, StopPassedColumns.ACTION_TYPE);
		return map; 	
    }
    
    
    public long insert(long enter_time, long leave_time, String stop, int action_type){
    	ContentValues values = new ContentValues();
    	long stay_period = leave_time - enter_time;
    	values.put(StopPassedColumns.ENTER_TIME, enter_time);
    	values.put(StopPassedColumns.LEAVE_TIME, leave_time);
    	values.put(StopPassedColumns.STAY_PERIOD, stay_period);
    	values.put(StopPassedColumns.STOPS, stop);
    	values.put(StopPassedColumns.ACTION_TYPE, action_type);
    	
    	SQLiteDatabase db = mDatabaseOpenHelper.getWritableDatabase();
    	long rowId = db.insert(TABLE_NAME, StopPassedColumns.ENTER_TIME, values);
    	
    	return rowId;    	
    }  
    
    /**
     * 
     * @param from, the start time of history, or -1 
     * @param to, the end time of history, or -1 
     * @return
     */
    public Cursor getStopPassed(String[] projection, long from, long to){
    	String selection = "";
    	if(from> -1){
    		selection += StopPassedColumns.LEAVE_TIME + ">= " + Long.toString(from);
    	}
    	if(to> -1){
    		selection += (TextUtils.isEmpty(selection) ? " AND " : "") + StopPassedColumns.ENTER_TIME + "<= " + Long.toString(to);
    	}
    	Log.d(TAG,selection);
    	return query(projection,selection,null);
    }
    
    private Cursor query(String[] projection, String selection,String[] selectionArgs){
    	SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
    	qb.setTables(TABLE_NAME);
    	qb.setProjectionMap(mColumnMap);

        Cursor cursor = qb.query(mDatabaseOpenHelper.getReadableDatabase(),
                projection, selection, selectionArgs, null, null, StopPassedColumns.DEFAULT_SORT_ORDER);

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }
    
    
    private static final String TABLE_CREATE_STRING = 
    	"CREATE TABLE " + TABLE_NAME + " ("
    	+ StopPassedColumns._ID + "INTEGER PRIMARY KEY,"
    	+ StopPassedColumns.ENTER_TIME + " INTEGER,"
    	+ StopPassedColumns.LEAVE_TIME + " INTEGER,"
    	+ StopPassedColumns.STAY_PERIOD + " INTEGER,"
    	+ StopPassedColumns.STOPS + " TEXT,"
    	+ StopPassedColumns.ACTION_TYPE + " INTEGER"
    	+ ");";  
    
    public static final void createTable(SQLiteDatabase db){
		db.execSQL(TABLE_CREATE_STRING);
    }
    
    public static final class StopPassedColumns implements BaseColumns {
    	private StopPassedColumns(){};
    	
    	
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/stoppassed");

        /**
         * The MIME type of {@link #CONTENT_URI} providing a directory of notes.
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.cubt.stoppassed";

        /**
         * The MIME type of a {@link #CONTENT_URI} sub-directory of a single note.
         */
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.cubt.stoppassed";

    	
        
        public static final String ENTER_TIME = "entertime";
        public static final String LEAVE_TIME = "leavetime";
        public static final String STAY_PERIOD = "period";
        public static final String ACTION_TYPE = "action";
        public static final String STOPS = "stop";
        

        /**
         * The default sort order for this table
         */
        public static final String DEFAULT_SORT_ORDER = ENTER_TIME + " DESC";
        
    	
    }
    

}
