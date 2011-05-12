package edu.cuhk.cubt.ui;

import edu.cuhk.cubt.R;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class CubtTabActivity extends TabActivity {

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
}
