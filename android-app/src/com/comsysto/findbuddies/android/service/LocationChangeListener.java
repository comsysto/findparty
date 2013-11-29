package com.comsysto.findbuddies.android.service;

import android.content.Context;
import android.location.*;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Observable;


/**
 *
 * @author elbatya
 * @author crusi
 */
public class LocationChangeListener extends Observable implements LocationListener {

    private final Geocoder geocoderEnglish;
    private final Geocoder geocoderSystemLanguage;
    private LocationManager locationManager;
    private boolean isActive;
    private final Criteria accuracyCoarseCriteria;
    private final Criteria accuracyFineCriteria;

    public LocationChangeListener(Context con, LocationManager locationManager) {
        // Log.d("LocationChangeListener","Constructor");

        geocoderEnglish = new Geocoder(con, Locale.ENGLISH);
        geocoderSystemLanguage = new Geocoder(con);

        this.locationManager = locationManager;

        accuracyCoarseCriteria = new Criteria();
        accuracyCoarseCriteria.setAccuracy(Criteria.ACCURACY_COARSE);
        accuracyFineCriteria = new Criteria();
        accuracyFineCriteria.setAccuracy(Criteria.ACCURACY_FINE);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // Log.d("LocationChangeListener","onStatusChanged"+status);
    }

    @Override
    public void onProviderEnabled(String provider) {
        // Log.d("LocationChangeListener","onProviderEnabled");
    }

    @Override
    public void onProviderDisabled(String provider) {
        // Log.d("LocationChangeListener","onProviderDisabled");

    }

    @Override
    public void onLocationChanged(Location location) {
        // Log.d("LocListener", "onLocationChanged");
        extractRelevantLocationAndInformObserver(location);
    }

    /**
     * @param location
     */
    private void extractRelevantLocationAndInformObserver(final Location location) {

            new AsyncTask<Void, Void, LocationInfo>() {

                @Override
                protected void onPostExecute(LocationInfo locationInfo) {
                    if (locationInfo != null && locationInfo.getLocationInEnglish() != null
                            && locationInfo.getLocationInSystemLanguage() != null) {
                        notifyObservers(locationInfo);
                    }

                }

                @Override
                    protected LocationInfo doInBackground(Void... params) {
                        try {
                            final LocationInfo locationInfo = new LocationInfo();

                            List<Address> result = geocoderEnglish.getFromLocation(location.getLatitude(), location
                                    .getLongitude(), 1);

                            if (result.size() > 0) {
                                locationInfo.setLocationInEnglish(result.get(0));

                                String country = result.get(0).getCountryName();
                                String postalCode = result.get(0).getPostalCode();
                                String loString = postalCode + "-" + country;

                                if (loString.length() > 0) {
                                    // Log.d("LocationChangeListener: ",
                                    // "Notifiying observers about " + loString);
                                    setChanged();
                                }
                            }

                            result = geocoderSystemLanguage.getFromLocation(location.getLatitude(), location.getLongitude(),
                                    1);

                            if (result.size() > 0) {
                                locationInfo.setLocationInSystemLanguage(result.get(0));
                                locationInfo.setLocationStringForParty(result.get(0).getAddressLine(0));
                                locationInfo.setLongitude(result.get(0).getLongitude());
                                locationInfo.setLatitude(result.get(0).getLatitude());
                            }
                            return locationInfo;
                        } catch (IOException e) {
                            Log.e("LocListener", "error", e);
                            return null;
                        }
                    }
            }.execute();
    }

    /**
     * Deactivates this LocationChangeListener. This means that no more
     * locations updates are requested. If the this LocationChangeListener was
     * already deactivated, nothing will happen.
     */
    public synchronized void deactivate() {
        if (isActive) {
            locationManager.removeUpdates(this);
        }
        isActive = false;
    }

    /**
     * Activates this LocationChangeListener. This means that a location update
     * is requested every 15 minutes. If the this LocationChangeListener was
     * already active, it will be first deactivated and then reactivated.
     */
    public synchronized void activate() {
        deactivate();
        String provider = locationManager.getBestProvider(accuracyCoarseCriteria, true);

        locationManager.requestSingleUpdate(provider, this, null);
        isActive = true;


    }

    /**
     * We try to initialize the last position from GPS and Network Provider.
     *
     * <ol>
     * <li>NetworkProvider is accurate enough, so if we get a location from him
     * and he is active -> take it</li>
     * <li>We try only to aquire the GPS Provider location over the
     * NetworkProvider location if</li>
     * <ul>
     * <li>location from networkprovider is null</li>
     * <li>location from networkprovider is available but the provider is
     * disabled and the gps provider is active</li>
     * </ul>
     * </ol>
     */
    public void initializeFromHistory() {

        Location lastKnownLocation = getLastKnownLocation();

        if(lastKnownLocation!=null) {
            extractRelevantLocationAndInformObserver(lastKnownLocation);
        } else {
            Log.i("LocationChangeListener", "no last known location found");
        }

    }

    private Location getLastKnownLocation() {
        Location lastKnownLocation = null;
        String fineProvider = locationManager.getBestProvider(accuracyFineCriteria, true);
        String coarseProvider = locationManager.getBestProvider(accuracyCoarseCriteria, true);

        if(fineProvider!=null) {
            lastKnownLocation = locationManager.getLastKnownLocation(fineProvider);
        } else if(coarseProvider!=null && lastKnownLocation==null) {
            lastKnownLocation = locationManager.getLastKnownLocation(coarseProvider);
        }
        return lastKnownLocation;
    }

}
