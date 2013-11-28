package com.comsysto.findbuddies.android.activity.masterdetail;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import com.comsysto.findbuddies.android.adapter.PartyListAdapter;
import com.comsysto.findbuddies.android.service.async.party.SearchPartiesAsync;
import com.comsysto.findbuddies.android.service.async.party.SearchPartiesCallback;
import com.comsysto.findbuddies.android.widget.LoadingProgressDialog;
import com.comsysto.findparty.Party;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * A list fragment representing a list of Parties. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link PartyMapFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link com.comsysto.findbuddies.android.activity.masterdetail.PartyListFragment.Callbacks}
 * interface.
 */
public class PartyListFragment extends android.support.v4.app.ListFragment implements Observer, GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener, LocationListener, SearchPartiesCallback {

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;
    private LoadingProgressDialog dialog;
    private LocationClient locationClient;
    private Thread partiesLoadingThread;
    private List<Party> parties;

    @Override
    public void onConnected(Bundle bundle) {
        Location lastLocation = locationClient.getLastLocation();
        dismissDialog();
        if (lastLocation != null) {
            loadParties(lastLocation);
        } else {
            dialog = new LoadingProgressDialog(getActivity(), "Last known position not found. Trying to refresh position. Please wait...", true);
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setNumUpdates(1);
            locationClient.requestLocationUpdates(locationRequest, this);
        }


    }

    @Override
    public void onDisconnected() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onLocationChanged(Location location) {
        dismissDialog();
        loadParties(location);
    }

    public List<Party> getParties() {
        return parties;
    }

    @Override
    public void update(Observable observable, Object data) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void successOnSearchParties(List<Party> parties) {
        dismissDialog();
        this.parties = parties;
        setListAdapter(new PartyListAdapter(getActivity(), parties));
    }

    private void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

    @Override
    public void failureOnSearchParties() {
        dismissDialog();
        Toast.makeText(getActivity(), "Could not load Parties", Toast.LENGTH_LONG).show();
    }

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(Party party);
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PartyListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        triggerLocationUpdate();
    }

    private void triggerLocationUpdate() {
        dialog = new LoadingProgressDialog(getActivity(), "Aquiring last known position. Please wait...", true);
        locationClient = new LocationClient(getActivity(), this, this);
        locationClient.connect();
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = null;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        mCallbacks.onItemSelected(parties.get(position));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }

    private void loadParties(final Location location) {
        dialog = new LoadingProgressDialog(getActivity(), "Loading nearby Parties. Please wait...", true);
        new SearchPartiesAsync(this, location.getLongitude(),location.getLatitude()).execute();

    }
}
