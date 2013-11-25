package com.comsysto.findbuddies.android.service.async.party;

import android.os.AsyncTask;
import android.util.Log;
import com.comsysto.findbuddies.android.application.PartyManagerApplication;
import com.comsysto.findparty.Party;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: djurast
 * Date: 25.11.13
 * Time: 16:33
 *
 * @version $Id$
 */
public class GetUsersPartiesAsync extends AsyncTask <Void, Void, List<Party>> {

    private GetUsersPartiesCallback callback;
    private String userName;


    public GetUsersPartiesAsync(GetUsersPartiesCallback callback, String userName) {
        this.callback = callback;
        this.userName = userName;
    }

    @Override
    protected List<Party> doInBackground(Void... params) {
        List<Party> parties = PartyManagerApplication.getInstance().getAllParties(userName);
        Log.i(GetUsersPartiesAsync.class.getName(), "retrieved my parties from server: " + parties);
        return parties;
    }

    @Override
    protected void onPostExecute(List<Party> parties) {
        if (parties != null) {
            this.callback.successOnGetUsersParties(parties);
        } else {
            this.callback.failureOnGetUsersParties();
        }
    }
}
