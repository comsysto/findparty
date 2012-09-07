package com.comsysto.findparty.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.geo.Point;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.stereotype.Component;

import com.comsysto.findparty.Party;
import com.comsysto.findparty.User;

@Component
public class PartyServiceImpl implements PartyService {

    @Autowired
    @Qualifier("partiesMongoOperations")
    public MongoOperations partyMongoOperations;
    
    @Autowired
    @Qualifier("userMongoOperations")
    public MongoOperations userMongoOperations;
    

    public static final Double KILOMETER = 111.0d;

    /**
     * The Attribute that is used for the search for the start position
     */
    public static final String LOCATION = "location";


	@Override
	public Party showDetails(String partyId) {
		return findById(partyId);
	}

    private Party findById(String partyId) {
        Party party = partyMongoOperations.findById(partyId, Party.class);     
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
        partyMongoOperations.save(party);        
    }

    @Override
	public void joinParty(String username, String partyId) {
    	Party party = findById(partyId);
    	User user = findUserAndCreateIfNotExists(username);

    	party.getCandidates().add(user.getUsername());
    	partyMongoOperations.save(party);
	}

	
	@Override
	public void update(Party party) {
	    //checks if party with same id exists
	    findById(party.getId());
	    	    
	    partyMongoOperations.save(party);
	}
	
	@Override
	public String createParty(Party party) {
	    
		partyMongoOperations.insert(party);
        
		User user = findUserAndCreateIfNotExists(party.getOwner());
		
		return party.getId();
	}

    private User findUserAndCreateIfNotExists(String username) {
        Criteria criteria = Criteria.where("username").is(username);
        Query query = new Query(criteria );
        User user = userMongoOperations.findOne(query , User.class);
        if(user!=null) {
            return user;
        }
        else {
            return createUser(username);
        }
    }


    private User createUser(String username) {
        User user = new User();
        user.setUsername(username);
        userMongoOperations.insert(user);
        return user;
    }

    @Override
    public List<Party> getAllParties(String username) {
        Criteria criteria = Criteria.where("owner").is(username);
        List<Party> parties = partyMongoOperations.find(new Query(criteria), Party.class);
        return parties;
    }

    @Override
    public void delete(String partyId) {
        Party party = findById(partyId);
        partyMongoOperations.remove(party);
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
        parties.addAll(partyMongoOperations.find(new Query(criteria),
                Party.class));
        return parties;
    }


}
