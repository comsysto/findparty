package com.comsysto.findparty.service;

import com.comsysto.findparty.Party;
import com.comsysto.findparty.Picture;
import com.comsysto.findparty.exceptions.ResourceNotFoundException;
import com.comsysto.findparty.web.PartyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.geo.Point;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PartyServiceImpl implements PartyService {

    public static final String SERVER_URL = "http://snuggle.eu01.aws.af.cm/pictures/";

    @Autowired
    public MongoService mongoService;
    
    public static final Double KILOMETER = 111.0d;

    /**
     * The Attribute that is used for the search for the location position
     */
    public static final String LOCATION = "location";


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

    @Override
    public String createPartyImage(String partyId, byte[] content) {
        Party party = findPartyById(partyId);
        String pictureUrl = party.getPictureUrl();
        String pictureId = getIdFromPictureUrl(pictureUrl);
        Picture picture = null;
        if(pictureId != null){
             picture = mongoService.getMongoTemplate().findById(pictureId, Picture.class);
        }
        if(picture == null){
            picture = createPicture(content);
        }else{
            picture.setContent(content);
            mongoService.getMongoTemplate().save(picture);
        }
        //TODO url aus dem Request holen
        party.setPictureUrl(SERVER_URL + picture.getId());
        return party.getPictureUrl();
    }

    private Picture createPicture(byte[] content) {
        Picture picture = new Picture();
        picture.setContent(content);
        mongoService.getMongoTemplate().insert(picture);
        return picture;
    }

    private String getIdFromPictureUrl(String pictureUrl) {
        if(pictureUrl.contains(SERVER_URL)){
            return StringUtils.substringAfterLast("/", pictureUrl);
        }
        return null;
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

        parties.addAll(mongoService.getMongoTemplate().find(new Query(criteria),
                Party.class));
        return parties;
    }

}
