package com.comsysto.dalli.android.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.comsysto.dalli.android.R;
import com.comsysto.dalli.android.menu.OptionMenuHandler;

/**
 * Displays the SplashScreen from where the user can navigate to FocusPage,
 * ReviewPage, GroupViewPage and AdminPage.
 * 
 * @author stefandjurasic
 *
 */
public class SplashScreenActivity extends AbstractActivity {

	private OptionMenuHandler optionMenuHandler;
	private ProgressDialog dialog;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.optionMenuHandler = new OptionMenuHandler(this);

		setContentView(R.layout.main);

		Button findPartyButton = (Button) findViewById(R.id.FIND_PARTY_BUTTON);
		Button createNewPartyButton = (Button) findViewById(R.id.CREATE_NEW_PARTY_BUTTON);
        Button myPartiesButton = (Button) findViewById(R.id.MY_PARTIES_BUTTON);
        Button manageUserPicture = (Button) findViewById(R.id.MANAGE_USER_PICTURE_BUTTON);


        findPartyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getPartyManagerApplication().isReady()) {
                    goTo(SplashScreenActivity.this, FindPartiesMapActivity.class);
                } else {
                    SplashScreenActivity.this.showDialog(5);
                }
            }
        });

        myPartiesButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (getPartyManagerApplication().isReady()) {
                    goTo(SplashScreenActivity.this, MyPartiesActivity.class);
                } else {
                    SplashScreenActivity.this.showDialog(5);
                }
            }
        });

		createNewPartyButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (getPartyManagerApplication().isReady()) {
                    goTo(SplashScreenActivity.this, CreatePartyActivity.class);
                } else {
                    SplashScreenActivity.this.showDialog(5);
                }
            }
        });

        manageUserPicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (getPartyManagerApplication().isReady()) {
                    goTo(SplashScreenActivity.this, ManageUserPictureDialogActivity.class);
                } else {
                    SplashScreenActivity.this.showDialog(5);
                }
            }
        });

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading. Please wait...");
	}

	private void showProgressDialogIfNotReady() {
		if (!dialog.isShowing() && !getPartyManagerApplication().isReady()) {
			dialog.show();
			new AsyncTask<Void, Void, Void>() {
				
				@Override
				protected Void doInBackground(Void... params) {
					while (!getPartyManagerApplication().isReady()) {
						try{
                            Thread.sleep(10);
                            } catch (InterruptedException e) {
                                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                     }
					return null;
				}
				
				protected void onPostExecute(Void result) {
                    dialog.hide();
				};
				
			}.execute();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		showProgressDialogIfNotReady();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Log.i("SplashScreenActivity", "onCreatDialog called");

		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle("Splash Screeen Button clicked");
		alertDialog
				.setMessage("inital text - needed or you can't change it later in onprepare.... bug???");
		return alertDialog;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		Log.i("SplashScreenActivity", "onPrepareDialog called");

		switch (id) {
		case 5:
			((AlertDialog) dialog).setTitle("Service not ready");
			((AlertDialog) dialog).setMessage("Please wait until backend is initialized");
			break;			
		default:
			throw new IllegalStateException();
		}
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

    @Override
    protected void onDestroy() {
        dialog.dismiss();
        super.onDestroy();    //To change body of overridden methods use File | Settings | File Templates.
    }
}