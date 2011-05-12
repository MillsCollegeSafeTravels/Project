package edu.mills.cs180.safetravels;

import java.util.ArrayList;

import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class MyItemizedOverlay extends ItemizedOverlay {
    private ArrayList<OverlayItem> myOverlays = new ArrayList<OverlayItem>();

    public MyItemizedOverlay(Drawable defaultMarker) {
        super(boundCenter(defaultMarker));
        // TODO Auto-generated constructor stub
    }

    public void addOverlay(OverlayItem overlay) {
        myOverlays.add(overlay);
        populate();
    }

    @Override
    protected OverlayItem createItem(int i) {
        return myOverlays.get(i);

    }

    @Override
    public int size() {
        // TODO Auto-generated method stub
        return myOverlays.size();
    }

}
