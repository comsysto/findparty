package com.comsysto.dalli.android.activity;

import android.view.View;
import android.view.View.OnClickListener;
import com.comsysto.dalli.android.application.PartyManagerApplication;
import com.comsysto.findparty.Party;

import java.util.GregorianCalendar;

/**
 * Activity for creating Parties
 * 
 * @author stefandjurasic
 *
 */
public class CreatePartyActivity extends PartyActivity {

	@Override
	public OnClickListener getOnClickListener() {
		return new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Party newParty= new Party();
                newParty.setCategory(categoryNameText.getText().toString());
                newParty.setStartDate(calendar.getTime());
                newParty.setLevel((String) levelSpinner.getSelectedItem());
                newParty.setSize(numberOfParticipants);
                newParty.setOwner(((PartyManagerApplication)getApplication()).getUser().getUsername());
                getTaskManagerApplication().createParty(newParty);
                goToSplashScreen(CreatePartyActivity.this);
            }
		};
	}

    @Override
    protected void fillValues() {
        setTextOnNumberOfParticipantsButton(getNumberOfParticipants());
        this.calendar = GregorianCalendar.getInstance();
        setTimeOnView();
    }

    public String getCategory() {
		return (String) getIntent().getExtras().get("category");
	}
}
