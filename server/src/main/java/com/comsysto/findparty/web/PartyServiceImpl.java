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

@Component
public class PartyServiceImpl implements PartyService {

    @Autowired
    @Qualifier("partiesMongoOperations")
    public MongoOperations mongoOperations;


    public static final Double KILOMETER = 111.0d;

    /**
     * The Attribute that is used for the search for the start position
     */
    public static final String START = "start";
    public static final String LOCATION = "location";

    @Override
	public List<Party> searchParties(Double lon, Double lat, Double maxdistance) {
        Criteria criteria = new Criteria(START).near(new Point(lon, lat)).maxDistance(getInKilometer(maxdistance));
        List<Party> parties = new ArrayList<Party>();
        parties.addAll(mongoOperations.find(new Query(criteria),
                Party.class));
        return parties;
	}

	@Override
	public Party showDetails(String partyId) {
		return findById(partyId);
	}

    private Party findById(String partyId) {
        Criteria criteria = Criteria.where(PARTY_ID()).is(partyId);
        Party party = mongoOperations.findOne(new Query(criteria), Party.class);
        
        if(party==null)
            throw new NotFoundException("an existing party with id="+partyId+" was not found on server!");
        
        return party;
    }

    private String PARTY_ID() {
        return "ID";
    }

    @Override
	public void cancelParty(String username, String partyId) {
        Party party = findById(partyId);
        cancelParty(username, party);
    }

    private void cancelParty(String username, Party party) {
        party.getParticipants().remove(username);
        party.getCandidates().remove(username);
    }

    @Override
	public void joinParty(String username, String partyId) {
    	Party party = findById(partyId);
    	party.getCandidates().add(username);
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
        return party.getId();
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


}
