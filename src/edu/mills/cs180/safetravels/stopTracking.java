package edu.mills.cs180.safetravels;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class stopTracking extends Activity implements OnClickListener{
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stoptracking);

		View StopTrackYesButton = findViewById(R.id.stop_track_yes_button);
		StopTrackYesButton.setOnClickListener(this);
		View StopTrackNoButton = findViewById(R.id.stop_track_no_button);
		StopTrackNoButton.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		//Yes
		case R.id.stop_track_yes_button:
			MapPage.locationManager.removeUpdates(MapPage.listener);
			finish();
			break;
			//made it button
		case R.id.stop_track_no_button:
			finish();
			break;
		}

	}
}
