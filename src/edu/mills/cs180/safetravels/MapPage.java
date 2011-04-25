/**
 * 
 */
package edu.mills.cs180.safetravels;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

/**
 * @author KateFeeney
 *
 */
public class MapPage extends MapActivity implements OnClickListener {
	private MapView map;
	private MapController controller;
	private MyOverlay overlay;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//set view to mapview
		setContentView(R.layout.mapview);
		//init background map, get sat and zoom 
		initMapView();
		//init location, turn on tracker and compass
		initMyLocation();
		 //set up click listeners
        View SendToFriendButton = findViewById(R.id.send_to_friend_button);
        SendToFriendButton.setOnClickListener(this);
        View DangerButton = findViewById(R.id.danger_button);
        DangerButton.setOnClickListener(this);
        View MadeItButton = findViewById(R.id.made_it_button);
        MadeItButton.setOnClickListener(this);
	}

	/** Find and initialize the map view. */
	private void initMapView() {
		map = (MapView) findViewById(R.id.map);
		controller = map.getController();
		map.setSatellite(false);
		map.setBuiltInZoomControls(true);
	}

	/** Start tracking the position on the map. */
	private void initMyLocation() {
		overlay = new MyOverlay(this, map);
		overlay.enableMyLocation();
		overlay.enableCompass(); // does not work in emulator
		overlay.runOnFirstFix(new Runnable() {
			@Override
			public void run() {
				// Zoom in to current location
				controller.setZoom(16);
				//get location
				controller.animateTo(overlay.getMyLocation());		
			}
		});
		map.getOverlays().add(overlay);
	}

	@Override
	protected boolean isRouteDisplayed() {
		// Required by MapActivity
		return false;
	}
	//Listeners 
	
	//OnClick
	@Override
	public void onClick(View v){
		switch(v.getId()){
		//Send to a friend button
		case R.id.send_to_friend_button:
			startActivity(new Intent(this, SendTextMessage.class));
			break;
		//made it button
		case R.id.made_it_button:
			overlay.disableMyLocation();
			overlay.disableCompass();
			startActivity(new Intent(this, SendMadeItTextMessage.class));
			finish();
			break;
		//danger button
		case R.id.danger_button:
			startActivity(new Intent(this, SendTextMessageDanger.class));
			break;
		}
	}

}
