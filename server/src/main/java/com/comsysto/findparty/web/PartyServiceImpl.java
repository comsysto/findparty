package com.comsysto.findparty.web;

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
        List<Party> tracks = mongoOperations.find(new Query(criteria),
                Party.class);
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
