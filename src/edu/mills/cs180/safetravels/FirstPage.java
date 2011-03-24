package edu.mills.cs180.safetravels;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class FirstPage extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //set up click listeners
        View TrackButton = findViewById(R.id.track_button);
        TrackButton.setOnClickListener(this);
        View SafeRouteButton = findViewById(R.id.route_button);
        SafeRouteButton.setOnClickListener(this);
    }
	@Override
    public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()){
		case R.id.route_button:
			startActivity(new Intent(this,RoutePage.class));
    		break;
		case R.id.track_button:
			startActivity(new Intent(this,TrackPage.class));
			break;
		}
   	}
}