package com.comsysto.findparty.web;

import com.comsysto.findparty.Party;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface PartyController {

	public List<Party> search(Double lon, Double lat, Double maxdistance, HttpServletRequest request);
	
	public String create(Party party);

	public void subscribe(String partyId, String action, String username);

	public Party show(String partyId);
	
	public List<Party> findByUsername(String username);
	
	public void update(Party party, String partyId);

	public void delete(String partyId);

}
