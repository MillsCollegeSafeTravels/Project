package edu.mills.cs180.safetravels;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Preferences extends PreferenceActivity {
    private boolean mCheckboxPreference;
    private String mCustomPref;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
   //gets the preferences but doesn't do anything with them
    // until we actually have Danger button options, this is superfluous :[
    private void getPrefs() {
            // Get the xml/preferences.xml preferences
            SharedPreferences prefs = PreferenceManager
                            .getDefaultSharedPreferences(getBaseContext());
            mCheckboxPreference = prefs.getBoolean("checkboxPref", true);
            // Get the custom preference
            SharedPreferences mySharedPreferences = getSharedPreferences(
                            "myCustomSharedPrefs", Activity.MODE_PRIVATE);
            mCustomPref = mySharedPreferences.getString("myCusomPref", "");
    }
}