package com.comsysto.dalli.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.comsysto.dalli.android.R;
import com.comsysto.findparty.Party;

import java.util.GregorianCalendar;

/**
 * Activity for editing exisiting Tasks
 * 
 * @author stefandjurasic
 *
 */
public class EditPartyActivity extends PartyActivity {


	/**
	 * Initializes fields from existing Task
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.saveButton.setText(R.string.SAVE_TASK_BUTTON);
    }


	@Override
	public OnClickListener getOnClickListener() {
		return new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Party selectedParty = getTaskManagerApplication().getSelectedParty();
				selectedParty.setCategory(categoryNameText.getText().toString());
				selectedParty.setStartDate(calendar.getTime());
                selectedParty.setLevel((String)levelSpinner.getSelectedItem());
                selectedParty.setSize(numberOfParticipants);

				getTaskManagerApplication().saveParty(selectedParty);
				goToSplashScreen(EditPartyActivity.this);
			}
		};
	}		
	
	public String getCategory() {
        return getTaskManagerApplication().getSelectedParty().getCategory();
	}

    public int getLevelSpinnerSelectionPosition(String selectedLevel) {

        for (int i = 0; i< LEVELS.length; i++) {
            if (LEVELS[i].equals(selectedLevel)) {
                return i;
            }

        }
        return 0;
    }

    @Override
    protected void fillValues() {
        Party selectedParty = getTaskManagerApplication().getSelectedParty();
        setTextOnNumberOfParticipantsButton(selectedParty.getSize());
        this.numberOfParticipants = selectedParty.getSize();
        this.calendar = new GregorianCalendar();
        this.calendar.setTime(selectedParty.getStartDate());
        setTimeOnView();
        this.levelSpinner.setSelection(getLevelSpinnerSelectionPosition(selectedParty.getLevel()));
    }
}
