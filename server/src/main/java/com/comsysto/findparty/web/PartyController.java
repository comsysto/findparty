package com.comsysto.findparty.web;

import java.util.Set;

import com.comsysto.findparty.Party;

public interface PartyController {


	public Set<Party> searchParties(Double lon, Double lat, Double maxdistance);
	
	public void createParty(Party party);

	public void cancelParty(String username, String partyId);

	public abstract void showDetails(String partyId);

	public abstract void joinParty(String partyId, String username);

}
