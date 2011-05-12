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
 * @author KateFeeney
 *
 */
public class MapPage extends MapActivity implements OnClickListener {
	private MapView mMap;
	private MapController mController;
	private static final long LOCATION_UPDATE_MIN_DISTANCE = 5;
	private static final long LOCATION_UPDATE_INTERVAL = 1000;
	public static LocationManager sLocationManager;
	protected GeoPoint mCurrentGeoPoint;
	static MyLocationListener sListener;
	private List<Overlay> mMapOverlays;
	private List<GeoPoint> mPoints;
	private PathOverlay mPathOverlay;
	//overlay for the flashing dot at the user's current location
	static MyLocationOverlay sOverlay;
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
		mMap = (MapView) findViewById(R.id.map);
		mController = mMap.getController();
		mController.setZoom(20);
		mMap.setSatellite(false);
		mMap.setBuiltInZoomControls(true);		
	}
	/** Start tracking the position on the map. */
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
		    } 
		});
		mMap.getOverlays().add(sOverlay);
		showCurrentLocation(location);		
	}
	/** adds overlay path to map, moves to current location. */
	protected void showCurrentLocation(Location location){
		if(location!=null){
			mCurrentGeoPoint=new GeoPoint((int)(location.getLatitude()*1e6),
			        (int)(location.getLongitude()*1e6));
			mPoints.add(mCurrentGeoPoint);
			mController.animateTo(mCurrentGeoPoint);
			mMapOverlays.add(mPathOverlay);
		}
	}
	private class MyLocationListener implements LocationListener{
		public void onLocationChanged(Location location){
			showCurrentLocation(location);
		}
		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
		}
		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
		}
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
		}
	}
	@Override
	protected boolean isRouteDisplayed() {
		// Required by MapActivity
		return false;
	}    
	//OnClick
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
	
	// allow menu to pop up
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    // brings up page when selected on menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.about_menuitem:
            startActivity(new Intent(this, About.class));
            break;
        case R.id.planroute_menuitem:
            startActivity(new Intent(this, RoutePage.class));
            break;
        }
        return true;
    }
    @Override
    public void onBackPressed(){
    	startActivity(new Intent(this, TrackingDisabler.class));
    }
}
