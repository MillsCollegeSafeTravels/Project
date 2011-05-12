package edu.mills.cs180.safetravels;
import android.app.Activity;
import android.os.Bundle;

/**
 * About is an activity class that allows the user to view the information about app.
 * 
 * @author Kate Feeny
 * @author Jess Martin
 * @author TeAirra Ward
 * @author Jodessa Lanzadares
 * @author Dani E-F
 *
 */
public class About extends Activity {
	
	/**
	 * Called when the activity is starting. Sets the content view to the about layout.
	 * 
	 * @param savedInstanceState if the activity is being re-initialized after previously being 
	 * shut down then this Bundle contains the data it most recently supplied in 
	 * onSaveInstanceState(Bundle). Note: Otherwise it is null.
	 */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
    }//onCreate
}//About