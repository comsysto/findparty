package com.comsysto.dalli.android.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import com.comsysto.dalli.android.R;
import com.comsysto.dalli.android.application.PartyManagerApplication;
import com.comsysto.dalli.android.map.PartyItemizedOverlay;
import com.comsysto.dalli.android.model.CategoryType;
import com.comsysto.findparty.Party;
import com.google.android.maps.*;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: stefandjurasic
 * Date: 07.09.12
 * Time: 13:42
 * To change this template use File | Settings | File Templates.
 */
public class FindPartiesMapActivity extends MapActivity {

    private MapView mapView;
    private Double currentDistance = 1000.;

    private static final Double SEARCH_DISTANCE = 20d;

    private MyLocationOverlay myLocOverlay;
    private PartyItemizedOverlay itemizedoverlay;

    private Map<CategoryType, Drawable> categoryDrawables;
    private Thread locationUpdateThread;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.find_parties_map);
        mapView = (MapView) findViewById(R.id.find_parties_map_view);
        //mapView.setBuiltInZoomControls(true);

        mapView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    loadPartiesAndShowOnMap();
                }

                return false;
            }
        });

        initCategoryDrawables();

        initializeOverlays();

        zoomToMyLocation();

        loadPartiesAndShowOnMap();
    }

    private void initCategoryDrawables() {
        categoryDrawables = new HashMap<CategoryType, Drawable>();
        categoryDrawables.put(CategoryType.BIKING, getDrawable(R.drawable.androidmarker_red));
        categoryDrawables.put(CategoryType.CLUBBING, getDrawable(R.drawable.androidmarker));
        categoryDrawables.put(CategoryType.HIKING, getDrawable(R.drawable.androidmarker_red));
        categoryDrawables.put(CategoryType.JOGGING, getDrawable(R.drawable.androidmarker_red));
        categoryDrawables.put(CategoryType.MUSIC, getDrawable(R.drawable.androidmarker));
        categoryDrawables.put(CategoryType.RUNNING, getDrawable(R.drawable.androidmarker_red));
        categoryDrawables.put(CategoryType.SWIMMING, getDrawable(R.drawable.androidmarker_red));
    }

    private void initializeOverlays() {
        myLocOverlay = new MyLocationOverlay(this, mapView);
        myLocOverlay.enableMyLocation();


        Drawable androidMarker = getDrawable(R.drawable.androidmarker);
        itemizedoverlay = new PartyItemizedOverlay(androidMarker, FindPartiesMapActivity.this);
        itemizedoverlay.populateNow();
        List<Overlay> overlays = mapView.getOverlays();
        overlays.add(myLocOverlay);
        overlays.add(itemizedoverlay);
    }

    private Drawable getDrawable(int id) {
        return PartyItemizedOverlay.boundCenterBottom(FindPartiesMapActivity.this.getResources().getDrawable(id));
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * returns the {@link com.comsysto.dalli.android.application.PartyManagerApplication}
     * @return instance of {@link com.comsysto.dalli.android.application.PartyManagerApplication}
     */
    PartyManagerApplication getPartyManagerApplication() {
        return (PartyManagerApplication) getApplication();
    }

    private void zoomToMyLocation() {
        myLocOverlay.runOnFirstFix(new Runnable() {
            public void run() {
                    mapView.getController().setZoom(15);
                    mapView.getController().animateTo(myLocOverlay.getMyLocation());
                    mapView.invalidate();
                    loadPartiesAndShowOnMap();
            }
        });

    }

    private void loadPartiesAndShowOnMap() {
        Log.d("FindPartiesMapActivity", "Map moved --> looking for nearby parties");
        final GeoPoint geoPoint = mapView.getMapCenter();

        locationUpdateThread = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (FindPartiesMapActivity.this) {
                    double mapLongitude = geoPoint.getLongitudeE6()/1E6;
                    double mapLatitude = geoPoint.getLatitudeE6()/1E6;

                    Log.d("FindPartiesMapActivity", "Current map center lon/lat : " + mapLongitude + " / " + mapLatitude);

                    List<Party> parties = getPartyManagerApplication().searchParties(mapLongitude, mapLatitude, SEARCH_DISTANCE);

                    Log.d("FindPartiesMapActivity", parties.size() + " parties found in " + SEARCH_DISTANCE + " km area: " + parties.toString());

                    for (Party party : parties) {
                        if (itemizedoverlay.containsParty(party.getId())) {
                            continue;
                        }

                        GeoPoint partyLocation = new GeoPoint(getMicroDegrees(party.getLocation().getLat()), getMicroDegrees(party.getLocation().getLon().doubleValue()));

                        OverlayItem overlayitem = new OverlayItem(partyLocation, party.getCategory(), party.getOwner() + " " + new SimpleDateFormat().format(party.getStartDate()));
                        if (categoryDrawables.containsKey(CategoryType.valueOf(party.getCategory()))) {
                            overlayitem.setMarker(categoryDrawables.get(CategoryType.valueOf(party.getCategory())));
                        }
                        itemizedoverlay.addOverlay(party.getId(), overlayitem);
                    }
                }
            }
        });

        locationUpdateThread.start();
    }

    private int getMicroDegrees(Double coordinate) {
        return (int)(coordinate.doubleValue() * 1E6);
    }
}

