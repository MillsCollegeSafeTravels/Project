package edu.mills.cs180.safetravels;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

/**
 * PathOverlay is a class that can draw a path from a given list of GeoPoints.
 * 
 * @author Kate Feeny
 * @author Jess Martin
 * @author TeAirra Ward
 * @author Jodessa Lanzadares
 * @author Dani E-F
 *
 */
public class PathOverlay extends Overlay{
	
	/**
	 * A list of GeoPoints.
	 */
	private List<GeoPoint> mGPoints;
	
	/**
	 * Class constructor specifying the list of GeoPoints to be assigned to mGPoints.
	 * 
	 * @param gpoints the list of GeoPoints to be assigned to mGPoints
	 */
    public PathOverlay(List<GeoPoint> gpoints){
        this.mGPoints = gpoints;
    }//Constructor
    
    /**
     * Draws a path on the map by converting GeoPoints to points that can be drawn, creating a path 
     * from the points, and creating a Paint object for the path to be drawn using the new Paint.
     * 
     * @param canvas the Canvas on which the path will be drawn
     * @param mapView the MapView from which the points are generated
     * @param shadow a boolean that when true allows the shadow overlay to be drawn
     */
    @Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {
        List<Point> mPoints = new ArrayList<Point>();
        Path path = new Path();
        
        // Convert to a point that can be drawn on the map.
        for(GeoPoint g : mGPoints){    
        	Point tpoint = new Point();
            mapView.getProjection().toPixels(g, tpoint);
            mPoints.add(tpoint);
        }//for
        
        // Create a path from the points
        path.moveTo(mPoints.get(0).x, mPoints.get(0).y);
        for(Point p : mPoints){
            path.lineTo(p.x, p.y);
        }//for
        
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
	    paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
	    paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(3);
        
        // Draw to the map
        canvas.drawPath(path,paint);
    }//draw
    
}//PathOverlay
