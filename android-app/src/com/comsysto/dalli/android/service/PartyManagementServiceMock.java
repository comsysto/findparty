package com.comsysto.dalli.android.service;

import com.comsysto.findparty.Party;
import com.comsysto.findparty.User;
import com.comsysto.findparty.web.PartyService;

import java.util.*;


/**
 * Current mock implementation. In future we need a offline implementation of this service,
 * returning Tasks from a local storage when offline.
 * 
 * @author stefandjurasic
 *
 */
public class PartyManagementServiceMock implements PartyService {


	Map<String, Party> parties = new HashMap<String, Party>();

	public PartyManagementServiceMock() {

	}

    @Override
    public List<Party> searchParties(Double lon, Double lat, Double maxdistance) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Party showDetails(String partyId) {
        return parties.get(partyId);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void cancelParty(String partyId, String username) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void joinParty(String partyId, String username) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String createParty(Party party) {
        String s = UUID.randomUUID().toString();
        parties.put(s, party);
        return s;
    }

    @Override
    public void update(Party party) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Party> getAllParties(String username) {
        return new ArrayList<Party>(parties.values());  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void delete(String partyId) {
        parties.remove(partyId);
    }

    @Override
	public String echo(String echo) {
		return echo;
	}

    @Override
    public User createUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        return null;
    }

    @Override
    public User getUser(String username) {
        User user = new User();
        user.setUsername(username);
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        User user = new User();
        user.setUsername("mockedUser");
        user.setPassword("password");
        user.setId("1abcd");
        return Arrays.asList(user);
    }

}
