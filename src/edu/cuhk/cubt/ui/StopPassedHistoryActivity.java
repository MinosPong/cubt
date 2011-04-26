package edu.cuhk.cubt.ui;

import edu.cuhk.cubt.R;
import edu.cuhk.cubt.db.DbStopPassed;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class StopPassedHistoryActivity extends Activity {
	private Spinner stopSpinner;
	private ListView historyListView;
	
	private String TAG = "StopPassedHistoryActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.stoppassed);
		findViews();
		
		DbStopPassed db = new DbStopPassed(this);
		Cursor cursor = db.getStopPassed(
				new String[] {	
						DbStopPassed.StopPassedColumns._ID,
						DbStopPassed.StopPassedColumns.STOPS,
						DbStopPassed.StopPassedColumns.ENTER_TIME,
						DbStopPassed.StopPassedColumns.LEAVE_TIME
					}
				, -1, -1);
		
		if(cursor != null){
			startManagingCursor(cursor);
			SimpleCursorAdapter adapter = new SimpleCursorAdapter (this, R.layout.stoppassed_list_item, cursor, 
					new String[] {	DbStopPassed.StopPassedColumns.STOPS,
									DbStopPassed.StopPassedColumns.ENTER_TIME,
									DbStopPassed.StopPassedColumns.LEAVE_TIME
								}, 
					new int[] {R.id.stop, R.id.enter_time, R.id.leave_time});
			adapter.setViewBinder(stopViewBinder);
			historyListView.setAdapter(adapter);
		}
	}
	
	private void findViews(){
		historyListView = (ListView)findViewById(R.id.stoppassedlist);
	}	
	
	private SimpleCursorAdapter.ViewBinder stopViewBinder = new SimpleCursorAdapter.ViewBinder(){

		@Override
		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
			if(columnIndex >= 2  ){
				Time t = new Time();
				t.set(cursor.getLong(columnIndex));	
				((TextView)view).setText(t.format("%Y/%m/%d %H:%M:%S"));
				return true;
			}
			return false;
		}
		
	};
}
