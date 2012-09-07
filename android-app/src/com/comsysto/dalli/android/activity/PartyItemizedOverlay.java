package com.comsysto.dalli.android.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: stefandjurasic
 * Date: 07.09.12
 * Time: 14:29
 * To change this template use File | Settings | File Templates.
 */
public class PartyItemizedOverlay extends ItemizedOverlay<OverlayItem> {

    private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
    private Context context;

    public PartyItemizedOverlay(Drawable defaultMarker, Context context) {
        super(boundCenterBottom(defaultMarker));
        this.context = context;
    }

    @Override
    protected OverlayItem createItem(int i) {
        return mOverlays.get(0);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int size() {
        return mOverlays.size();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected boolean onTap(int index) {
        OverlayItem item = mOverlays.get(index);
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(item.getTitle());
        dialog.setMessage(item.getSnippet());
        dialog.show();
        return true;
    }

    public void addOverlay(OverlayItem overlay) {
        mOverlays.add(overlay);
        populate();
    }
}
