package com.comsysto.findparty.web;

import java.util.List;

import com.comsysto.findparty.Party;

public interface PartyController {

	public List<Party> search(Double lon, Double lat, Double maxdistance);
	
	public String create(Party party);

	public void subscribe(String partyId, String action, String username);

	public void show(String partyId);
	
	public List<Party> findByUsername(String username);
	
	public void update(Party party);

	public void delete(String partyId);

}
