package com.comsysto.dalli.android.menu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import com.comsysto.dalli.android.R;
import com.comsysto.dalli.android.activity.CreatePartyActivity;
import com.comsysto.dalli.android.activity.CreditsActivity;
import com.comsysto.dalli.android.activity.Preferences;
import com.comsysto.dalli.android.application.PartyManagerApplication;

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
		inflater.inflate(com.comsysto.dalli.android.R.menu.quick_menu, menu);
		return true;
	}
	
	public void onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
//		case R.id.preferences_menu_item:
//			goTo(activity, Preferences.class);
//			break;
		case R.id.credits:
			goTo(activity, CreditsActivity.class);
			break;
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
