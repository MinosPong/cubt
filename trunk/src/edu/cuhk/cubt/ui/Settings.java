package edu.cuhk.cubt.ui;

import edu.cuhk.cubt.R;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Settings extends PreferenceActivity {

	private static SharedPreferences settings;
	
	public static String sharedPreferenceFile = "edu.cuhk.cubt.cubt_pref";
	
	public static String PREF_VIRTUAL_SENSOR = "key_virtual_sensor";
	public static String PREF_VIRTUAL_FILE = "key_virtual_file";
	public static String PREF_MAPVIEW_REFRESH_PERIOD = "key_refresh_preiod";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getPreferenceManager().setSharedPreferencesName(sharedPreferenceFile);
		settings = getSharedPreferences(sharedPreferenceFile, MODE_PRIVATE);
		addPreferencesFromResource(R.xml.preferences);
	}
	
}
