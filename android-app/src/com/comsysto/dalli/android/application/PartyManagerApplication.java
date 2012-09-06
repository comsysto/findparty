package com.comsysto.dalli.android.application;

import android.app.Application;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import com.comsysto.dalli.android.service.PartyManagementService;
import com.comsysto.dalli.android.service.PartyManagementServiceMock;
import com.comsysto.findparty.Party;
import com.comsysto.findparty.User;

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

	private List<Party> parties;

	private Party selectedParty;

	private PartyManagementService partyManagementService;
	
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
		String host = defaultSharedPreferences.getString("host",
				"10.0.2.2:8080");
		if (isConnected()) {
			initializeOnlineService(host);
		} else {
			this.partyManagementService = new PartyManagementServiceMock();
		}
	}

	private void initializeOnlineService(final String host) {
		AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				PartyManagerApplication.this.partyManagementService = new PartyManagementServiceMock();
				try {
					String echo = PartyManagerApplication.this.partyManagementService
							.echo("echo");
					if (echo.equals("echo")) {
						Log.i("Server Check", "Server is online");
					} else {
						Log.e("Server Check", "Server returned wrong echo ("+ echo + "), going offline.");
						PartyManagerApplication.this.partyManagementService = new PartyManagementServiceMock();
					}
				} catch (Exception e) {
					Log.e("Server Check", "Server not reachable", e);
					PartyManagerApplication.this.partyManagementService = new PartyManagementServiceMock();
				}
				PartyManagerApplication.this.ready = true;
                PartyManagerApplication.this.parties = partyManagementService.getAllPartiesFor(user.getUsername());
				return null;
			}
		};
		task.execute();
	}

	public void createParty(Party newParty) {
		Party createdParty = this.partyManagementService.createParty(user.getUsername(), newParty);
		this.parties.add(createdParty);
	}

	public void loadParties() {
		this.parties = this.partyManagementService.getAllPartiesFor(user.getUsername());
	}
	
	public List<Party> getParties() {
		loadParties();
		return parties;
	}

	public void deleteParty(Party party) {
		this.partyManagementService.deleteParty(user.getUsername(), party.getId());
		this.parties.remove(party);
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
		this.partyManagementService.saveParty(user.getUsername(), updatedParty);
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

}
