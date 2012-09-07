package com.comsysto.dalli.android.service;

import com.comsysto.findparty.Party;
import com.comsysto.findparty.web.PartyService;

import java.util.ArrayList;
import java.util.List;


/**
 * Current mock implementation. In future we need a offline implementation of this service,
 * returning Tasks from a local storage when offline.
 * 
 * @author stefandjurasic
 *
 */
public class PartyManagementServiceMock implements PartyService {

	List<Party> parties = new ArrayList<Party>();

	public PartyManagementServiceMock() {
        getAllParties("asdf");
	}


    @Override
    public void cancelParty(String partyId, String username) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public String createParty(Party party) {
        parties.add(party);
        return "newPartyId123";
    }

    @Override
    public void delete(String partyId) {
        for (Party party : parties) {
            if(party.getId()==partyId)
                parties.remove(party);
        }
    }

    @Override
    public List<Party> getAllParties(String username) {
        List<Party> userParties = new ArrayList<Party>();
        for (Party party : parties) {
            if(party.getOwner().equals(username))
                userParties.add(party);
        }
        return userParties;
    }

    @Override
    public void joinParty(String partyId, String username) {
        for (Party party : parties) {
            if(party.getId()==partyId)
                party.getCandidates().add(username);
        }
        
    }

    @Override
    public List<Party> searchParties(Double lon, Double lat) {
        return new ArrayList<Party>();
    }

    @Override
    public Party showDetails(String partyId) {
        for (Party party : parties) {
            if(party.getId()==partyId)
                return party;
        }
        return null;
    }

    @Override
    public void update(Party party) {
        delete(party.getId());
        parties.add(party);
    }


    @Override
    public String echo(String input) {
        return input;
    }

}
