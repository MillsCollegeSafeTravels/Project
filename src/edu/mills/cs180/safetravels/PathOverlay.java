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

public class PathOverlay extends Overlay{
	private List<GeoPoint> gpoints;

    public PathOverlay(List<GeoPoint> gpoints){
        this.gpoints = gpoints;
    }

    @Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow) 
    {
        List<Point> mpoints = new ArrayList<Point>();
        
        // Convert to a point that can be drawn on the map.
        for(GeoPoint g : gpoints){    
        	Point tpoint = new Point();
            mapView.getProjection().toPixels(g, tpoint);
            mpoints.add(tpoint);
        }

        Path path = new Path();

        // Create a path from the points
        
        path.moveTo(mpoints.get(0).x, mpoints.get(0).y);
        for(Point p : mpoints){
            path.lineTo(p.x, p.y);
        }

        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
	    paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
	    paint.setStrokeCap(Paint.Cap.ROUND);
        //paint.setARGB(255, 255, 0, 0);
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(3);
        // Draw to the map
        canvas.drawPath(path,paint);

    }
}
