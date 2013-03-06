package com.comsysto.dalli.android.service;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.util.Log;
import com.comsysto.findparty.Point;

import java.io.IOException;
import java.util.List;
import java.util.Observer;

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

            LocationManager locationManager = (LocationManager)context.getSystemService( Context.LOCATION_SERVICE );

            locationListener = new LocationChangeListener(context, locationManager);

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

    public void requestLocationFromPoint(final Point location, final LocationRequester locationRequester) {
        //TODO: the geocoder should be part of the LocationService and not of the listener

        AsyncTask<Void, Void, Address> asyncTask = new AsyncTask<Void, Void, Address>() {
            @Override
            protected Address doInBackground(Void... params) {
                Geocoder geocoderSystemLanguage = new Geocoder(context);
                List<Address> addresses = null;
                try {
                    addresses = geocoderSystemLanguage.getFromLocation(location.getLat(), location.getLon(), 1);
                } catch (IOException e) {
                    Log.e("LocationService", "Error getting the name of the location", e);
                    return null;
                }
                if (!addresses.isEmpty()) {
                    return addresses.get(0);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Address address) {
                if (address == null) {
                    locationRequester.updateLocationAddress("Error getting the name of the GPS.");
                } else {
                    informLocationRequester(address, locationRequester);
                }
            }
        };

        asyncTask.execute();

    }

    public void requestPointFromAddress(final String address, final LocationRequester locationRequester) {
        //TODO: the geocoder should be part of the LocationService and not of the listener
        AsyncTask<Void, Void, Address> asyncTask = new AsyncTask<Void, Void, Address>() {
            @Override
            protected Address doInBackground(Void... params) {
                Geocoder geocoderSystemLanguage = new Geocoder(context);
                List<Address> addresses = null;
                try {
                    addresses = geocoderSystemLanguage.getFromLocationName(address, 1);
                } catch (IOException e) {
                    Log.e("LocationService", "Error getting the name of the location", e);
                    return null;
                }
                if (!addresses.isEmpty()) {
                    return addresses.get(0);
                }
                return null;            }

            @Override
            protected void onPostExecute(Address address) {
                if (address == null) {
                    locationRequester.updateLocationAddress("Error getting the name of the location");
                } else {
                    informLocationRequester(address, locationRequester);
                }
            }
        };

        asyncTask.execute();
    }

    private void informLocationRequester(Address address, LocationRequester locationRequester) {
        Point point = new Point();
        point.setLat(address.getLatitude());
        point.setLon(address.getLongitude());
        locationRequester.updateLocationPoint(point, address.getAddressLine(0));
    }
}
