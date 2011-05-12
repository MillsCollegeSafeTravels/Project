package edu.mills.cs180.safetravels;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

public class FirstPage extends Activity implements OnClickListener{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // set up click listeners
        View trackButton = findViewById(R.id.track_button);
        trackButton.setOnClickListener(this);
        View SafeRouteButton = findViewById(R.id.route_button);
        SafeRouteButton.setOnClickListener(this);
    }
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        // Eliminates color banding
        window.setFormat(PixelFormat.RGBA_8888);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.route_button:
            startActivity(new Intent(this, RoutePage.class));            
            break;
        case R.id.track_button:   
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