package com.comsysto.findbuddies.android.activity;

import com.comsysto.findbuddies.android.application.PartyManagerApplication;
import com.comsysto.findbuddies.android.model.LevelType;
import com.comsysto.findbuddies.android.service.async.party.UpdatePartyMode;
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
        party.setOwner(PartyManagerApplication.getInstance().getUsername());
        party.setPictureUrl(PartyManagerApplication.getInstance().getUserImageUrl());
        party.setCategory(getDefaultCategory());
        party.setSize(2);
        party.setStartDate(new Date());

        party.setLevel(LevelType.EVERYBODY.name());
        return party;
    }

    private String getDefaultCategory() {
        return getAllCategories().get(0);
    }

    @Override
    public UpdatePartyMode getUpdatePartyAsyncMode() {
        return UpdatePartyMode.CREATE;
    }
}
