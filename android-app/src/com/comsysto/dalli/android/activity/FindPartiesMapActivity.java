package com.comsysto.dalli.android.activity;

import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import com.comsysto.dalli.android.R;
import com.comsysto.dalli.android.application.PartyManagerApplication;
import com.comsysto.dalli.android.service.LocationInfo;
import com.comsysto.dalli.android.service.LocationService;
import com.comsysto.findparty.Party;
import com.google.android.maps.*;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created with IntelliJ IDEA.
 * User: stefandjurasic
 * Date: 07.09.12
 * Time: 13:42
 * To change this template use File | Settings | File Templates.
 */
public class FindPartiesMapActivity extends MapActivity implements Observer {

    private MapView mapView;
    private Double currentDistance = 10.;
    private LocationService locationService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_parties_map);

        mapView = (MapView) findViewById(R.id.find_parties_map_view);
        mapView.setBuiltInZoomControls(true);

        locationService = new LocationService(getApplicationContext(), this);
        locationService.activate();
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

    @Override
    public void update(Observable observable, Object o) {
        LocationInfo locationInfo = (LocationInfo)o;
        locationService.deactivate();

        List<Party> parties = getPartyManagerApplication().searchParties(locationInfo.getLongitude(), locationInfo.getLatitude(), this.currentDistance);




        List<Overlay> mapOverlays = mapView.getOverlays();
        Drawable drawable = this.getResources().getDrawable(R.drawable.androidmarker);

        for (Party party : parties) {
            PartyItemizedOverlay itemizedoverlay = null;
            if (party.getPicture() != null && party.getPicture().getContent() != null)  {
                Drawable image = new BitmapDrawable(BitmapFactory.decodeByteArray(party.getPicture().getContent(), 0, party.getPicture().getContent().length));
                itemizedoverlay = new PartyItemizedOverlay(image, this);
            } else {
                itemizedoverlay = new PartyItemizedOverlay(drawable, this);
            }


            GeoPoint currentLocation = new GeoPoint(getMicroDegrees(party.getLocation().getLat()), getMicroDegrees(party.getLocation().getLon().doubleValue()));

            OverlayItem overlayitem = new OverlayItem(currentLocation, party.getCategory(), party.getOwner() + " " + new SimpleDateFormat().format(party.getStartDate()));
            itemizedoverlay.addOverlay(overlayitem);
            mapOverlays.add(itemizedoverlay);
        }


        final MyLocationOverlay myLocOverlay = new MyLocationOverlay(this, mapView);
        myLocOverlay.enableMyLocation();
        mapView.getOverlays().add(myLocOverlay);


        myLocOverlay.runOnFirstFix(new Runnable() {
            public void run() {
                mapView.getController().setZoom(15);
                mapView.getController().animateTo(myLocOverlay.getMyLocation());
            }
        });
    }

    private int getMicroDegrees(Double coordinate) {
        return (int)(coordinate.doubleValue() * 1E6);
    }
}
