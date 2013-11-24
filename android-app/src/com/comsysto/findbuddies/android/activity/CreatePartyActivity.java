package com.comsysto.findbuddies.android.activity;

import com.comsysto.findbuddies.android.service.async.UpdateMode;
import com.comsysto.findparty.Party;

import java.util.Date;

/**
 * Activity for creating Parties
 * 
 * @author stefandjurasic
 *
 */
public class CreatePartyActivity extends PartyActivity {

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

    public UpdateMode getUpdateMode() {
        return UpdateMode.CREATE;
    }
}
