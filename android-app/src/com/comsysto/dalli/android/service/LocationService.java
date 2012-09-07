package com.comsysto.dalli.android.service;

import java.io.IOException;
import java.util.List;
import java.util.Observer;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.util.Log;
import com.comsysto.findparty.Point;

public class LocationService extends Activity {

    private LocationChangeListener locationListener;
    private Observer[] observers;

    private Context context;

    public LocationService(Context context, Observer... observers) {
        this.context = context;
        this.observers = observers;
    }

    public void activate() {
        if (locationListener == null) {
            locationListener = new LocationChangeListener(context, (LocationManager) context.getSystemService(LOCATION_SERVICE));

            if (observers != null) {
                for (Observer observer : observers) {
                    locationListener.addObserver(observer);
                }
            }

            locationListener.initializeFromHistory();

        }
        locationListener.activate();
    }

    public void deactivate() {
        locationListener.deactivate();

    }

    public String getLocationFromPoint(Point location) {
        //TODO: the geocoder should be part of the LocationService and not of the listener
        Geocoder geocoderSystemLanguage = new Geocoder(context);
        List<Address> addresses = null;
        try {
            addresses = geocoderSystemLanguage.getFromLocation(location.getLat(), location.getLon(), 1);
        } catch (IOException e) {
            Log.e("LocationService", "Error getting the name of the location", e);
            return "Error getting the name of the location";
        }
        return addresses.get(0).getAddressLine(0);  //To change body of created methods use File | Settings | File Templates.
    }
}
