package com.comsysto.dalli.android.service;

import com.comsysto.findparty.Point;

/**
 * Created with IntelliJ IDEA.
 * User: stefandjurasic
 * Date: 21.11.12
 * Time: 11:45
 * To change this template use File | Settings | File Templates.
 */
public interface LocationRequester {

    public void updateLocationPoint(Point point, String address);
    public void updateLocationAddress(String address);
}
