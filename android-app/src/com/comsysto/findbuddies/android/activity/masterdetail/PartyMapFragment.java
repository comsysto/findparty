package com.comsysto.findbuddies.android.activity.masterdetail;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.comsysto.findbuddies.android.R;
import com.comsysto.findbuddies.android.model.CategoryType;
import com.comsysto.findparty.Party;
import com.comsysto.findparty.Point;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

/**
 * A fragment representing a single Party detail screen.
 * This fragment is either contained in a {@link PartyListActivity}
 * in two-pane mode (on tablets) or a {@link PartyMapActivity}
 * on handsets.
 */
public class PartyMapFragment extends android.support.v4.app.Fragment
        implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener, LocationListener, Observer{
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    private GoogleMap map;
    private HashMap<CategoryType, Bitmap> categoryDrawables;
    private LocationClient locationClient;

    public static final String SEPARATOR = " - ";



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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initializeMap();
        initCategoryDrawables();
        initializeLocationClient();
    }

    private void initializeLocationClient() {
        locationClient = new LocationClient(getActivity(), this, this);
        locationClient.connect();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_party_detail, container, false);

        return rootView;
    }

    public void zoomToParty(Party party) {
        zoomToPoint(party.getLocation());

        Bitmap partyBitmap = categoryDrawables.get(CategoryType.valueOf(party.getCategory()));
        map.addMarker(getMarker(party, partyBitmap));
    }

    private void zoomToPoint(Point point) {
        LatLng latLng = new LatLng(point.getLat(), point.getLon());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 14);
        map.animateCamera(cameraUpdate);
    }

    private MarkerOptions getMarker(Party party, Bitmap partyBitmap) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(party.getLocation().getLat(), party.getLocation().getLon()));
        markerOptions.title(party.getName());
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(partyBitmap));
        return markerOptions;
    }

    private void initializeMap() {
        SupportMapFragment supportMapFragment = (SupportMapFragment)getFragmentManager().findFragmentById(R.id.map);


        map = supportMapFragment.getMap();

        map.setMyLocationEnabled(true);

        map.setMyLocationEnabled(true);

    }

    private void initCategoryDrawables() {
        categoryDrawables = new HashMap<CategoryType, Bitmap>();
        categoryDrawables.put(CategoryType.BIKING, getDrawable(R.drawable.biking));
        categoryDrawables.put(CategoryType.CLUBBING, getDrawable(R.drawable.clubbing));
        categoryDrawables.put(CategoryType.HIKING, getDrawable(R.drawable.hiking));
        categoryDrawables.put(CategoryType.JOGGING, getDrawable(R.drawable.jogging));
        categoryDrawables.put(CategoryType.MUSIC, getDrawable(R.drawable.music));
        categoryDrawables.put(CategoryType.SNUGGLING, getDrawable(R.drawable.snuggling));
        categoryDrawables.put(CategoryType.SWIMMING, getDrawable(R.drawable.androidmarker));
    }

    private Bitmap getDrawable(int id) {
        Drawable drawable = this.getResources().getDrawable(id);
        return ((BitmapDrawable) drawable).getBitmap();
    }


    @Override
    public void onConnected(Bundle bundle) {
        Location location = locationClient.getLastLocation();
        if(location == null) {
            LocationRequest request = LocationRequest.create();
            request.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            request.setNumUpdates(1);
            locationClient.requestLocationUpdates(request, this);
        } else {
            zoomToPoint(getPoint(location));
        }
    }

    private Point getPoint(Location location) {
        Point point = new Point();
        point.setLat(location.getLatitude());
        point.setLon(location.getLongitude());
        return point;
    }

    @Override
    public void onDisconnected() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onLocationChanged(Location location) {
        zoomToPoint(getPoint(location));
    }

    @Override
    public void update(Observable observable, Object data) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
