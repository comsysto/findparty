package com.comsysto.findparty.web;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.geo.Point;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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
    public static final String LOCATION = "location";

    @Override
	public Set<Party> searchParties(Double lon, Double lat, Double maxdistance) {
        Criteria criteria = new Criteria(LOCATION).near(new Point(lon, lat)).maxDistance(getInKilometer(maxdistance));
        Set<Party> parties = new HashSet<Party>();
        parties.addAll(mongoOperations.find(new Query(criteria),
                Party.class));
        return parties;
	}

	@Override
	public Party showDetails(String partyId) {
		Criteria criteria = Criteria.where(PARTY_ID()).is(partyId);
        return mongoOperations.findOne(new Query(criteria), Party.class);
	}

    private String PARTY_ID() {
        return "ID";
    }

    @Override
	public void cancelParty(String username, String partyId) {
        Criteria criteria = Criteria.where(PARTY_ID()).is(partyId);
        Party party = mongoOperations.findOne(new Query(criteria), Party.class);
        cancelParty(username, party);
    }

    private void cancelParty(String username, Party party) {
        party.getParticipants().remove(username);
        party.getCandidates().remove(username);
    }

    @Override
	public void joinParty(String username, String partyId) {
    	Criteria criteria = Criteria.where(PARTY_ID()).is(partyId);
    	Party party = mongoOperations.findOne(new Query(criteria),Party.class);
    	party.getCandidates().add(username);
		mongoOperations.save(party);
		
	}

	@Override
	public void createParty(Party party) {
		mongoOperations.insert(party);
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

}
