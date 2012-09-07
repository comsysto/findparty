package com.comsysto.dalli.android.service;

import java.util.Observer;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;

public class LocationUpdater extends Activity {

    private LocationChangeListener locationListener;
    private Observer[] observers;

    private Context context;

    public LocationUpdater(Context context, Observer... observers) {
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

}
