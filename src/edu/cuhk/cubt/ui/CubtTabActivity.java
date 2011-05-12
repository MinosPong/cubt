package edu.cuhk.cubt.ui;

import edu.cuhk.cubt.R;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

public class CubtTabActivity extends TabActivity {

	static final int MENU_OPTION = Menu.FIRST + 1001;
	static final int MENU_EXIT = Menu.FIRST + 1002;	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tapview);
		Resources res = getResources(); // Resource object to get Drawables
	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab


	    intent = new Intent().setClass(this, CubtMapView.class);
	    spec = tabHost.newTabSpec("map").setIndicator("Map")
	                  .setContent(intent);
	    tabHost.addTab(spec);
	    
	    // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, StopPassedHistoryActivity.class);

	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("stophistory").setIndicator("Stop History")
	                  .setContent(intent);
	    tabHost.addTab(spec);


	    intent = new Intent().setClass(this, TestUserStateActivity.class);
	    spec = tabHost.newTabSpec("userstate").setIndicator("User State")
	                  .setContent(intent);
	    tabHost.addTab(spec);	    
	    
	    tabHost.setCurrentTab(0);
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, MENU_OPTION, MENU_OPTION, R.string.menu_settings).setIcon(android.R.drawable.ic_menu_edit);
		menu.add(0, MENU_EXIT, MENU_EXIT, R.string.menu_exit).setIcon(android.R.drawable.ic_menu_close_clear_cancel);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
			case MENU_OPTION:
				Intent intent = new Intent(this, Settings.class);
				startActivity(intent);
				return true;
			case MENU_EXIT:
				finish();
				return true;
		}
		
		return super.onOptionsItemSelected(item);
	}

	
}
