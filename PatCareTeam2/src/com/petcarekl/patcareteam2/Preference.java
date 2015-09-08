package com.petcarekl.patcareteam2;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import com.petcare.teamiki.R;


public class Preference extends PreferenceActivity{

	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	 
	        addPreferencesFromResource(R.xml.settings);
	 
	    }
}
