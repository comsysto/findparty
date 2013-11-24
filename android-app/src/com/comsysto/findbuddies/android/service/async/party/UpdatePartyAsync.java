package com.comsysto.findbuddies.android.service.async.party;

import android.os.AsyncTask;
import com.comsysto.findbuddies.android.application.PartyManagerApplication;
import com.comsysto.findparty.Party;
import com.comsysto.findparty.web.PartyService;

/**
 * Created with IntelliJ IDEA.
 * User: stefandjurasic
 * Date: 24.11.13
 * Time: 12:44
 * To change this template use File | Settings | File Templates.
 */
public class UpdatePartyAsync extends AsyncTask<Party, Void, String> {

    private final UpdatePartyCallback callback;

    public UpdatePartyAsync(UpdatePartyCallback callback) {
        this.callback = callback;
    }

    @Override
    protected String doInBackground(Party... params) {
        if (params == null || params.length != 1) {
            throw new IllegalArgumentException("You have to pass one Party. Actually passed: " + params);
        }
        PartyService partyService = PartyManagerApplication.getInstance().getPartyService();
        Party toBeUpdatedParty = params[0];
        switch(this.callback.getUpdatePartyAsyncMode()) {
            case CREATE:
                return partyService.createParty(toBeUpdatedParty);
            case UPDATE:
                partyService.update(toBeUpdatedParty);
                return toBeUpdatedParty.getId();
            case DELETE:
                partyService.deleteParty(toBeUpdatedParty.getId());
                return toBeUpdatedParty.getId();
            default:
                throw new IllegalArgumentException("UNKNOWN MODE");
        }
    }

    @Override
    protected void onPostExecute(String partyId) {
        if (partyId != null) {
            callback.successOnPartyUpdate(partyId);
        } else {
            callback.errorOnPartyUpdate();
        }

    }
}
