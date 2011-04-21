package edu.mills.cs180.safetravels;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Projection;

public class MyOverlay extends MyLocationOverlay{
    private Point point1;
    private Point point2;
    private GeoPoint currentGeoPoint;
    private Projection projection;
    private GeoPoint lastGeoPoint;
    private MapPage mapPage;
    
    
    public MyOverlay(android.content.Context context,MapView mapView){
    	super(context, mapView);
        projection = mapView.getProjection();
    }
    @Override 
    public void draw(Canvas canvas, MapView mapView, boolean shadow){
        super.draw(canvas, mapView, shadow);
        Paint paint = new Paint();
        paint.setDither(true);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(3);
       
        point1 = new Point();
        point2 = new Point();
        Path path = new Path();
       
        /* define your own GeoPoints here
            example: geoPoint1 = new GeoPoint(int latitudeE6, int longitudeE6);
            see documentation: http://code.google.com/android/add-ons/ ... Point.html
        */
        
        currentGeoPoint = getMyLocation();
        lastGeoPoint=mapPage.geoPoint;
        
        projection.toPixels(lastGeoPoint, point1);
        projection.toPixels(currentGeoPoint, point2);
       
        path.moveTo(point2.x, point2.y);
        path.lineTo(point1.x, point1.y);
       
        canvas.drawPath(path, paint);
        
        mapPage.geoPoint=currentGeoPoint;
    }

}


 

 
 