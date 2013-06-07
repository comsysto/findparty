package com.comsysto.findbuddies.android.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.comsysto.findbuddies.android.R;
import com.comsysto.findbuddies.android.model.CategoryType;
import com.comsysto.findparty.Party;
import com.comsysto.findparty.Picture;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.android.maps.MyLocationOverlay;

import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created with IntelliJ IDEA.
 * User: stefandjurasic
 * Date: 14.03.13
 * Time: 15:42
 * To change this template use File | Settings | File Templates.
 */
public class BuddiesMapActivity extends AbstractActivity implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener, GoogleMap.InfoWindowAdapter, LocationListener, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnCameraChangeListener {

    static final LatLng HAMBURG = new LatLng(53.558, 9.927);
    static final LatLng KIEL = new LatLng(53.551, 9.993);
    public static final String SEPARATOR = " - ";
    private GoogleMap map;
    private LocationClient locationClient;
    private List<Party> parties;
    private Thread locationUpdateThread;
    private Set<String> partiesShownOnMap = new HashSet<String>();
    private static final Double SEARCH_DISTANCE = 20d;
    private HashMap<CategoryType, Bitmap> categoryDrawables;
    private MyLocationOverlay myLocOverlay;
    private Map<Marker, Party> partyMarkerMap = new HashMap<Marker, Party>();;
    private LayoutInflater inflater;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initCategoryDrawables();
        initGoogleStuff();
        setContentView(R.layout.buddies_map);
        initializeMap();
        initializeLocationClient();
    }

    private void initializeLocationClient() {
        locationClient = new LocationClient(this, this, this);
        locationClient.connect();
    }

    private void initializeMap() {
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();
        map.setMyLocationEnabled(true);
        map.setOnCameraChangeListener(this);
        map.setOnInfoWindowClickListener(this);
        map.setInfoWindowAdapter(this);
    }

    private void initGoogleStuff() {
        inflater = (LayoutInflater) getApplicationContext().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        try {
            MapsInitializer.initialize(this);
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.d("BuddiesMapActivity", "Could not initialize the MapInitializer", e);
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
        if(location == null) {
            LocationRequest request = LocationRequest.create();
            request.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            request.setNumUpdates(1);
            locationClient.requestLocationUpdates(request, this);
        } else {
            zoomToLocation(location);
        }
    }

    private void zoomToLocation(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 14);
        map.animateCamera(cameraUpdate);
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
                    } else {
                        Log.e("FindPartiesMapActivity", "Returned parties were null but at least expected empty list!");
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (BuddiesMapActivity.this.parties != null) {
                                displayParties(BuddiesMapActivity.this.parties);
                            }
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
                if(!isPartyAlreadyShownOnMap(party)){
                    Bitmap partyBitmap = categoryDrawables.get(CategoryType.valueOf(party.getCategory()));
                    Marker partyMarker = map.addMarker(getMarker(party, partyBitmap));
                    addParty(partyMarker, party);
                }
            }
        }
    }

    private boolean isPartyAlreadyShownOnMap(Party party) {
        if(partiesShownOnMap.contains(party.getId())){
            return true;
        }else{
            return false;
        }
    }

    private void addParty(Marker partyMarker, Party party) {
        partiesShownOnMap.add(party.getId());
        partyMarkerMap.put(partyMarker, party);
    }

    private MarkerOptions getMarker(Party party, Bitmap partyBitmap) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(party.getLocation().getLat(), party.getLocation().getLon()));
        markerOptions.title(party.getName());
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(partyBitmap));
        return markerOptions;
    }

    private Bitmap getUserPictureBitmap(String username) {
        Picture picture = getPartyManagerApplication().getUserPicture(username);
        if(picture != null){
            return BitmapFactory.decodeByteArray(picture.getContent(), 0, picture.getContent().length);
        }
        return null;
    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View view = inflater.inflate(R.layout.party_overlay_info, null);
        Party party = partyMarkerMap.get(marker);

        ImageView imageView = (ImageView)view.findViewById(R.id.imageView);
        imageView.setImageResource(CategoryType.valueOf(party.getCategory()).getDrawableId());

        Bitmap userPictureBitmap = getUserPictureBitmap(party.getOwner());

        if(userPictureBitmap != null){
            ImageView userPicture = (ImageView)view.findViewById(R.id.userPicture);
            userPicture.setImageBitmap(userPictureBitmap);
        }

        TextView size = (TextView)view.findViewById(R.id.sizeValue);
        size.setText(party.getSize().toString());
        TextView date = (TextView)view.findViewById(R.id.dateValue);
        date.setText(simpleDateFormat.format(party.getStartDate()));
        TextView user = (TextView)view.findViewById(R.id.userValue);
        user.setText(party.getOwner());
        TextView subject = (TextView)view.findViewById(R.id.subjectValue);
        subject.setText(party.getSubject());
        TextView experience = (TextView)view.findViewById(R.id.experienceValue);
        experience.setText(party.getLevel());
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onLocationChanged(Location location) {
        zoomToLocation(location);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.search_parties_menu_item).setVisible(false);
        return true;

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Party party = partyMarkerMap.get(marker);

        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        //Intent emailIntent = Intent.makeMainSelectorActivity(Intent.ACTION_SEND, Intent.CATEGORY_APP_EMAIL);

        String[] recipients = new String[]{party.getOwner()};

        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, recipients);

        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Your party: " + getSubject(party));

        emailIntent.setType("message/rfc822");


        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }

    private String getSubject(Party party) {
        StringBuilder subject = new StringBuilder();
        subject.append(party.getCategory());
        if(party.getSubject() != null){
            subject.append(SEPARATOR);
            subject.append(party.getSubject());
        }
        return subject.toString();
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        loadPartiesAndShowOnMap(cameraPosition.target);
    }
}
