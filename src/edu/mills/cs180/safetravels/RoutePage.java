package edu.mills.cs180.safetravels;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

public class RoutePage extends MapActivity implements OnClickListener{
	private MapView mMapRoute;
	private MapController mControllerRoute;
	private MyLocationOverlay mOverlayRoute;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set view to mapview
        setContentView(R.layout.routepage);
        // init background map, get sat and zoom
        initMapView();
        // init location, turn on tracker and compass
        initMyLocation();
        // set up click listeners
        View TrackThisRouteButton = findViewById(R.id.track_this_route_button);
        TrackThisRouteButton.setOnClickListener(this);
    }

    /** Find and initialize the map view. */
    private void initMapView() {
        mMapRoute = (MapView) findViewById(R.id.mapRoute);
        mControllerRoute = mMapRoute.getController();
        mMapRoute.setSatellite(false);
        mMapRoute.setBuiltInZoomControls(true);
    }

    /** Start tracking the position on the map. */
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
    
    @Override
    protected boolean isRouteDisplayed() {
        // Required by MapActivity
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.track_this_route_button:
            startActivity(new Intent(this, MapPage.class));
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
        //only option is to view about info until settings are adding for plan route
        case R.id.about_menuitem:
            startActivity(new Intent(this, About.class));
            break;
        case R.id.settings_menuitem:
            startActivity(new Intent(this, Preferences.class));
            break;
        }
        return true;
    }
}
