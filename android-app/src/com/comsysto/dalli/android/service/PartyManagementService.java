package com.comsysto.dalli.android.service;

import com.comsysto.findparty.Party;

import java.util.List;

/**
 * {@link PartyManagementService} containing all relevant methods
 * for the app to interact with the backend.
 * 
 * @author Stefan Djurasic
 *
 */
public interface PartyManagementService {//extends UserManagementService{
	
	Party createParty(String userName, Party newParty);
	
	List<Party> getAllPartiesFor(String userName);
	
	void saveParty(String userName, Party party);
	
	void deleteParty(String userName, String id);

	String echo(String echo);
}