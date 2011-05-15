package edu.cuhk.cubt.db;

import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.location.Location;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;
import edu.cuhk.cubt.db.DbStopPassed.StopPassedColumns;

public class DbTravelLocation {


	private static final String TAG = "DbTravelLocation";
	
    private final DatabaseOpenHelper mDatabaseOpenHelper;
    
    public static final String TABLE_NAME = "travellocation";    

    private static final HashMap<String,String> mColumnMap = buildColumnMap();
    
    public static final String AUTHORITY = "edu.cuhk.cubt";
     
    private static DbTravelLocation db;
    
    protected DbTravelLocation(Context context){
    	mDatabaseOpenHelper = new DatabaseOpenHelper(context);
    }   
    
    public static DbTravelLocation getInstance(Context context){
    	if(db == null){
    		db  = new DbTravelLocation(context.getApplicationContext()); 
    	}
    	return db;
    }
    
    
    private static HashMap<String,String> buildColumnMap(){
    	HashMap<String,String> map = new HashMap<String,String>();
    	map.put(TravelLocationColumns._ID, TravelLocationColumns._ID);
    	map.put(TravelLocationColumns.TIME , TravelLocationColumns.TIME );
    	map.put(TravelLocationColumns.LATITUDE , TravelLocationColumns.LATITUDE );
    	map.put(TravelLocationColumns.LONGITUDE , TravelLocationColumns.LONGITUDE );
		return map; 	
    }
    
    public int getTid(){
    	int ret = 0;
    	Cursor cursor = mDatabaseOpenHelper.getReadableDatabase().rawQuery("SELECT MAX(" + TravelLocationColumns.TID + ") FROM " + TABLE_NAME +";",null);

		if(cursor != null){
			cursor.moveToFirst();
			ret = cursor.getInt(0) + 1;
		}
		return ret;
    }
    
    public long insert(int tid, Location location){
    	ContentValues values = new ContentValues();
    	values.put(TravelLocationColumns.TID, tid);
    	values.put(TravelLocationColumns.TIME, location.getTime());
    	values.put(TravelLocationColumns.LONGITUDE, location.getLongitude());
    	values.put(TravelLocationColumns.LATITUDE, location.getLatitude());
    	
    	SQLiteDatabase db = mDatabaseOpenHelper.getWritableDatabase();
    	long rowId = db.insert(TABLE_NAME, StopPassedColumns.ENTER_TIME, values);
    	db.close();
    	return rowId;    	
    }
    
    
    public Cursor getTravel(){
    	String[] projection;
    	String sql = "SELECT " + TravelLocationColumns.TID + " " + TravelLocationColumns._ID + 
    		" , max(" + TravelLocationColumns.TIME +") " + TravelLocationColumns.LATITUDE +
    		", min(" + TravelLocationColumns.TIME +") "  + TravelLocationColumns.LONGITUDE + 
    		" FROM " + TABLE_NAME + " GROUP BY " + TravelLocationColumns.TID +";";
    	Log.i(TAG, sql);
    	return mDatabaseOpenHelper.getWritableDatabase().rawQuery(sql, null);
    }
    
    public Cursor getLocations(String[] projection, long tid){
    	String selection = "";    	
    	selection += TravelLocationColumns.TID + "=" + tid;
    	return query(projection,selection,null);
    }
    
    private Cursor query(String[] projection, String selection,String[] selectionArgs){
    	SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
    	qb.setTables(TABLE_NAME);
    	qb.setProjectionMap(mColumnMap);

    	SQLiteDatabase db = mDatabaseOpenHelper.getWritableDatabase();
        Cursor cursor = qb.query(db,
                projection, selection, selectionArgs, null, null, TravelLocationColumns.DEFAULT_SORT_ORDER);

        if (cursor == null) {
            db.close();
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            db.close();
            return null;
        }
        return cursor;
    }
    
    
    private static final String TABLE_CREATE_STRING = 
    	"CREATE TABLE " + TABLE_NAME + " ("
    	+ TravelLocationColumns._ID + " INTEGER PRIMARY KEY,"
    	+ TravelLocationColumns.TID + " INTEGER,"
    	+ TravelLocationColumns.TIME + " INTEGER,"
    	+ TravelLocationColumns.LATITUDE + " FLOAT,"
    	+ TravelLocationColumns.LONGITUDE + " FLOAT"
    	+ ");";  
    
    protected static final void createTable(SQLiteDatabase db){
		db.execSQL(TABLE_CREATE_STRING);
		db.execSQL("CREATE INDEX " + TABLE_NAME + "_idx ON " + TABLE_NAME + "(" + TravelLocationColumns.TID +"," + TravelLocationColumns.TIME + ");");
    }
        
    protected static final void deleteTable(SQLiteDatabase db){
    	String TABLE_DELETE_STRING = "DROP TABLE " + TABLE_NAME + ";";
    	db.execSQL(TABLE_DELETE_STRING);
    	db.execSQL("DROP INDEX "+ TABLE_NAME + "_idx;");
    }
    
    private final void resetTable(){
    	SQLiteDatabase db = mDatabaseOpenHelper.getWritableDatabase();
    	deleteTable(db);
    	createTable(db);
    }
    
    public static final class TravelLocationColumns implements BaseColumns {
    	private TravelLocationColumns(){};
    	
    	
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/stoppassed");

        /**
         * The MIME type of {@link #CONTENT_URI} providing a directory of notes.
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.cubt.travellocation";

        /**
         * The MIME type of a {@link #CONTENT_URI} sub-directory of a single note.
         */
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.cubt.travellocation";


        public static final String TID = "tid";
        public static final String TIME = "time";
        public static final String LONGITUDE = "longitude";
        public static final String LATITUDE = "latitude";
        

        /**
         * The default sort order for this table
         */
        public static final String DEFAULT_SORT_ORDER = TIME + " ASC";
    }
    	
}
