package com.comsysto.findbuddies.android.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.comsysto.dalli.android.R;
import com.comsysto.findbuddies.android.activity.*;
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
        if (!getPartyManagerApplication().isReady()) {
            activity.showDialog(5);
        }
        switch (item.getItemId()) {
            case R.id.add_party_menu_item:
                goTo(activity, CreatePartyActivity.class);
                break;
            case R.id.serach_parties_menu_item:
                goTo(activity, FindPartiesMapActivity.class);
                break;
            case R.id.manage_picture_menu_item:
                goTo(activity, ManageUserPictureDialogActivity.class);
                break;
            case R.id.logout:
                getPartyManagerApplication().getAccountService().removeAll();
                goTo(activity, StartActivity.class);
                break;
            case R.id.credits:
                goTo(activity, CreditsActivity.class);
                break;
            case R.id.debug_new_map_api_menu_item:
                goTo(activity, BuddiesMapActivity.class);
                break;
            default:
                //do nothing
        }
	}
	
	void goTo(Context from, Class<?> to) {
		Intent intent = new Intent(from, to);
		activity.startActivity(intent);
	}	
	
	
/*	void showQuickInsert() {
		AlertDialog.Builder alert = new AlertDialog.Builder(activity).setTitle(
				"Quick Insert").setMessage("New Task:");

		// Set an EditText view to get user input
		final EditText input = new EditText(activity);
		alert.setView(input);

		alert.setPositiveButton(R.string.ADD_PARTY_BUTTON, null);
//				new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int whichButton) {
//						Task newTask = new Task(input.getText().toString());
//						getPartyManagerApplication().createParty(newTask);
//						//ugly hack to inform when new task is here
//						if (activity instanceof ListActivity) {
//							((AbstractPartyListActivity)activity).notifyDataSetChanged();
//						}
//					}
//				});

		alert.setNegativeButton(R.string.CANCEL_BUTTON, null);
		alert.setNeutralButton(R.string.EDIT_BUTTON,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						Intent intent = new Intent(activity,
								CreatePartyActivity.class);

						intent.putExtra("category", input.getText().toString());

						activity.startActivity(intent);
					}
				});
		alert.show();
	}	*/
	
	PartyManagerApplication getPartyManagerApplication() {
		return (PartyManagerApplication) activity.getApplication();
	}	
}
