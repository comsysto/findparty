package com.comsysto.dalli.android.map;

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
    private ArrayList<String> partyIds = new ArrayList<String>();

    private Context context;

    public PartyItemizedOverlay(Drawable defaultMarker, Context context) {
        super(boundCenterBottom(defaultMarker));
        this.context = context;
    }

    @Override
    protected OverlayItem createItem(int i) {
        return mOverlays.get(i);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int size() {
        return mOverlays.size();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected boolean onTap(int index) {
        OverlayItem item = mOverlays.get(index);
        if(item!=null) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setTitle(item.getTitle());
            dialog.setMessage(item.getSnippet());
            dialog.show();
            return true;
        }
        return false;
    }

    public void addOverlay(String partyId, OverlayItem overlay) {
        this.mOverlays.add(overlay);
        this.partyIds.add(partyId);

        populate();
    }

    public boolean containsParty(String partyId) {
        return partyIds.contains(partyId);
    }


    public static Drawable boundCenterBottom(Drawable image) {
        return ItemizedOverlay.boundCenterBottom(image);
    }

    public void populateNow() {
        populate();
    }
}
