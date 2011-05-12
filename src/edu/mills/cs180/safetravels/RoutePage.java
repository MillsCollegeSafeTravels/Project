package edu.mills.cs180.safetravels;

import java.util.List;

import android.content.Intent;
import android.graphics.Path.Direction;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;


/**
 * RoutePage is an activity that allows the user to generate a route from one location to another.
 * 
 * @author Kate Feeny
 * @author Jess Martin
 * @author TeAirra Ward
 * @author Jodessa Lanzadares
 * @author Dani E-F
 *
 */

public class RoutePage extends MapActivity implements OnClickListener{

	/**
	 * MapView object for creating a map.
	 */
	private MapView mMapRoute;

	/**
	 * MapController object for controlling map actions.
	 */
	private MapController mControllerRoute;

	/**
	 * MyLocationOverlay object for drawing the current location.
	 */
	private MyLocationOverlay mOverlayRoute;

	/**
	 * Called when the activity is starting. Initializes the map and location and sets the 
	 * onClickListeners for the button Views to this.
	 * 
	 * @param savedInstanceState if the activity is being re-initialized after previously being 
	 * shut down then this Bundle contains the data it most recently supplied in 
	 * onSaveInstanceState(Bundle). Note: Otherwise it is null.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//set view to mapview
		setContentView(R.layout.routepage);
		
		//init background map, get sat and zoom 
		initMapView();
		//init location, turn on tracker and compass
		initMyLocation();

		 //set up click listeners
        View TrackThisRouteButton = findViewById(R.id.track_this_route_button);
        TrackThisRouteButton.setOnClickListener(this);
	}

	/** 
	 * Finds and initializes the MapView.
	 */
	private void initMapView() {

		mMapRoute = (MapView) findViewById(R.id.mapRoute);
		mControllerRoute = mMapRoute.getController();
		mMapRoute.setSatellite(false);
		mMapRoute.setBuiltInZoomControls(true);
	}
	
	/** 
	 * Starts tracking the user's current location on the map. 
	 */
	private void initMyLocation() {
		mOverlayRoute = new MyLocationOverlay(this, mMapRoute);
		mOverlayRoute.enableMyLocation();
		mOverlayRoute.enableCompass(); // does not work in emulator
		mOverlayRoute.runOnFirstFix(new Runnable() {
			@Override
			public void run() {
				// Zoom in to current location

				mControllerRoute.setZoom(16);
				mControllerRoute.animateTo(mOverlayRoute.getMyLocation());
			}
		});
		mMapRoute.getOverlays().add(mOverlayRoute);
	}

	/**
	 * Required by MapActivity but is not used.
	 * @return always false
	 */
	@Override
	protected boolean isRouteDisplayed() {
		// Required by MapActivity
		return false;
	}

	/**
	 * Called when a View is clicked. If the track route button is clicked, the MapPage activity is 
	 * started. If the find route button is clicked, it attempts to retrieve the latitude and 
	 * longitude of the entered address.
	 * 
	 * @param v the View that was clicked
	 */
	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.track_this_route_button:
			startActivity(new Intent(this, MapPage.class));
			break;
		}
	}
	
}



