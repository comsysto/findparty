package com.comsysto.findbuddies.android.activity.masterdetail;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.comsysto.findbuddies.android.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

/**
 * A fragment representing a single Party detail screen.
 * This fragment is either contained in a {@link PartyListActivity}
 * in two-pane mode (on tablets) or a {@link PartyMapActivity}
 * on handsets.
 */
public class PartyMapFragment extends android.support.v4.app.Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    private GoogleMap map;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PartyMapFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_party_detail, container, false);

        return rootView;
    }

    public LatLng zoomToLocation(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 14);
        map.animateCamera(cameraUpdate);
        return latLng;
    }

//    private void initializeMap() {
//        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
//                .getMap();
//        map.setMyLocationEnabled(true);
//        map.setOnCameraChangeListener(this);
//        map.setInfoWindowAdapter(this);
//    }
}
