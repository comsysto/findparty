package com.comsysto.dalli.android.service;

import android.location.Address;

/**
 * POJO for holding all relevant location information, which are especially
 * needed for retrieving the weather informations.
 *
 * @author elbatya
 */
public class LocationInfo {

    /**
     * The locations {@link Address} localized to English.
     */
    Address locationInEnglish;

    /**
     * The locations {@link Address} localized to the current system language.
     */
    Address locationInSystemLanguage;

    /**
     * A location string, which is needed for Google's weather api. The format
     * is <code>postalCode + "-" + country</code>, for example: 81243-germany
     */
    String locationStringForParty;
    private double longitude;
    private double latitude;

    /**
     * Gets the locations {@link Address} localized to English.
     *
     * @return the locations {@link Address} localized to English
     */
    public Address getLocationInEnglish() {
        return locationInEnglish;
    }

    /**
     * Sets the locations {@link Address} localized to English.
     *
     * @param locationInEnglish
     *            the locations {@link Address} localized to English
     */
    public void setLocationInEnglish(Address locationInEnglish) {
        this.locationInEnglish = locationInEnglish;
    }

    /**
     * Gets the locations {@link Address} localized to the current system
     * language.
     *
     * @return the locations {@link Address} localized to the current system
     *         language
     */
    public Address getLocationInSystemLanguage() {
        return locationInSystemLanguage;
    }

    /**
     * Sets the locations {@link Address} localized to the current system
     * language.
     *
     * @param locationInSystemLanguage
     *            the locations {@link Address} localized to the current system
     *            language
     */
    public void setLocationInSystemLanguage(Address locationInSystemLanguage) {
        this.locationInSystemLanguage = locationInSystemLanguage;
    }

    /**
     * Gets a location string, which is needed for Google's weather api. The
     * format is <code>postalCode + "-" + country</code>, for example:
     * 81243-germany
     *
     * @return a location string, which is needed for Google's weather api
     */
    public String getLocationStringForParty() {
        return locationStringForParty;
    }

    /**
     * Sets a location string, which is needed for Google's weather api. The
     * format is <code>postalCode + "-" + country</code>, for example:
     * 81243-germany
     *
     * @param locationStringForParty
     *            a location string, which is needed for Google's weather api
     */
    public void setLocationStringForParty(String locationStringForParty) {
        this.locationStringForParty = locationStringForParty;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLatitude() {
        return latitude;
    }
}
