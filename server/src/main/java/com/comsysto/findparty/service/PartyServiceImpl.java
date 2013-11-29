package com.comsysto.findparty.service;

import com.comsysto.findparty.Party;
import com.comsysto.findparty.exceptions.ResourceNotFoundException;
import com.comsysto.findparty.web.PartyService;
import com.comsysto.findparty.web.PictureService;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.geo.Point;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class PartyServiceImpl implements PartyService {

    private static final Logger LOG = Logger.getLogger(PartyServiceImpl.class);

    @Autowired
    public MongoService mongoService;

    @Autowired
    public PictureService pictureService;

    public static final Double KILOMETER = 111.0d;

    public static final String LOCATION = "location";
    public static final String START_DATE = "startDate";

	@Override
	public Party showDetails(String partyId) {
		return findPartyById(partyId);
	}

    private Party findPartyById(String partyId) {
        Party party = mongoService.getMongoTemplate().findById(partyId, Party.class);
        if(party==null)
            throw new ResourceNotFoundException("an existing party with id="+partyId+" was not found on server!");
        
        return party;
    }


	@Override
	public void update(Party party) {
        //checks if party with same id exists
        findPartyById(party.getId());
	    mongoService.getMongoTemplate().save(party);
	}
	
	@Override
	public String createParty(Party party) {
		mongoService.getMongoTemplate().insert(party);
		return party.getId();
	}

    @Override
    public List<Party> getAllParties(String username) {
        Criteria criteria = Criteria.where("owner").is(username);
        List<Party> parties = mongoService.getMongoTemplate().find(new Query(criteria), Party.class);
        return parties;
    }

    @Override
    public void deleteParty(String partyId) {
        Party party = findPartyById(partyId);
        mongoService.getMongoTemplate().remove(party);
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
        List <Party> parties = new ArrayList<Party>();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        Date startDate = calendar.getTime();

        DateTime dt = new DateTime().withTimeAtStartOfDay();
        LOG.info("Joda_date: " + dt);
        LOG.info("Query for startDate >= " + startDate);


        Criteria locationCriteria = new Criteria()
                .where(LOCATION)
                    .near(new Point(lon, lat))
                    .maxDistance(getInKilometer(maxdistance))
                .and(START_DATE)
                    .gte(startDate);
        Query query = new Query(locationCriteria);

        parties.addAll(mongoService.getMongoTemplate().find(query, Party.class));
        return parties;
    }

}
