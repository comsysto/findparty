package com.comsysto.findbuddies.android.service.async;

/**
 * Created with IntelliJ IDEA.
 * User: stefandjurasic
 * Date: 24.11.13
 * Time: 12:41
 * To change this template use File | Settings | File Templates.
 */
public interface PartyCallback {
    public void success(String partyId);
    public void error();
}
