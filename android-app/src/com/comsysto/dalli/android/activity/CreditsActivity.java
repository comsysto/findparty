package com.comsysto.dalli.android.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.comsysto.dalli.android.R;
import com.comsysto.dalli.android.menu.OptionMenuHandler;

/**
 * Shows comSysto Credits :)
 * 
 * @author stefandjurasic
 * @author Peter Dmytrasz
 *
 */
public class CreditsActivity extends AbstractActivity {

	private OptionMenuHandler optionMenuHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.optionMenuHandler = new OptionMenuHandler(this);
		
		setContentView(R.layout.credits);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return this.optionMenuHandler.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		this.optionMenuHandler.onOptionsItemSelected(item);
		return super.onOptionsItemSelected(item);
	}		
}
