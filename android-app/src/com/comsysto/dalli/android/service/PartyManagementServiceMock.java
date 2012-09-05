package com.comsysto.dalli.android.service;

import com.comsysto.findparty.Party;

import java.util.ArrayList;
import java.util.List;


/**
 * Current mock implementation. In future we need a offline implementation of this service,
 * returning Tasks from a local storage when offline.
 * 
 * @author stefandjurasic
 *
 */
public class PartyManagementServiceMock implements PartyManagementService {

	List<Party> tasks = new ArrayList<Party>();

	public PartyManagementServiceMock() {
        getAllPartiesFor("asdf");
	}

	@Override
	public Party createParty(String userName, Party newParty) {
		return newParty;
	}

	@Override
	public void deleteParty(String userName, String id) {
		//mockidimock, adapter removes, is enough
	}

	@Override
	public void saveParty(String userName, Party party) {
		// TODO Auto-generated method stub

	}

	@Override
	public String echo(String echo) {
		return echo;
	}

	@Override
	public List<Party> getAllPartiesFor(String userId) {
		// TODO Auto-generated method stub
		return tasks;
	}

//	@Override
//	public User createUser(User user, String password) {
//		return user;
//	}
//
//	@Override
//	public User login(String username, String password) throws AuthenticationException {
//		// TODO Auto-generated method stub
//		User user = new User();
//		user.setUsername(username);
//		return user;
//	}

}
