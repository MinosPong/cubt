package edu.cuhk.cubt.ui;

import edu.cuhk.cubt.R;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Settings extends PreferenceActivity {

	private static SharedPreferences settings;
	
	public static String sharedPreferenceFile = "edu.cuhk.cubt.cubt_pref";
	
	public static String PREF_VIRTUAL_SENSOR = "key_virtual_sensor";

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getPreferenceManager().setSharedPreferencesName(sharedPreferenceFile);
		settings = getSharedPreferences(sharedPreferenceFile, MODE_PRIVATE);
		addPreferencesFromResource(R.xml.preferences);
	}
	
	
	
	
}
