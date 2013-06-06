package com.comsysto.findbuddies.android.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.comsysto.findbuddies.android.R;
import com.comsysto.findbuddies.android.model.CategoryType;
import com.comsysto.findparty.Party;
import com.comsysto.findparty.Picture;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import java.util.HashMap;
import java.util.List;



/**
 * Created with IntelliJ IDEA.
 * User: stefandjurasic
 * Date: 14.03.13
 * Time: 15:42
 * To change this template use File | Settings | File Templates.
 */
public class BuddiesMapActivity extends AbstractActivity implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener, GoogleMap.OnCameraChangeListener {

    static final LatLng HAMBURG = new LatLng(53.558, 9.927);
    static final LatLng KIEL = new LatLng(53.551, 9.993);
    private GoogleMap map;
    private LocationClient locationClient;
    private List<Party> parties;
    private Thread locationUpdateThread;
    private static final Double SEARCH_DISTANCE = 20d;
    private HashMap<CategoryType, Bitmap> categoryDrawables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initCategoryDrawables();
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
        map.setOnCameraChangeListener(this);

        locationClient = new LocationClient(this, this, this);
        locationClient.connect();
        try {
            MapsInitializer.initialize(this);
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

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
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 14);
        map.animateCamera(cameraUpdate);
        loadPartiesAndShowOnMap(latLng);
    }

    private void loadPartiesAndShowOnMap(final LatLng center) {

        this.locationUpdateThread = new Thread(new Runnable() {

            @Override
            public void run() {
                synchronized (BuddiesMapActivity.this) {
                    double mapLongitude = center.longitude;
                    double mapLatitude = center.latitude;

                    Log.d("FindPartiesMapActivity", "Current map center lon/lat : " + mapLongitude + " / " + mapLatitude);

                    BuddiesMapActivity.this.parties = getPartyManagerApplication().getPartyService().searchParties(mapLongitude, mapLatitude, SEARCH_DISTANCE);

                    if(BuddiesMapActivity.this.parties != null){
                        Log.d("FindPartiesMapActivity", BuddiesMapActivity.this.parties.size() + " parties found in " + SEARCH_DISTANCE + " km area: " + BuddiesMapActivity.this.parties.toString());
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            displayParties(BuddiesMapActivity.this.parties);
                        }
                    });


                }
            }
        });

        locationUpdateThread.start();
    }

    private void displayParties(List<Party> parties) {
        for (Party party : parties) {
            if (map != null) {
                Bitmap partyBitmap = categoryDrawables.get(CategoryType.valueOf(party.getCategory()));
                map.addMarker(new MarkerOptions().position(new LatLng(party.getLocation().getLat(), party.getLocation().getLon()))
                        .title(party.getName())
                .icon(BitmapDescriptorFactory.fromBitmap(partyBitmap)));
            }
        }
    }

    private BitmapDescriptor getUserPictureBitmap(String username) {
        Picture picture = getPartyManagerApplication().getUserPicture(username);
        if(picture != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(picture.getContent(), 0, picture.getContent().length);
            return BitmapDescriptorFactory.fromBitmap(bitmap);
        }
        return null;
    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        loadPartiesAndShowOnMap(cameraPosition.target);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }
}
