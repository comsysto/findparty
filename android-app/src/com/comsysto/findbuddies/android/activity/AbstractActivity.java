package com.comsysto.findbuddies.android.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.comsysto.dalli.android.R;
import com.comsysto.findbuddies.android.application.PartyManagerApplication;
import com.comsysto.findbuddies.android.account.AccountAuthenticator;
import com.comsysto.findbuddies.android.menu.OptionMenuHandler;

/**
 * Abstract activity for convenient methods.
 * 
 * @author stefandjurasic
 *
 */
public abstract class AbstractActivity extends Activity {

    private OptionMenuHandler optionMenuHandler;

    /**
	 * goes to another activity
	 * 
	 * @param Activity coming from
	 * @param Class going to, careful must be an Activity!
	 */
	void goTo(Activity from, Class<?> to) {
		Intent intent = new Intent(from, to);
        //intent.putExtra("activity", "createActivity");
		startActivity(intent);
	}

    void goToTop(Activity from) {
        Intent intent = new Intent(from, MyPartiesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        disableTitleInActionBar();
        this.optionMenuHandler = new OptionMenuHandler(this);



    }

    /**
	 * returns the {@link com.comsysto.findbuddies.android.application.PartyManagerApplication}
	 * @return instance of {@link com.comsysto.findbuddies.android.application.PartyManagerApplication}
	 */
	PartyManagerApplication getPartyManagerApplication() {
		return (PartyManagerApplication) getApplication();
	}

	@Override
	public void onResume() {
		super.onResume();
		AccountManager mAccountManager = AccountManager.get(this);
		Account[] accountsByType = mAccountManager.getAccountsByType(AccountAuthenticator.AUTH_TYPE);
		if (accountsByType.length == 0) {
			if (isTaskRoot()) {
				Intent intent = new Intent(this, StartActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
			finish();
		}
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.optionMenuHandler.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.optionMenuHandler.onOptionsItemSelected(item);
        return super.onOptionsItemSelected(item);
    }


    private void disableTitleInActionBar() {
        ActionBar ab = getActionBar();

        if (ab != null) {
            ab.setDisplayShowTitleEnabled(false);
        }

    }
	
}
