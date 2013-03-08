package com.comsysto.dalli.android.application;

import android.app.Application;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import com.comsysto.dalli.android.service.PartyManagementServiceImpl;
import com.comsysto.dalli.android.service.PartyManagementServiceMock;
import com.comsysto.dalli.android.model.CategoryType;
import com.comsysto.findparty.Party;
import com.comsysto.findparty.User;
import com.comsysto.findparty.web.PartyService;

import java.util.List;

/**
 * {@link PartyManagerApplication} holds relevant stuff for the whole app .
 * 
 * Currently:
 * <ul>
 * 	<li>caches the Tasks</li>
 * 	<li>holds the currently selectedParty (e.g. when editing a Task)</li>
 *  <li>delegates update,get,create,delete calls to the {@link com.comsysto.dalli.android.service.PartyManagementService}</li>
 *  <li>whether online/offline selects a corresponding {@link com.comsysto.dalli.android.service.PartyManagementService}</li>
 * 
 * @author stefandjurasic
 *
 */
public class PartyManagerApplication extends Application {

    private static final String CLOUD_HOST =  "snuggle.eu01.aws.af.cm";
    private static final String LOCAL_EMULATOR = "10.0.2.2:8080";
    private static final String LOCAL_TIM = "192.168.178.62:8080";
    private static final String LOCAL_ROB = "192.168.1.169:8080";
    private static final String LOCAL_STEFAN = "192.168.178.69:8080";
    private static final String TAG = Constants.LOG_APP_PREFIX + PartyManagerApplication.class.getSimpleName();

    private Party selectedParty;

	private PartyService partyService;

	private boolean ready;
	
	private User user;

	@Override
	public void onCreate() {
		initializeService();
	}

	public void initializeService() {
        Log.d(TAG, "initializing application");
		this.ready = false;
		if (isConnected()) {
			initializeOnlineService(LOCAL_STEFAN);
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

	public void createParty(Party newParty) {
		this.partyService.createParty(newParty);
	}


	public List<Party> getParties() {
		return this.partyService.getAllParties(user.getUsername());
	}

	public void deleteParty(Party party) {
		this.partyService.delete(party.getId());
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

	public boolean isReady() {
		return ready;
	}

	public void saveParty(Party updatedParty) {
		this.partyService.update(updatedParty);
	}

	public void setSelectedParty(Party selectedParty) {
		this.selectedParty = selectedParty;
	}

	public Party getSelectedParty() {
		return selectedParty;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public User createAccount(String userName, String password) {
        return this.partyService.createUser(userName, password);
	}

    public List<Party> searchParties(Double longitude, Double latitude, Double maxDistance) {
        return this.partyService.searchParties(longitude, latitude, maxDistance);
    }

    public List<String> getAllCategories() {
        return CategoryType.names();

    }

    public boolean authenticate(String username, String password) {
        Log.d(TAG, "authenticating username/password: " + username + "/" + password);
        User user = partyService.getUser(username);
        if(user!=null && user.getPassword() != null && user.getPassword().equals(password)) {
            Log.d(TAG, "user successfully authenticated: " + user);
            return true;
        }
        Log.d(TAG, "authentication failed!");
        return false;
    }
}
