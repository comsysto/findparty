package com.comsysto.findbuddies.android.service.async.party;

/**
 * Created with IntelliJ IDEA.
 * User: stefandjurasic
 * Date: 24.11.13
 * Time: 12:41
 * To change this template use File | Settings | File Templates.
 */
public interface UpdatePartyCallback {
    public void successOnPartyUpdate(String partyId);
    public void errorOnPartyUpdate();
    public UpdatePartyMode getUpdatePartyAsyncMode();

}
