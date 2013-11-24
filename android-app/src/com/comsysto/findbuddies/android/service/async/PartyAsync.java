package com.comsysto.findbuddies.android.service.async;

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
public class PartyAsync extends AsyncTask<Party, Void, String> {

    private final PartyCallback callback;
    private final UpdateMode mode;
    private boolean error;

    public PartyAsync(PartyCallback callback, UpdateMode mode) {
        this.callback = callback;
        if (mode == null) {
            throw new IllegalArgumentException("You have to pass a UpdateMode");
        }
        this.mode = mode;
    }

    @Override
    protected String doInBackground(Party... params) {
        if (params == null || params.length != 1) {
            throw new IllegalArgumentException("You have to pass one Party. Actually passed: " + params);
        }
        PartyService partyService = PartyManagerApplication.getInstance().getPartyService();
        if (UpdateMode.CREATE == mode) {
            return partyService.createParty(params[0]);
        } else if (UpdateMode.UPDATE == mode) {
            partyService.update(params[0]);
            return params[0].getId();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String partyId) {
        if (partyId != null) {
            callback.success(partyId);
        } else {
            callback.error();
        }

    }
}
