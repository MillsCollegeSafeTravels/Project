package edu.mills.cs180.safetravels;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Preferences extends PreferenceActivity {
    private boolean CheckboxPreference;
    private String customPref;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
    
    private void getPrefs() {
            // Get the xml/preferences.xml preferences
            SharedPreferences prefs = PreferenceManager
                            .getDefaultSharedPreferences(getBaseContext());
            CheckboxPreference = prefs.getBoolean("checkboxPref", true);
            // Get the custom preference
            SharedPreferences mySharedPreferences = getSharedPreferences(
                            "myCustomSharedPrefs", Activity.MODE_PRIVATE);
            customPref = mySharedPreferences.getString("myCusomPref", "");
    }
}