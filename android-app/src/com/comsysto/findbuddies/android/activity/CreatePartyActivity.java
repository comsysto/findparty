package com.comsysto.findbuddies.android.activity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import com.comsysto.findbuddies.android.R;
import com.comsysto.findparty.Party;

import java.util.Date;

/**
 * Activity for creating Parties
 * 
 * @author stefandjurasic
 *
 */
public class CreatePartyActivity extends PartyActivity {


    @Override
    void submit() {
        getPartyManagerApplication().getPartyService().createParty(party);
    }

    public String getCategory() {
		return (String) getIntent().getExtras().get("category");
	}

    @Override
    protected Party getParty() {
        Party party = new Party();
        party.setOwner(getPartyManagerApplication().getUser().getUsername());
        party.setCategory(getDefaultCategory());
        party.setSize(2);
        party.setStartDate(new Date());

        party.setLevel(LEVELS[0]);
        return party;
    }

    private String getDefaultCategory() {
        return getAllCategories().get(0);
    }
}
