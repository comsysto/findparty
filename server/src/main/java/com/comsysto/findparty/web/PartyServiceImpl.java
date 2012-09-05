package com.comsysto.findparty.web;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.comsysto.findparty.Party;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.geo.Point;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class PartyServiceImpl implements PartyService {

    @Autowired
    @Qualifier("tracksMongoOperations")
    public MongoOperations mongoOperations;


    public static final Double KILOMETER = 111.0d;

    /**
     * The Attribute that is used for the search for the start position
     */
    public static final String START = "start";

    private static final Double MAXDISTANCE = 5.;

    @Override
	public Set<Party> searchParties(Double lon, Double lat) {
        Criteria criteria = new Criteria(START).near(new Point(lon, lat)).maxDistance(getInKilometer(MAXDISTANCE));
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
    }

    @Override
	public void joinParty(String username, String partyId) {
		// TODO Auto-generated method stub
		
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
