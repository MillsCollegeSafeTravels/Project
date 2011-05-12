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
import android.widget.ProgressBar;

/**
 * FirstPage is the first activity the user sees. The user can click buttons to track themselves or 
 * plan a route.
 * 
 * @author Kate Feeny
 * @author Jess Martin
 * @author TeAirra Ward
 * @author Jodessa Lanzadares
 * @author Dani E-F
 *
 */
public class FirstPage extends Activity implements OnClickListener {
	
	/**
	 * A ProgressBar for visual effect
	 */
	private ProgressBar mProgress;
	
	/**
	 * Called when the activity is starting. Sets the onClickListeners for the button Views to this.
	 * 
	 * @param savedInstanceState if the activity is being re-initialized after previously being 
	 * shut down then this Bundle contains the data it most recently supplied in 
	 * onSaveInstanceState(Bundle). Note: Otherwise it is null.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mProgress = (ProgressBar) findViewById(R.id.progress_bar);
		//set up click listeners
		View trackButton = findViewById(R.id.track_button);
		trackButton.setOnClickListener(this);
		View SafeRouteButton = findViewById(R.id.route_button);
		SafeRouteButton.setOnClickListener(this);
	}//onCreate
	
	/**
	 * Calls its super's method
	 */
	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		Window window = getWindow();
		// Eliminates color banding
		window.setFormat(PixelFormat.RGBA_8888);
	}//onAttachedToWindow
	
	/**
	 * Called when a View is clicked. If the route button was clicked, the RoutePage activity is 
	 * started. If the track button was clicked, the MapPage activity is started.
	 * 
	 * @param v the View that was clicked
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()){
		case R.id.route_button:
			startActivity(new Intent(this,RoutePage.class));			
			break;
		case R.id.track_button:
			startActivity(new Intent(this,MapPage.class));
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
			break;
		case R.id.settings_menuitem:
			startActivity(new Intent(this, Preferences.class));
			break;
		}
		return true;
	}
}
