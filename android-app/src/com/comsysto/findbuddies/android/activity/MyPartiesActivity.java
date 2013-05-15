package com.comsysto.findbuddies.android.activity;

import android.os.Bundle;
import com.comsysto.findbuddies.android.R;

/**
 * Acitivty for displaying only Active Tasks!
 * 
 * 
 * @author stefandjurasic
 *
 */
public class MyPartiesActivity extends AbstractPartyListActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.my_parties);
		super.onCreate(savedInstanceState);
	}

    @Override
    public void onBackPressed(){
        moveTaskToBack(true);
    }

}
