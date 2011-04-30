// Created by plusminus on 22:01:11 - 29.09.2008
package edu.mills.cs180.safetravels;

import java.util.LinkedList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

/**
 * 
 * @author Manuel Stahl
 *
 */
public class MyMyLocationOverlay extends Overlay implements LocationListener {
        // ===========================================================
        // Constants
        // ===========================================================

        // ===========================================================
        // Fields
        // ===========================================================
        
        protected final Paint mPaint = new Paint();
        protected final Paint mCirclePaint = new Paint();
        
        protected final Bitmap DIRECTION_ARROW;
        
        protected MapController mMapController;
        private Context mCtx;
        private LocationManager mLocationManager;
        private boolean mMyLocationEnabled = false;
        private LinkedList<Runnable> mRunOnFirstFix = new LinkedList<Runnable>();
        private final Point mScreenCoords = new Point();
        
        protected Location mLocation;
        protected boolean mFollow = false;      // follow location updates
        
        private final Matrix directionRotater = new Matrix();
        
        private final float DIRECTION_ARROW_CENTER_X;
        private final float DIRECTION_ARROW_CENTER_Y;
        private final int DIRECTION_ARROW_WIDTH;
        private final int DIRECTION_ARROW_HEIGHT;

        // ===========================================================
        // Constructors
        // ===========================================================
        
        public MyMyLocationOverlay(final Context ctx, final MapView mapView) {
                this.mCtx = ctx;
                this.mMapController = mapView.getController();
                this.mCirclePaint.setARGB(0, 100, 100, 255);
                this.mCirclePaint.setAntiAlias(true);
                
                this.DIRECTION_ARROW = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.icon28);
                
                this.DIRECTION_ARROW_CENTER_X = this.DIRECTION_ARROW.getWidth() / 2 - 0.5f;
                this.DIRECTION_ARROW_CENTER_Y = this.DIRECTION_ARROW.getHeight() / 2 - 0.5f;
                this.DIRECTION_ARROW_HEIGHT = this.DIRECTION_ARROW.getHeight();
                this.DIRECTION_ARROW_WIDTH = this.DIRECTION_ARROW.getWidth();
        }

        // ===========================================================
        // Getter & Setter
        // ===========================================================
        
        public Location getLastFix() {
                return mLocation;
        }
        
        public GeoPoint getMyLocation(Location myLocation) {
                return TypeConverter.locationToGeoPoint(mLocation);
        }

        public boolean isMyLocationEnabled() {
                return mMyLocationEnabled;
        }
        
        public boolean isLocationFollowEnabled() {
                return mFollow;
        }
        
        public void followLocation(boolean enable) {
                mFollow = enable;
        }
        
        // ===========================================================
        // Methods from SuperClass/Interfaces
        // ===========================================================
        
       // @Override
       // protected void onDrawFinished(Canvas c, OpenStreetMapView osmv) {}
        
        public void onDraw(final Canvas c, final MapView osmv) {
                if(this.mLocation != null) {
                        final Projection pj = osmv.getProjection();
                        pj.toPixels(TypeConverter.locationToGeoPoint(mLocation), mScreenCoords);
                        final float radius = pj.metersToEquatorPixels(this.mLocation.getAccuracy());
                        
                        this.mCirclePaint.setAlpha(50);
                        this.mCirclePaint.setStyle(Style.FILL);
                        c.drawCircle(mScreenCoords.x, mScreenCoords.y, radius, this.mCirclePaint);
                        
                        this.mCirclePaint.setAlpha(150);
                        this.mCirclePaint.setStyle(Style.STROKE);
                        c.drawCircle(mScreenCoords.x, mScreenCoords.y, radius, this.mCirclePaint);
                        
                        float[] mtx = new float[9];
                        c.getMatrix().getValues(mtx);
                        
                        /* Rotate the direction-Arrow according to the bearing we are driving. And draw it to the canvas. */
                        Bitmap arrow = Bitmap.createBitmap(DIRECTION_ARROW, 0, 0, DIRECTION_ARROW_WIDTH, DIRECTION_ARROW_HEIGHT);
                        this.directionRotater.setRotate(this.mLocation.getBearing(), DIRECTION_ARROW_CENTER_X , DIRECTION_ARROW_CENTER_Y);
                        this.directionRotater.postTranslate(-arrow.getWidth() / 2, -arrow.getHeight() / 2);                     
                        this.directionRotater.postScale(1/mtx[Matrix.MSCALE_X], 1/mtx[Matrix.MSCALE_Y]);
                        this.directionRotater.postTranslate(mScreenCoords.x, mScreenCoords.y);
                        c.drawBitmap(arrow, this.directionRotater, this.mPaint);
                }
        }

        @Override
        public void onLocationChanged(Location location) {
                mLocation = location;
                if (mFollow)
                        mMapController.animateTo(TypeConverter.locationToGeoPoint(location));
        }
        
        @Override
        public void onProviderDisabled(String provider) {
        }
        
        @Override
        public void onProviderEnabled(String provider) {
        }
        
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
                if(status == LocationProvider.AVAILABLE) {
                        final Thread t = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                        for(Runnable runnable: mRunOnFirstFix) {
                                                runnable.run();
                                        }
                                        mRunOnFirstFix.clear();
                                }
                        });
                        t.run();
                }
        }
        
        // ===========================================================
        // Methods
        // ===========================================================

        public void disableMyLocation() {
                getLocationManager().removeUpdates(this);
        }
        
        public boolean enableMyLocation() {
                if (!mMyLocationEnabled) {
                        Criteria crit = new Criteria();
                        crit.setAccuracy(Criteria.ACCURACY_FINE);
                        
                        String provider = getLocationManager().getBestProvider(crit, true);
                        try {
                                getLocationManager().requestLocationUpdates(provider, 0, 0, this);
                         
                                
                        } catch(Exception e) {
                                disableMyLocation();
                               // Toast.makeText(this.mCtx, R.string.no_location_provider, Toast.LENGTH_LONG).show();
                                return mMyLocationEnabled = false;
                        }
                }
                return mMyLocationEnabled = true;
        }
        
        public boolean runOnFirstFix(Runnable runnable) {
                if(mMyLocationEnabled) {
                        runnable.run();
                        return true;
                } else {
                        mRunOnFirstFix.addLast(runnable);
                        return false;
                }
        }
        
        private LocationManager getLocationManager() {
                if(this.mLocationManager == null)
                        this.mLocationManager = (LocationManager) mCtx.getSystemService(Context.LOCATION_SERVICE);
                return this.mLocationManager; 
        }

		public void enableCompass() {
			// TODO Auto-generated method stub
			
		}

		public void disableCompass() {
			// TODO Auto-generated method stub
			
		}

        
        // ===========================================================
        // Inner and Anonymous Classes
        // ===========================================================
}