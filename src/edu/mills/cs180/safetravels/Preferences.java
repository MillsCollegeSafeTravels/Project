package edu.mills.cs180.safetravels;
import android.os.Bundle;
import android.preference.PreferenceActivity;
public class Preferences extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.settings);
	}
}