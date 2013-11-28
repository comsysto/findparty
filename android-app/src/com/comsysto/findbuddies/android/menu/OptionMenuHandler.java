package com.comsysto.findbuddies.android.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.comsysto.findbuddies.android.R;
import com.comsysto.findbuddies.android.activity.*;
import com.comsysto.findbuddies.android.activity.masterdetail.PartyListActivity;
import com.comsysto.findbuddies.android.activity.signin.GoogleSigninActivity;
import com.comsysto.findbuddies.android.application.PartyManagerApplication;

/**
 * OptionMenu displayed when the button menu is pushed.
 * 
 * @author stefandjurasic
 *
 */
public class OptionMenuHandler {

	private Activity activity;

	public OptionMenuHandler(Activity activity) {
		this.activity = activity;
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = activity.getMenuInflater();
		inflater.inflate(R.menu.action_bar_menu, menu);
		return true;
	}
	
	public void onOptionsItemSelected(MenuItem item) {
//        if (!PartyManagerApplication.getInstance().isConnected()) {
//            activity.showDialog(5);
//        }
        switch (item.getItemId()) {
            case R.id.add_party_menu_item:
                goTo(activity, CreatePartyActivity.class);
                break;
            case R.id.search_parties_menu_item:
                goTo(activity, BuddiesMapActivity.class);
                break;
            case R.id.manage_picture_menu_item:
                goTo(activity, ManageUserPictureDialogActivity.class);
                break;
            case R.id.logout:
                PartyManagerApplication.getInstance().getAccountService().removeAll();
                Intent googlePlusLogout = new Intent(activity, GoogleSigninActivity.class);
                googlePlusLogout.setAction("GOOGLE_PLUS_LOGOUT");
                Log.i("CS_LOGOUT_OptionMenu", "removed device account");
                Log.i("CS_LOGOUT_OptionMenu", "setting logout action: GOOGLE_PLUS_LOGOUT");
                Log.i("CS_LOGOUT_OptionMenu", "starting Google Signin again to clear default GPlus account");
                activity.startActivity(googlePlusLogout);
                break;
            case R.id.credits:
                goTo(activity, CreditsActivity.class);
                break;
            case R.id.masterdetail:
                goTo(activity, PartyListActivity.class);

            default:
                //do nothing
        }
	}
	
	void goTo(Context from, Class<?> to) {
		Intent intent = new Intent(from, to);
		activity.startActivity(intent);
	}	

}
