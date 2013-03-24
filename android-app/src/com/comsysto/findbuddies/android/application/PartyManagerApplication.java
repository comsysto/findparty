package com.comsysto.findbuddies.android.application;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import com.comsysto.findbuddies.android.account.AccountService;
import com.comsysto.findbuddies.android.activity.StartActivity;
import com.comsysto.findbuddies.android.service.PartyManagementServiceImpl;
import com.comsysto.findbuddies.android.service.PartyManagementServiceMock;
import com.comsysto.findparty.Party;
import com.comsysto.findparty.Picture;
import com.comsysto.findparty.User;
import com.comsysto.findparty.web.PartyService;

import java.io.ByteArrayOutputStream;

/**
 * {@link PartyManagerApplication} holds relevant stuff for the whole app .
 * 
 * Currently:
 * <ul>
 * 	<li>caches the Tasks</li>
 * 	<li>holds the currently selectedParty (e.g. when editing a Task)</li>
 *  <li>delegates update,get,create,delete calls to the {@link com.comsysto.findbuddies.android.service.PartyManagementService}</li>
 *  <li>whether online/offline selects a corresponding {@link com.comsysto.findbuddies.android.service.PartyManagementService}</li>
 * 
 * @author stefandjurasic
 *
 */
public class PartyManagerApplication extends Application {

    private static final String CLOUD_HOST =  "snuggle.eu01.aws.af.cm";
    private static final String LOCAL_EMULATOR = "10.0.2.2:8080";
    private static final String LOCAL_STEFAN = "192.168.178.69:8080";
    private static final String LOCAL_ROB = "192.168.178.65:8080";
    private static final String TAG = Constants.LOG_APP_PREFIX + PartyManagerApplication.class.getSimpleName();

    private Party selectedParty;

	private PartyService partyService;

    private AccountService accountService;

	private boolean ready;

	@Override
	public void onCreate() {
		initializePartyService();
        intializeAccountService();
    }

    private void intializeAccountService() {
        accountService = new AccountService(this.getApplicationContext());
    }

    public void initializePartyService() {
        Log.d(TAG, "initializing application");
		this.ready = false;
		if (isConnected()) {
			initializeOnlineService(CLOUD_HOST);
		} else {
            //TODO: If no network connection available close the application with a hint!
            Log.d(TAG, "using Mock-Service");
			this.partyService = new PartyManagementServiceMock();
		}
	}

	private void initializeOnlineService(final String host) {
		AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				PartyManagerApplication.this.partyService = new PartyManagementServiceImpl(host, PartyManagerApplication.this);
				try {
					String echo = PartyManagerApplication.this.partyService.echo("echo");
					if (echo.equals("echo")) {
						Log.i(TAG, "Server-Check ["+host+"]: Server is online");
					} else {
						Log.e(TAG, "Server-Check ["+host+"]: Server returned wrong echo ("+ echo + "), going offline.");
					}
				} catch (Exception e) {
					Log.e(TAG, "Server-Check ["+host+"]: Server not reachable", e);

				}
				PartyManagerApplication.this.ready = true;
				return null;
			}
		};
		task.execute();
	}

    public boolean isReady() {
        return ready;
    }

	boolean isConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
		if (activeNetworkInfo != null && activeNetworkInfo.isAvailable()
				&& activeNetworkInfo.isConnected()) {
            Log.d(TAG, "device connected successfully to network");
			return true;
		} else {
            Log.d(TAG, "device not connected to network");
			return false;
		}
	}

    public User getUser() {
        String username = accountService.getUsername();
        return partyService.getUser(username);
    }

    public boolean authenticate(String username, String password) {
        Log.d(TAG, "authenticating username/password: " + username + "/" + password);
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        if(partyService.login(user)) {
            Log.d(TAG, "user successfully authenticated: " + user);
            accountService.createAccount(username, password);
            return true;
        }
        Log.d(TAG, "account failed!");
        return false;
    }

    public PartyService getPartyService() {
        return partyService;
    }

    public AccountService getAccountService() {
        return accountService;
    }

    public Party getSelectedParty() {
        return selectedParty;
    }

    public void setSelectedParty(Party selectedParty) {
        this.selectedParty = selectedParty;
    }

    public void saveUserPicture(Bitmap resizedBitmap) {
        User user = getUser();
        Picture picture = user.getPicture();
        if (picture == null) {
            picture = new Picture();
            user.setPicture(picture);
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        picture.setContent(stream.toByteArray());
        partyService.update(user);
    }

    public void deleteUserPicture() {
        User user = getUser();
        user.setPicture(null);
        partyService.update(user);
    }

    public void goToStart(Activity activity) {
        Intent intent = new Intent(activity, StartActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
        activity.finish();
    }

}
