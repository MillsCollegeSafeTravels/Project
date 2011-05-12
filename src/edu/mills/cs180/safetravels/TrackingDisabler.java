package edu.mills.cs180.safetravels;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * TrackingDisabler is an activity class that allows the user to stop tracking or allow it to 
 * continue.
 * 
 * @author Kate Feeny
 * @author Jess Martin
 * @author TeAirra Ward
 * @author Jodessa Lanzadares
 * @author Dani E-F
 *
 */
public class TrackingDisabler extends Activity implements OnClickListener{
    
	/**
	 * Called when the activity is starting. Sets the onClickListeners for the button Views to this.
	 * 
	 * @param savedInstanceState if the activity is being re-initialized after previously being 
	 * shut down then this Bundle contains the data it most recently supplied in 
	 * onSaveInstanceState(Bundle). Note: Otherwise it is null.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stoptracking);
		View StopTrackYesButton = findViewById(R.id.stop_track_yes_button);
		StopTrackYesButton.setOnClickListener(this);
		View StopTrackNoButton = findViewById(R.id.stop_track_no_button);
		StopTrackNoButton.setOnClickListener(this);
	}
	
	/**
	 * Called when a View is clicked. If the stop tracking button was clicked, the MapPage activity 
	 * stops tracking the user's location. Otherwise, the activity finishes.
	 * 
	 * @param v the View that was clicked
	 */
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		//Yes
		case R.id.stop_track_yes_button:
			MapPage.sLocationManager.removeUpdates(MapPage.sListener);
		    MapPage.sOverlay.disableMyLocation();  
		    MapPage.sOverlay.disableCompass();
			finish();
			break;
			//made it button
		case R.id.stop_track_no_button:
			finish();
			break;
		}//switch
	}//onClick
	
}//TrackingDisabler
