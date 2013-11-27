package com.comsysto.findparty.web;

import com.comsysto.findparty.Party;
import com.comsysto.findparty.exceptions.InvalidRequestException;
import com.comsysto.findparty.exceptions.ResourceNotFoundException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(value = "/parties", produces = "application/json")
public class PartyControllerImpl implements PartyController {

    public static final Logger LOGGER = Logger.getLogger(PartyControllerImpl.class);

    @Autowired
    public PartyService partyService;


    @RequestMapping(value = "/search/{lon}/{lat}/{maxdistance}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    List<Party> search(@PathVariable("lon") Double lon, @PathVariable("lat") Double lat, @PathVariable("maxdistance") Double maxdistance, HttpServletRequest request) {
        List<Party> parties = partyService.searchParties(lon, lat, maxdistance);
        return parties;
    }


    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String create(@RequestBody Party party) {
        String partyId = partyService.createParty(party);
        return partyId;
    }

    @RequestMapping(value = "/{partyId}", method = RequestMethod.PUT, consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody Party party, @PathVariable String partyId) {
        
        if(!party.getId().equals(partyId))
            throw new InvalidRequestException("Id mismatch of requested party(id="+partyId+") and provided party(id="+party.getId()+") does not match");
        
        partyService.update(party);            
    }

    @Override
    @RequestMapping(value = "/{partyId}", method = RequestMethod.POST, consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public String createPartyImage(@RequestBody byte[] content, @PathVariable String partyId) {
        return partyService.createPartyImage(partyId, content);
    }

    @RequestMapping(value = "/{partyId}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Party show(@PathVariable("partyId") String partyId) {
        return partyService.showDetails(partyId);
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody List<Party> findByUsername(@RequestParam(value = "user", required = true) String username) {
        return partyService.getAllParties(username);        
    }
    

    @RequestMapping(value="/{partyId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable String partyId) {
        partyService.deleteParty(partyId);
    }

    @RequestMapping(value="/echo/{input}", method = RequestMethod.GET)
    public @ResponseBody String echo(@PathVariable("input") String input) {
        return input;
    }

}
