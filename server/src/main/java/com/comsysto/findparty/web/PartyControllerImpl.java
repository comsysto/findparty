package com.comsysto.findparty.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.comsysto.findparty.Party;

@Controller
@RequestMapping("/parties")
public class PartyControllerImpl implements PartyController {

    @Autowired
    public PartyService partyService;

    /*
     * @RequestMapping(value = "/get", method = RequestMethod.GET, produces =
     * "application/json") public
     * 
     * @ResponseBody List<com.comsysto.findparty.Party> getAll() throws
     * Exception { return
     * mongoOperations.findAll(com.comsysto.findparty.Party.class); }
     */

    /*
     * @RequestMapping(value = "/get/{lon1}/{lat1}/{lon2}/{lat2}/", method =
     * RequestMethod.GET, produces = "application/json") public
     * 
     * @ResponseBody List<com.comsysto.findparty.Party>
     * getByBounds(@PathVariable("lon1") Double lon1, @PathVariable("lat1")
     * Double lat1, @PathVariable("lon2") Double lon2, @PathVariable("lat2")
     * Double lat2) throws Exception { /* > box = [[40.73083, -73.99756],
     * [40.741404, -73.988135]] > db.places.find({"loc" : {"$within" : {"$box" :
     * box}}})
     */
    /*
     * Criteria criteria = new Criteria(START).within(new Box(new
     * com.comsysto.findparty.Point(lon1, lat1), new
     * com.comsysto.findparty.Point(lon2, lat2)));
     * List<com.comsysto.findparty.Party> tracks = mongoOperations.find(new
     * Query(criteria), com.comsysto.findparty.Party.class); return tracks; }
     */

    @RequestMapping(value = "/location/{lon}/{lat}", method = RequestMethod.GET, produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    List<Party> search(@PathVariable("lon") Double lon, @PathVariable("lat") Double lat) {
        List<Party> parties = partyService.searchParties(lon, lat);
        return parties;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public void create(@RequestBody Party party) {
        partyService.createParty(party);
    }

    @RequestMapping(value = "/{partyId}", method = RequestMethod.PUT, consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody Party party) {
        partyService.update(party);            
    }
    
    @RequestMapping(value = "/{partyId}/subscriptions", method = RequestMethod.PUT, consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public void subscribe(@PathVariable String partyId, @RequestParam(value = "action", required = false) String action, @RequestBody String username) {
        
        if(action!=null && action.equals("cancel"))
            partyService.cancelParty(username, partyId);
        
        partyService.joinParty(username, partyId);
    }
    
    @RequestMapping(value = "/{partyId}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void show(@PathVariable("partyId") String partyId) {
        partyService.showDetails(partyId);
    }

    
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody List<Party> findByUsername(@RequestParam(value = "user", required = true) String username) {
        return partyService.getAllParties(username);        
    }
    
    
    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable String partyId) {
        partyService.delete(partyId);        
    }
    
    
    @RequestMapping(value="/echo/{input}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody String echo(@PathVariable String input) {
        return input;
    }

    /*
     * @RequestMapping(value = "/add", method = RequestMethod.POST, consumes =
     * "application/json")
     * 
     * @ResponseStatus(HttpStatus.OK) public void add(@RequestBody
     * com.comsysto.findparty.Party track) throws Exception {
     * mongoOperations.insert(track); }
     */

    /*
     * @RequestMapping(value = "/foruser", method = RequestMethod.GET)
     * 
     * @ResponseStatus(HttpStatus.OK) public
     * 
     * @ResponseBody List<com.comsysto.findparty.Party> tracksForUser() throws
     * Exception { com.comsysto.findparty.User principal =
     * (com.comsysto.findparty.User)
     * SecurityContextHolder.getContext().getAuthentication().getPrincipal();
     * Criteria criteria = Criteria.where(USER).is(principal.getUsername());
     * List<com.comsysto.findparty.Party> tracks = mongoOperations.find(new
     * Query(criteria), com.comsysto.findparty.Party.class); return tracks; }
     */

    /*
     * @RequestMapping(value = "/upload", method = RequestMethod.POST)
     * 
     * @ResponseStatus(HttpStatus.OK) public void upload(@RequestParam("file")
     * MultipartFile multipartFile) throws Exception {
     * com.comsysto.findparty.User principal = (com.comsysto.findparty.User)
     * SecurityContextHolder.getContext().getAuthentication().getPrincipal();
     * ObjectMapper mapper = new ObjectMapper(); com.comsysto.findparty.Party
     * track = mapper.readValue(multipartFile.getBytes(),
     * com.comsysto.findparty.Party.class);
     * track.setUser(principal.getUsername()); mongoOperations.insert(track); }
     */
}
