package edu.mills.cs180.safetravels;

import java.util.List;

import android.content.Intent;
import android.graphics.Path.Direction;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
	private MapView mMap;

	/**
	 * MapController object for controlling map actions.
	 */
	private MapController mController;

	/**
	 * MyLocationOverlay object for drawing the current location.
	 */
	private MyLocationOverlay mOverlay;

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
		View FindRouteButton = findViewById(R.id.find_route_button);
		FindRouteButton.setOnClickListener(this);
	}//onCreate

	/** 
	 * Finds and initializes the MapView.
	 */
	private void initMapView() {
		mMap = (MapView) findViewById(R.id.map);
		mController = mMap.getController();
		mMap.setSatellite(false);
		mMap.setBuiltInZoomControls(true);
	}//initMapView

	/** 
	 * Starts tracking the user's current location on the map. 
	 */
	private void initMyLocation() {
		mOverlay = new MyLocationOverlay(this, mMap);
		mOverlay.enableMyLocation();
		mOverlay.enableCompass(); // does not work in emulator
		mOverlay.runOnFirstFix(new Runnable() {
			@Override
			public void run() {
				// Zoom in to current location
				mController.setZoom(16);
				mController.animateTo(mOverlay.getMyLocation());
			}//run
		});
		mMap.getOverlays().add(mOverlay);
	}//initMyLocation

	/**
	 * Required by MapActivity but is not used.
	 * @return always false
	 */
	@Override
	protected boolean isRouteDisplayed() {
		// Required by MapActivity
		return false;
	}//isRouteDisplayed

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
		case R.id.find_route_button:
			Geocoder gc = new Geocoder(this);
			EditText address = (EditText)findViewById(R.id.destination_address);
			String addressInput = address.getText().toString(); //Get input text
			double lat;
			double lon;
			try {
				List<Address> foundAdresses = gc.getFromLocationName(addressInput, 5); //Search addresses
				for (int i = 0; i < foundAdresses.size(); ++i) {
					//Save results as Longitude and Latitude
					//@todo: if more than one result, then show a select-list
					Address x = foundAdresses.get(i);
					lat = x.getLatitude();
					lon = x.getLongitude();
				}//for
			}//try

			catch (Exception e) {
				//@todo: Show error message
			}//catch

			break;
		}//switch
	}//onClick

	/**
	 * Allows the menu to appear when the device's menu button is pressed.
	 * 
	 * @param menu the Menu to appear
	 * @return true if no errors occur
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}//onCreateOptionsMenu


	/**
	 * Starts an activity when a menu item is selected. The activity that is started is determined 
	 * by which menu item was selected.
	 * 
	 * @param item the MenuItem that was selected
	 * @return true if the activity starts with no errors
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.about_menuitem:
			startActivity(new Intent(this, About.class));
		case R.id.settings_menuitem:
			startActivity(new Intent(this, Preferences.class));
		}//switch
		return true;
	}//onOptionsItemSelected

}//RoutePage


