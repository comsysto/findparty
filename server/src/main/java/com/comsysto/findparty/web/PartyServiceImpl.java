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

	@Override
	public Set<Party> searchParties(Double lon, Double lat) {
        Criteria criteria = new Criteria(START).near(new Point(lon, lat)).maxDistance(getInKilometer(maxdistance));
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

}
