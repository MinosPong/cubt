package edu.cuhk.cubt.ui;

import edu.cuhk.ie.cubt.R;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Settings extends PreferenceActivity {

	private static SharedPreferences settings;
	
	private static String sharedPreferenceFile = "edu.cuhk.cubt.cubt_pref";
	
	public static String PREF_VIRTUAL = "virtual";

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		settings = getSharedPreferences(sharedPreferenceFile, MODE_PRIVATE);
	}
	
	
	
	
}
