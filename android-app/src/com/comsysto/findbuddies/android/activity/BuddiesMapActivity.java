package com.comsysto.findbuddies.android.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.comsysto.findbuddies.android.R;
import com.comsysto.findbuddies.android.application.PartyManagerApplication;
import com.comsysto.findbuddies.android.model.CategoryType;
import com.comsysto.findbuddies.android.model.LevelType;
import com.comsysto.findbuddies.android.service.async.party.SearchPartiesAsync;
import com.comsysto.findbuddies.android.service.async.party.SearchPartiesCallback;
import com.comsysto.findbuddies.android.service.async.picture.GetPictureAsync;
import com.comsysto.findbuddies.android.service.async.picture.GetPictureCallback;
import com.comsysto.findparty.Party;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import java.text.SimpleDateFormat;
import java.util.*;


/**
 * User: stefandjurasic
 * Date: 14.03.13
 * Time: 15:42
 */
public class BuddiesMapActivity extends AbstractActivity implements
        SearchPartiesCallback,
        GetPictureCallback,
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener, GoogleMap.InfoWindowAdapter, LocationListener, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnCameraChangeListener {

    public static final String SEPARATOR = " - ";
    private GoogleMap map;
    private LocationClient locationClient;

    private Set<String> partiesShownOnMap = new HashSet<String>();
    private HashMap<CategoryType, Bitmap> categoryDrawables;
    private Map<Marker, Party> partyMarkerMap = new HashMap<Marker, Party>();
    private LayoutInflater inflater;

    private MarkerAndView lastShownMarkerAndView;


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
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
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
        categoryDrawables.put(CategoryType.BIKING, getDrawable(CategoryType.BIKING.getDrawableId()));
        categoryDrawables.put(CategoryType.CLUBBING, getDrawable(CategoryType.CLUBBING.getDrawableId()));
        categoryDrawables.put(CategoryType.HIKING, getDrawable(CategoryType.HIKING.getDrawableId()));
        categoryDrawables.put(CategoryType.JOGGING, getDrawable(CategoryType.JOGGING.getDrawableId()));
        categoryDrawables.put(CategoryType.DANCING, getDrawable(CategoryType.DANCING.getDrawableId()));
        categoryDrawables.put(CategoryType.SNUGGLING, getDrawable(CategoryType.SNUGGLING.getDrawableId()));
        categoryDrawables.put(CategoryType.SWIMMING, getDrawable(CategoryType.SWIMMING.getDrawableId()));
        categoryDrawables.put(CategoryType.SOCCER, getDrawable(CategoryType.SOCCER.getDrawableId()));
        categoryDrawables.put(CategoryType.MUSIC, getDrawable(CategoryType.MUSIC.getDrawableId()));

    }

    private Bitmap getDrawable(int id) {
        BitmapDrawable drawable = (BitmapDrawable) this.getResources().getDrawable(id);
        Bitmap bitmap = drawable.getBitmap();
        //bitmap.setDensity(50 * PartyManagerApplication.getInstance().getDeviceDensity());
        return bitmap;
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location location = locationClient.getLastLocation();
        if (location == null) {
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
        double mapLongitude = center.longitude;
        double mapLatitude = center.latitude;

        new SearchPartiesAsync(this, mapLongitude, mapLatitude).execute();
    }

    private void displayParties(List<Party> parties) {
        for (Party party : parties) {
            if (map != null) {
                if (!isPartyAlreadyShownOnMap(party)) {
                    Bitmap partyBitmap = categoryDrawables.get(CategoryType.getForDisplayName(party.getCategory()));
                    Marker partyMarker = map.addMarker(getMarker(party, partyBitmap));
                    addParty(partyMarker, party);
                }
            }
        }
    }

    private boolean isPartyAlreadyShownOnMap(Party party) {
        if (partiesShownOnMap.contains(party.getId())) {
            return true;
        } else {
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


    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public View getInfoWindow(Marker marker) {
        Party party = partyMarkerMap.get(marker);
        if (lastShownMarkerAndView != null && party.getPictureUrl() != null) {
            synchronized (lastShownMarkerAndView) {
                if (party.getPictureUrl().equals(lastShownMarkerAndView.partyPictureUrl)) {
                    View view = lastShownMarkerAndView.view;
                    ImageView userPicture = (ImageView)view.findViewById(R.id.partyPicture);
                    userPicture.setImageBitmap(lastShownMarkerAndView.partyBitmap);
                    userPicture.setVisibility(View.VISIBLE);
                    this.lastShownMarkerAndView = null;
                    return view;
                }
                this.lastShownMarkerAndView = null;
            }
        }

        View view = inflater.inflate(R.layout.party_overlay_info, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        imageView.setImageResource(CategoryType.valueOf(party.getCategory()).getDrawableId());

        TextView size = (TextView) view.findViewById(R.id.sizeValue);
        size.setText(party.getSize().toString());
        TextView date = (TextView) view.findViewById(R.id.dateValue);

        SimpleDateFormat simpleDateFormat = PartyManagerApplication.getInstance().getSimpleDateFormat();

        date.setText(simpleDateFormat.format(party.getStartDate()));
        TextView user = (TextView) view.findViewById(R.id.userValue);
        user.setText(party.getOwner());
        TextView subject = (TextView) view.findViewById(R.id.subjectValue);
        subject.setText(party.getSubject());
        TextView experience = (TextView) view.findViewById(R.id.experienceValue);
        experience.setText(LevelType.getDisplayString(this, party.getLevel()));


        if (party.getPictureUrl() != null) {
            new GetPictureAsync(this, party.getPictureUrl()).execute();
            this.lastShownMarkerAndView = new MarkerAndView(marker, view);
        }

        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
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

        Intent emailIntent = new Intent(android.content.Intent.ACTION_SENDTO);

        String uriText =
                "mailto:" + party.getOwner() +
                        "?subject=Your party: " + getSubject(party);

        emailIntent.setData(Uri.parse(uriText));

        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }

    private String getSubject(Party party) {
        StringBuilder subject = new StringBuilder();
        subject.append(party.getCategory());
        if (party.getSubject() != null) {
            subject.append(SEPARATOR);
            subject.append(party.getSubject());
        }
        return subject.toString();
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        loadPartiesAndShowOnMap(cameraPosition.target);
    }

    @Override
    public void successOnGetPicture(Bitmap bitmap, String pictureUrl) {
        if (lastShownMarkerAndView != null) {
            Marker marker = lastShownMarkerAndView.marker;

            if (marker != null && marker.isInfoWindowShown()) {
                lastShownMarkerAndView.partyBitmap = bitmap;
                lastShownMarkerAndView.partyPictureUrl = pictureUrl;
                marker.hideInfoWindow();
                marker.showInfoWindow();
            }

        }

    }

    @Override
    public void errorOnGetPicture() {
        lastShownMarkerAndView = null;
    }

    @Override
    public void successOnSearchParties(List<Party> parties) {
        if (parties != null) {
            displayParties(parties);
        }


    }

    @Override
    public void failureOnSearchParties() {
        Toast.makeText(this, "Could not load Parties", Toast.LENGTH_LONG).show();
    }

    private class MarkerAndView {
        private final Marker marker;
        private final View view;
        private String partyPictureUrl;
        private Bitmap partyBitmap;

        public MarkerAndView(Marker marker, View view) {
            this.marker = marker;
            this.view = view;
        }
    }
}
