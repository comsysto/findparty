package com.comsysto.findbuddies.android.activity;

import android.os.Bundle;
import android.util.Log;
import com.comsysto.findbuddies.android.R;
import com.comsysto.findbuddies.android.service.LocationInfo;
import com.comsysto.findbuddies.android.service.LocationRequester;
import com.comsysto.findbuddies.android.service.LocationService;
import com.comsysto.findparty.Point;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Observable;
import java.util.Observer;


/**
 * Created with IntelliJ IDEA.
 * User: stefandjurasic
 * Date: 14.03.13
 * Time: 15:42
 * To change this template use File | Settings | File Templates.
 */
public class BuddiesMapActivity extends AbstractActivity implements LocationRequester, Observer {

    static final LatLng HAMBURG = new LatLng(53.558, 9.927);
    static final LatLng KIEL = new LatLng(53.551, 9.993);
    private GoogleMap map;
    private LocationService locationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buddies_map);
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();

        if (map != null) {
            Marker hamburg = map.addMarker(new MarkerOptions().position(HAMBURG)
                    .title("Hamburg"));
            Marker kiel = map.addMarker(new MarkerOptions()
                    .position(KIEL)
                    .title("Kiel")
                    .snippet("Kiel is cool")
                    .icon(BitmapDescriptorFactory
                            .fromResource(R.drawable.androidmarker)));
        }
        map.setMyLocationEnabled(true);




        locationService = new LocationService(getApplicationContext(), this);
        locationService.activate();
    }

    private void zoomToMyLocation(final Point location) {

        double mapLongitude = location.getLon();
        double mapLatitude = location.getLat();

        Log.d("FindPartiesMapActivity", "Current map center lon/lat : " + mapLongitude + " / " + mapLatitude);

        CameraUpdate center =
                CameraUpdateFactory.newLatLng(new LatLng(mapLatitude,
                        mapLongitude));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

        map.moveCamera(center);
        map.animateCamera(zoom);

//        FindPartiesMapActivity.this.parties = getPartyManagerApplication().getPartyService().searchParties(mapLongitude, mapLatitude, SEARCH_DISTANCE);


    }

    @Override
    public void updateLocationPoint(Point point, String address) {
        //TODO: Refactor LoccationService - separate location/address update
    }

    @Override
    public void updateLocationAddress(String address) {
        //TODO: Refactor LoccationService - separate location/address update
    }

    @Override
    public void update(Observable observable, Object data) {
        locationService.deactivate();
        LocationInfo locationInfo = (LocationInfo) data;
        Point location = new Point();
        location.setLat(locationInfo.getLatitude());
        location.setLon(locationInfo.getLongitude());
        zoomToMyLocation(location);
    }
}
