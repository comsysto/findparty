package com.comsysto.findbuddies.android.service.async.party;

import android.os.AsyncTask;
import android.util.Log;
import com.comsysto.findbuddies.android.activity.BuddiesMapActivity;
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
public class SearchPartiesAsync extends AsyncTask <Void, Void, List<Party>> {

    private static final Double SEARCH_DISTANCE = 20d;
    private SearchPartiesCallback callback;
    private final double longitude;
    private final double latitude;


    public SearchPartiesAsync(SearchPartiesCallback callback, double longitude, double latitude) {
        this.callback = callback;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @Override
    protected List<Party> doInBackground(Void... params) {
        Log.d("FindPartiesMapActivity", "Current map center lon/lat : " + longitude + " / " + latitude);
        return PartyManagerApplication.getInstance().searchParties(longitude, latitude, SEARCH_DISTANCE);

    }

    @Override
    protected void onPostExecute(List<Party> parties) {
        if (parties != null) {
            Log.d("FindPartiesMapActivity", parties.size() + " parties found in " + SEARCH_DISTANCE + " km area: " + parties);
            this.callback.successOnSearchParties(parties);
        } else {
            Log.e("FindPartiesMapActivity", "Returned parties were null but at least expected empty list!");
            this.callback.failureOnSearchParties();
        }
    }
}
