package edu.mills.cs180.safetravels;

import android.os.Bundle;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class TrackPage extends MapActivity {
	private MapView map;
	private MapController controller;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trackpage);
        initMapView();
        initMyLocation();
    }
    @Override
    protected boolean isRouteDisplayed(){
    	return false;
    }
    private void initMapView(){
    	map=(MapView) findViewById(R.id.mapview);
    	controller = map.getController();
    	map.setSatellite(true);
    	map.setBuiltInZoomControls(true);
    }
}