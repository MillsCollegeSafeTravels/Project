package edu.mills.cs180.safetravels;

import com.google.android.maps.GeoPoint;

import android.location.Location;

public class TypeConverter {
	
	static GeoPoint locationToGeoPoint(Location location){
		return new GeoPoint((int)(location.getLatitude()*1e6),(int)(location.getLongitude()*1e6));
	}
	
	
}
