package com.comsysto.findparty.web;

import com.comsysto.findparty.Party;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.mongodb.core.geo.Point;


import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/party")
public class PartyController {

    @Autowired
    public PartyService partyService;


    /**
     * The Attribute that is used for the search for the start position
     */
    public static final String START = "start";
    /**
     * The Attribute that is used for the search for the user
     */
    private static final String USER = "user";

/*    @RequestMapping(value = "/get", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    List<com.comsysto.findparty.Party> getAll() throws Exception {
        return mongoOperations.findAll(com.comsysto.findparty.Party.class);
    } */

  /*  @RequestMapping(value = "/get/{lon1}/{lat1}/{lon2}/{lat2}/", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    List<com.comsysto.findparty.Party> getByBounds(@PathVariable("lon1") Double lon1, @PathVariable("lat1") Double lat1, @PathVariable("lon2") Double lon2, @PathVariable("lat2") Double lat2) throws Exception {
        /*
        > box = [[40.73083, -73.99756], [40.741404,  -73.988135]]
        > db.places.find({"loc" : {"$within" : {"$box" : box}}})
         */
    /*
        Criteria criteria = new Criteria(START).within(new Box(new com.comsysto.findparty.Point(lon1, lat1), new com.comsysto.findparty.Point(lon2, lat2)));
        List<com.comsysto.findparty.Party> tracks = mongoOperations.find(new Query(criteria),
                com.comsysto.findparty.Party.class);
        return tracks;
    }
    */

    @RequestMapping(value = "/search/{lon}/{lat}/{maxdistance}", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    Set<Party> searchParties(@PathVariable("lon") Double lon, @PathVariable("lat") Double lat, @PathVariable("maxdistance") Double  maxdistance) throws Exception {
        return partyService.searchParties(lon, lat);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST, consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public void createParty(@RequestBody com.comsysto.findparty.Party party) throws Exception {
        partyService.createParty(party);
    }

    @RequestMapping(value = "/cancel/{username}/{partyId}", method = RequestMethod.GET, consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public void cancelParty(@PathVariable("username") String username, @PathVariable("partyId") String partyId) throws Exception {
        partyService.cancelParty(username, partyId);
    }

    @RequestMapping(value = "/showdetails/{partyId}", method = RequestMethod.GET, consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public void showDetails(@PathVariable("partyId") String partyId) throws Exception {
        partyService.showDetails(partyId);
    }

/*    @RequestMapping(value = "/add", method = RequestMethod.POST, consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public void add(@RequestBody com.comsysto.findparty.Party track) throws Exception {
        mongoOperations.insert(track);
    } */

/*    @RequestMapping(value = "/foruser", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public
    @ResponseBody
    List<com.comsysto.findparty.Party> tracksForUser() throws Exception {
        com.comsysto.findparty.User principal = (com.comsysto.findparty.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Criteria criteria = Criteria.where(USER).is(principal.getUsername());
        List<com.comsysto.findparty.Party> tracks = mongoOperations.find(new Query(criteria),
                com.comsysto.findparty.Party.class);
        return tracks;
    }*/

/*    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void upload(@RequestParam("file") MultipartFile multipartFile) throws Exception {
        com.comsysto.findparty.User principal = (com.comsysto.findparty.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ObjectMapper mapper = new ObjectMapper();
        com.comsysto.findparty.Party track = mapper.readValue(multipartFile.getBytes(), com.comsysto.findparty.Party.class);
        track.setUser(principal.getUsername());
        mongoOperations.insert(track);
    }*/
}
