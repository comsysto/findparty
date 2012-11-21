package com.comsysto.findparty.web;

import com.comsysto.findparty.Party;
import com.comsysto.findparty.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.geo.Point;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PartyServiceImpl implements PartyService {

    @Autowired
    public MongoOperations mongoOperations;
    
    public static final Double KILOMETER = 111.0d;

    /**
     * The Attribute that is used for the search for the location position
     */
    public static final String LOCATION = "location";


	@Override
	public Party showDetails(String partyId) {
		return findById(partyId);
	}

    private Party findById(String partyId) {
        Party party = mongoOperations.findById(partyId, Party.class);
        if(party==null)
            throw new NotFoundException("an existing party with id="+partyId+" was not found on server!");
        
        return party;
    }

    @Override
	public void cancelParty(String username, String partyId) {
        Party party = findById(partyId);
        cancelParty(username, party);
    }

    private void cancelParty(String username, Party party) {
        party.getParticipants().remove(username);
        party.getCandidates().remove(username);
        mongoOperations.save(party);
    }

    @Override
	public void joinParty(String username, String partyId) {
    	Party party = findById(partyId);
    	User user = findUserAndCreateIfNotExists(username);

    	party.getCandidates().add(user.getUsername());
    	mongoOperations.save(party);
	}

	
	@Override
	public void update(Party party) {
	    //checks if party with same id exists
	    findById(party.getId());
	    	    
	    mongoOperations.save(party);
	}
	
	@Override
	public String createParty(Party party) {
	    
		mongoOperations.insert(party);
        
		User user = findUserAndCreateIfNotExists(party.getOwner());
		
		return party.getId();
	}

    private User findUserAndCreateIfNotExists(String username) {
        Criteria criteria = Criteria.where("username").is(username);
        Query query = new Query(criteria );
        User user = mongoOperations.findOne(query , User.class);
        if(user!=null) {
            return user;
        }
        else {
            return createUser(username, "test1234");
        }
    }

    @Override
    public User createUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        mongoOperations.insert(user);
        return user;
    }

    @Override
    public User getUser(String username) {
        Query query = new Query(Criteria.where("username").is(username));
        User user = mongoOperations.findOne(query, User.class);
        return user;
    }

    @Override
    public List<Party> getAllParties(String username) {
        Criteria criteria = Criteria.where("owner").is(username);
        List<Party> parties = mongoOperations.find(new Query(criteria), Party.class);
        return parties;
    }

    @Override
    public void delete(String partyId) {
        Party party = findById(partyId);
        mongoOperations.remove(party);
    }
	
    /**
     * The current implementation of near assumes an idealized model of a flat earth, meaning that an arcdegree
     * of latitude (y) and longitude (x) represent the same distance everywhere.
     * This is only true at the equator where they are both about equal to 69 miles or 111km. Therefore you must divide the
     * distance you want by 111 for kilometer and 69 for miles.
     *
     * @param maxdistance The distance around a point.
     * @return The calcuated distance in kilometer.
     */
    private Double getInKilometer(Double maxdistance) {
        return maxdistance / KILOMETER;
    }

    @Override
    public String echo(String input) {
        return input;
    }

    @Override
    public List<Party> searchParties(Double lon, Double lat, Double maxdistance) {
        Criteria criteria = new Criteria(LOCATION).near(new Point(lon, lat)).maxDistance(getInKilometer(maxdistance));
        List<Party> parties = new ArrayList<Party>();

        parties.addAll(mongoOperations.find(new Query(criteria),
                Party.class));
        return parties;
    }


}
