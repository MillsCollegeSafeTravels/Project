/**
 * 
 */
package edu.mills.cs180.safetravels;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.media.SoundPool.OnLoadCompleteListener;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;


/**
 * @author KateFeeney
 *
 */
public class MapPage extends MapActivity implements OnClickListener {
	private MapView map;
	private MapController controller;
	
	private static final long LOCATION_UPDATE_MIN_DISTANCE = 5;
	private static final long LOCATION_UPDATE_INTERVAL = 1000;
	public static LocationManager locationManager;
	private GeoPoint currentGeoPoint;
	static MyLocationListener listener;
	
	List<Overlay> mapOverlays;
	List<GeoPoint> points;
	private PathOverlay pathOverlay;
	Canvas canvas;
	
	Paint paint = new Paint();
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
	   
        //define paint
        paint.setDither(true);
        paint.setColor(Color.BLUE);
	    paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
	    paint.setStrokeCap(Paint.Cap.ROUND);
	    paint.setStrokeWidth(3);
	}

	/** Find and initialize the map view. */
	private void initMapView() {
		map = (MapView) findViewById(R.id.map);
		controller = map.getController();
		controller.setZoom(20);
		map.setSatellite(false);
		map.setBuiltInZoomControls(true);		
	}

	/** Start tracking the position on the map. */
	private void initMyLocation() {
		listener=new MyLocationListener();
		locationManager= (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,LOCATION_UPDATE_INTERVAL, LOCATION_UPDATE_MIN_DISTANCE,listener);
		mapOverlays = map.getOverlays(); 
		Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		points = new ArrayList<GeoPoint>();
		pathOverlay =new PathOverlay(points);
		showCurrentLocation(location);		
	}
	protected void showCurrentLocation(Location location){
		if(location!=null){
			currentGeoPoint=new GeoPoint((int)(location.getLatitude()*1e6),(int)(location.getLongitude()*1e6));
			points.add(currentGeoPoint);
			controller.animateTo(currentGeoPoint);
			mapOverlays.add(pathOverlay);
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
			startActivity(new Intent(this, SendFriendTextMessage.class));
			break;
		//made it button
		case R.id.made_it_button:
			//overlay.disableMyLocation();
			//overlay.disableCompass();
			startActivity(new Intent(this, SendMadeItTextMessage.class));
			finish();
			break;
		//danger button
		case R.id.danger_button:
			startActivity(new Intent(this, SendTextMessageDanger.class));
			break;
//			public void onLongClick(View v){
//				if(v.getId()==R.id.danger_button){
//					return true; 
//				}else{
//					return false;
//				}
//				
//			}
			
				
			}
		}
	}

	
	
