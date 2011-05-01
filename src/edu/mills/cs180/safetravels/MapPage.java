/**
 * 
 */
package edu.mills.cs180.safetravels;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

/**
 * @author KateFeeney
 *
 */
public class MapPage extends MapActivity implements OnClickListener {
	private MapView map;
	private MapController controller;
	
	private static final long LOCATION_UPDATE_MIN_DISTANCE = 1;
	private static final long LOCATION_UPDATE_INTERVAL = 1000;
	public static LocationManager locationManager;
	private GeoPoint currentGeoPoint;
	static MyLocationListener listener;
	
	List<Overlay> mapOverlays;
	private MyItemizedOverlay itemizedOverlay;
	Drawable drawable;
	
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
		map.setSatellite(false);
		map.setBuiltInZoomControls(true);		
	}

	/** Start tracking the position on the map. */
	private void initMyLocation() {
		listener=new MyLocationListener();
		locationManager= (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,LOCATION_UPDATE_INTERVAL, LOCATION_UPDATE_MIN_DISTANCE,listener);
		mapOverlays = map.getOverlays(); 
		drawable = this.getResources().getDrawable(R.drawable.icon57);
		itemizedOverlay = new MyItemizedOverlay(drawable);
		showCurrentLocation();
	}
	protected void showCurrentLocation(){
		Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if(location!=null){
			currentGeoPoint=new GeoPoint((int)(location.getLatitude()*1e6),(int)(location.getLongitude()*1e6));
			controller.setZoom(20);
			controller.animateTo(currentGeoPoint);
			OverlayItem overLayitem = new OverlayItem(currentGeoPoint, null, null);
			itemizedOverlay.addOverlay(overLayitem);
			mapOverlays.add(itemizedOverlay);
		}
	}
	
	private class MyLocationListener implements LocationListener{
		public void onLocationChanged(Location location){
			showCurrentLocation();
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
/*	public class MyOverlay extends Overlay{

	    @Override 
	    public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when){
	        super.draw(canvas, mapView, shadow);
	        Point screenPts=new Point();
	        mapView.getProjection().toPixels(currentGeoPoint, screenPts);
	        canvas.drawPoint(screenPts.x, screenPts.y, paint);
			return true;
	    }*/
	//}
    
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
			//overlay.disableMyLocation();
			//overlay.disableCompass();
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
