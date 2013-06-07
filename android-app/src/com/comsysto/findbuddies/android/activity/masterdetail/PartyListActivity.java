package com.comsysto.findbuddies.android.activity.masterdetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.comsysto.findbuddies.android.R;
import com.comsysto.findparty.Party;


/**
 * An activity representing a list of Parties. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link PartyMapActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link PartyListFragment} and the item details
 * (if present) is a {@link PartyMapFragment}.
 * <p>
 * This activity also implements the required
 * {@link PartyListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class PartyListActivity extends FragmentActivity
        implements PartyListFragment.Callbacks {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private PartyMapFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_list);

        if (findViewById(R.id.party_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((PartyListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.party_list))
                    .setActivateOnItemClick(true);

            fragment = new PartyMapFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.party_detail_container, fragment)
                    .commit();
        }

        // TODO: If exposing deep links into your app, handle intents here.
    }

    /**
     * Callback method from {@link PartyListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(Party party) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, PartyMapActivity.class);
            detailIntent.putExtra(PartyMapFragment.ARG_ITEM_ID, party);
            startActivity(detailIntent);
        }
    }
}
