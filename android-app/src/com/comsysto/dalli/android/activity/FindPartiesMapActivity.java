package com.comsysto.dalli.android.activity;

import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.comsysto.dalli.android.R;
import com.comsysto.dalli.android.application.PartyManagerApplication;
import com.comsysto.dalli.android.map.PartyItemizedOverlay;
import com.comsysto.dalli.android.map.PartyOverlayItem;
import com.comsysto.dalli.android.model.CategoryType;
import com.comsysto.findparty.Party;
import com.comsysto.findparty.Picture;
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
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

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

        categoryDrawables.put(CategoryType.BIKING, getDrawable(R.drawable.biking));
        categoryDrawables.put(CategoryType.CLUBBING, getDrawable(R.drawable.clubbing));
        categoryDrawables.put(CategoryType.HIKING, getDrawable(R.drawable.hiking));
        categoryDrawables.put(CategoryType.JOGGING, getDrawable(R.drawable.jogging));
        categoryDrawables.put(CategoryType.MUSIC, getDrawable(R.drawable.music));
        categoryDrawables.put(CategoryType.SNUGGLING, getDrawable(R.drawable.snuggling));
        categoryDrawables.put(CategoryType.SWIMMING, getDrawable(R.drawable.androidmarker));
    }

    private void initializeOverlays() {
        myLocOverlay = new MyLocationOverlay(this, mapView);
        myLocOverlay.enableMyLocation();


        Drawable androidMarker = getDrawable(R.drawable.androidmarker);
        itemizedoverlay = new PartyItemizedOverlay(androidMarker, FindPartiesMapActivity.this) {
            @Override
            protected boolean onTap(int index) {
                final PartyOverlayItem item = this.mOverlays.get(index);

                if(item != null) {
                    final Party party = item.getParty();

                    DialogFragment fragment = new DialogFragment() {
                        @Override
                        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                            View view = inflater.inflate(R.layout.party_overlay_info, container);
                            getDialog().setTitle(party.getCategory());
                            ImageView imageView = (ImageView)view.findViewById(R.id.imageView);
                            imageView.setImageResource(CategoryType.valueOf(party.getCategory()).getDrawableId());

                            Bitmap userPictureBitmap = getUserPictureBitmap();

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
                            TextView experience = (TextView)view.findViewById(R.id.experienceValue);
                            experience.setText(party.getLevel());


                            return view;
                        }
                    };
                    fragment.show(getFragmentManager(),"partyOverlayInfo");


                return true;
                }


                return super.onTap(index);    //To change body of overridden methods use File | Settings | File Templates.
            }
        };
        itemizedoverlay.populateNow();
        List<Overlay> overlays = mapView.getOverlays();
        overlays.add(myLocOverlay);
        overlays.add(itemizedoverlay);
    }

    private Bitmap getUserPictureBitmap() {
        Picture picture = getPartyManagerApplication().getUserFromBackend().getPicture();
        if(picture != null){
            return BitmapFactory.decodeByteArray(picture.getContent(), 0, picture.getContent().length);
        }
        return null;
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
                //mapView.invalidate();
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

                    List<Party> parties = getPartyManagerApplication().getPartyService().searchParties(mapLongitude, mapLatitude, SEARCH_DISTANCE);

                    Log.d("FindPartiesMapActivity", parties.size() + " parties found in " + SEARCH_DISTANCE + " km area: " + parties.toString());

                    for (Party party : parties) {
                        if (itemizedoverlay.containsParty(party.getId())) {
                            continue;
                        }

                        GeoPoint partyLocation = new GeoPoint(getMicroDegrees(party.getLocation().getLat()), getMicroDegrees(party.getLocation().getLon().doubleValue()));

                        PartyOverlayItem overlayitem = new PartyOverlayItem(partyLocation, null, null, party);


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


    @Override
    protected void onPause() {
        myLocOverlay.disableMyLocation();
        super.onPause();    //To change body of overridden methods use File | Settings | File Templates.
    }
}

