/**
 * 
 */
package edu.mills.cs180.safetravels;

import java.util.ArrayList;
import java.util.List;

import android.content.*;
import android.graphics.*;
import android.location.*;
import android.os.Bundle;
import android.view.*;
import android.view.View.OnClickListener;

import com.google.android.maps.*;

/**
 * MapPage is an activity that allows the user to view his/her current location on a map, track 
 * the path they have taken, and send SMS messages to specified phone numbers.
 * 
 * @author Kate Feeny
 * @author Jess Martin
 * @author TeAirra Ward
 * @author Jodessa Lanzadares
 * @author Dani E-F
 *
 */
public class MapPage extends MapActivity implements OnClickListener {
	
	/**
	 * MapView object for creating a map.
	 */
	private MapView mMap;
	
	/**
	 * MapController object for controlling map actions.
	 */
	private MapController mController;
	
	/**
	 * A long holding the minimum distance the user must travel to update current location.
	 */
	private static final long LOCATION_UPDATE_MIN_DISTANCE = 5;
	
	/**
	 * A long holding the number of milliseconds in between updating the current location.
	 */
	private static final long LOCATION_UPDATE_INTERVAL = 1000;
	
	/**
	 * A LocationManager for managing location changes.
	 */
	public static LocationManager sLocationManager;
	
	/**
	 * A GeoPoint referencing the current GeoPoint.
	 */
	private GeoPoint mCurrentGeoPoint;
	
	/**
	 * A MyLocationListener for handling location changes.
	 */
	static MyLocationListener sListener;
	
	/**
	 * A list of Overlays.
	 */
	private List<Overlay> mMapOverlays;
	
	/**
	 * A list of GeoPoints.
	 */
	private List<GeoPoint> mPoints;
	
	/**
	 * A PathOverlay object that can draw a path.
	 */
	private PathOverlay mPathOverlay;
	
	/**
	 * A MyLocationOverlay object for generating a flashing dot at user's current location.
	 */
	static MyLocationOverlay sOverlay;
	
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
	}//onCreate
	
	/** 
	 * Finds and initializes the MapView. 
	 */
	private void initMapView() {
		mMap = (MapView) findViewById(R.id.map);
		mController = mMap.getController();
		mController.setZoom(20);
		mMap.setSatellite(false);
		mMap.setBuiltInZoomControls(true);		
	}//initMapView
	
	/** 
	 * Starts tracking the user's current location on the map and draws the user's path. 
	 */
	private void initMyLocation() {
		sListener=new MyLocationListener();
		sLocationManager= (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		sLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
		        LOCATION_UPDATE_INTERVAL, LOCATION_UPDATE_MIN_DISTANCE,sListener);
		
		mMapOverlays = mMap.getOverlays(); 
		Location location = sLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		mPoints = new ArrayList<GeoPoint>();
		mPathOverlay =new PathOverlay(mPoints);
		
		//Test start
		sOverlay = new MyLocationOverlay(this,mMap);
		sOverlay.enableMyLocation();
		sOverlay.enableCompass();
		sOverlay.runOnFirstFix(new Runnable(){
		    public void run(){
		    mController.setZoom(20);
		    mController.animateTo(sOverlay.getMyLocation());
		    }//run 
		});
		
		mMap.getOverlays().add(sOverlay);
		showCurrentLocation(location);		
	}//initMyLocation
	
	/** 
	 * Adds a PathOverlay to the map and moves the map to show the current location.
	 * 
	 * @param location the current location of the user
	 */
	protected void showCurrentLocation(Location location){
		if(location!=null){
			mCurrentGeoPoint=new GeoPoint((int)(location.getLatitude()*1e6),
			        (int)(location.getLongitude()*1e6));
			mPoints.add(mCurrentGeoPoint);
			mController.animateTo(mCurrentGeoPoint);
			mMapOverlays.add(mPathOverlay);
		}//if
	}//showCurrentLocation
	
	/**
	 * MyLocationListener is a class that allows for the current location to be shown on the map.
	 * 
	 * @author Kate Feeny
	 * @author Jess Martin
	 * @author TeAirra Ward
	 * @author Jodessa Lanzadares
	 * @author Dani E-F
	 *
	 */
	private class MyLocationListener implements LocationListener{
		
		/**
		 * Calls showCurrentLocation with the user's current location.
		 * 
		 * @param location the current location of the user
		 */
		public void onLocationChanged(Location location){
			showCurrentLocation(location);
		}//onLocationChanged
		
		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
		}//onProviderDisabled
		
		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
		}//onProviderEnabled
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
		}//onStatusChanged
	}//MyLocationListener
	
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
	 * Called when a View is clicked. If the send to friend button is clicked, the FriendTextMessage
	 *  activity is started. If the made it button is clicked, the MadeItTextMessage activity is 
	 *  started. If the danger button is clicked, then the DangerTextMessage activity is started.
	 * 
	 * @param v the View that was clicked
	 */
	@Override
	public void onClick(View v){
		switch(v.getId()){
		//Send to a friend button
		case R.id.send_to_friend_button:
			startActivity(new Intent(this, FriendTextMessage.class));
			break;
			//made it button
		case R.id.made_it_button:
			startActivity(new Intent(this, MadeItTextMessage.class));
			finish();
			break;
			//danger button
		case R.id.danger_button:
			startActivity(new Intent(this, DangerTextMessage.class));
			break;

		}
	}
}
