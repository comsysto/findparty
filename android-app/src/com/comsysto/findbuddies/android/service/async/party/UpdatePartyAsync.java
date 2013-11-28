package com.comsysto.findbuddies.android.service.async.party;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import com.comsysto.findbuddies.android.application.PartyManagerApplication;
import com.comsysto.findparty.Party;

/**
 * Created with IntelliJ IDEA.
 * User: stefandjurasic
 * Date: 24.11.13
 * Time: 12:44
 * To change this template use File | Settings | File Templates.
 */
public class UpdatePartyAsync extends AsyncTask<Party, Void, String> {

    private final UpdatePartyCallback callback;
    private final Bitmap bitmap;

    public UpdatePartyAsync(UpdatePartyCallback callback, Bitmap bitmap) {
        this.callback = callback;
        this.bitmap = bitmap;
    }

    @Override
    protected String doInBackground(Party... params) {
        if (params == null || params.length != 1) {
            throw new IllegalArgumentException("You have to pass one Party. Actually passed: " + params);
        }
        PartyManagerApplication partyManagerApplication = PartyManagerApplication.getInstance();
        Party toBeUpdatedParty = params[0];
        if (bitmap != null) {
            String pictureUrl = uploadBitmapFirst(bitmap);
            if (pictureUrl == null) {
                return null;
            }
            toBeUpdatedParty.setPictureUrl(pictureUrl);
        }
        String partyId = submit(partyManagerApplication, toBeUpdatedParty);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            //////WAHHHHHHHH
        }
        return partyId;
    }

    private String submit(PartyManagerApplication partyManagerApplication, Party toBeUpdatedParty) {
        switch(this.callback.getUpdatePartyAsyncMode()) {
            case CREATE:
                return partyManagerApplication.createParty(toBeUpdatedParty);
            case UPDATE:
                partyManagerApplication.update(toBeUpdatedParty);
                return toBeUpdatedParty.getId();
            case DELETE:
                partyManagerApplication.deleteParty(toBeUpdatedParty.getId());
                return toBeUpdatedParty.getId();
            default:
                throw new IllegalArgumentException("UNKNOWN MODE");
        }
    }

    private String uploadBitmapFirst(Bitmap bitmap) {
        return PartyManagerApplication.getInstance().uploadPicture(bitmap);
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
