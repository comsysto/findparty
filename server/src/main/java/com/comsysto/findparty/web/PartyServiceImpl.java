package com.comsysto.findparty.web;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.comsysto.findparty.Party;

@Component
public class PartyServiceImpl implements PartyService {

	@Autowired
	@Qualifier(value="partiesMongoOperations")
	private MongoTemplate partiesMongoTemplate;
	
	@Override
	public Set<Party> searchParties(Double lon, Double lat) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Party showDetails(String partyId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void cancelParty(String partyId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void joinParty(String username, String partyId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createParty(Party party) {
		partiesMongoTemplate.save(party, "party");
	}

}
