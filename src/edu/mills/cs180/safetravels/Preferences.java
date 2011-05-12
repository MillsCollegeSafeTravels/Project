package edu.mills.cs180.safetravels;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Preferences is an activity class that allows the user to view the settings that can be altered 
 * in the app.
 * 
 * @author Kate Feeny
 * @author Jess Martin
 * @author TeAirra Ward
 * @author Jodessa Lanzadares
 * @author Dani E-F
 *
 */
public class Preferences extends PreferenceActivity {
	
	/**
	 * Called when the activity is starting. Adds the items that can be set to the preferences.
	 * 
	 * @param savedInstanceState if the activity is being re-initialized after previously being 
	 * shut down then this Bundle contains the data it most recently supplied in 
	 * onSaveInstanceState(Bundle). Note: Otherwise it is null.
	 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }//onCreate
    
}//Preferences