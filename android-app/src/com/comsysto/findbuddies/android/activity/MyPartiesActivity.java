package com.comsysto.findbuddies.android.activity;

import android.os.Bundle;
import android.util.Log;
import com.comsysto.findbuddies.android.R;
import com.comsysto.findparty.User;

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
