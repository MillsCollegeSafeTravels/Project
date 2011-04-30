package edu.mills.cs180.safetravels;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.android.maps.GeoPoint;
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
	private MyMyLocationOverlay overlay;
	private Location pastLocation;

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
		overlay = new MyMyLocationOverlay(this, map);
		overlay.enableMyLocation();
	//	overlay.enableCompass(); // does not work in emulator
		overlay.runOnFirstFix(new Runnable() {
			@Override
			public void run() {
				// Zoom in to current location
				controller.setZoom(16);
				//get location
				overlay.getLastFix();
				GeoPoint geoPoint = overlay.getMyLocation();
				//set as first last location
				//convert to location
				float latitude=geoPoint.getLatitudeE6()/1000000;
				float longitude=geoPoint.getLongitudeE6()/1000000;
				pastLocation.setLatitude(latitude);
				pastLocation.setLongitude(longitude);
				controller.animateTo(geoPoint);		
			}
		});
		map.getOverlays().add(overlay);
	}

	@Override
	protected boolean isRouteDisplayed() {
		// Required by MapActivity
		return false;
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.send_to_friend_button:
			startActivity(new Intent(this, TestPage.class));
			break;
		case R.id.made_it_button:
			overlay.disableMyLocation();
			overlay.disableCompass();
			startActivity(new Intent(this, SendTextMessage.class));
			break;
		case R.id.danger_button:
			startActivity(new Intent(this, SendTextMessageDanger.class));
			break;
		}
	}
}