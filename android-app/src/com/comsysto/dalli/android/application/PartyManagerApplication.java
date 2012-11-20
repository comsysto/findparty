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

	private Party selectedParty;

	private PartyService partyService;

	private boolean ready;
	
	private User user;

	@Override
	public void onCreate() {
		initializeService();
	}

	public void initializeService() {
		this.ready = false;
		SharedPreferences defaultSharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
//		String host = defaultSharedPreferences.getString("host", "192.168.1.189");
		//String host = defaultSharedPreferences.getString("host", "snuggle.eu01.aws.af.cm");
		String host = "snuggle.eu01.aws.af.cm";
        //String host = "192.168.1.189:8080";
		if (isConnected()) {
			initializeOnlineService(host);
		} else {
            //TODO: If no network connection available close the application with a hint!
			this.partyService = new PartyManagementServiceMock();
		}
	}

	private void initializeOnlineService(final String host) {
		AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				PartyManagerApplication.this.partyService = new PartyManagementServiceImpl(host);
				try {
					String echo = PartyManagerApplication.this.partyService.echo("echo");
					if (echo.equals("echo")) {
						Log.i("Server Check", "Server is online");
					} else {
						Log.e("Server Check", "Server returned wrong echo ("+ echo + "), going offline.");
					}
				} catch (Exception e) {
					Log.e("Server Check", "Server not reachable", e);

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
			return true;
		} else {
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
		User user = new User();
		user.setUsername(userName);
        return user;
		//return this.partyManagementService.createUser(user, password);
	}

    public List<Party> searchParties(Double longitude, Double latitude, Double maxDistance) {
        return this.partyService.searchParties(longitude, latitude, maxDistance);
    }

    public List<String> getAllCategories() {
        return CategoryType.names();

    }
}
