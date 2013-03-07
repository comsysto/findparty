package com.comsysto.findparty.web;

import com.comsysto.findparty.ErrorModel;
import com.comsysto.findparty.Party;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/parties")
public class PartyControllerImpl implements PartyController {

    @Autowired
    public PartyService partyService;

    public static final Logger LOGGER = Logger.getLogger(PartyControllerImpl.class);

    public static final Double KILOMETER = 111.0d;

    /**
     * The Attribute that is used for the search for the start position
     */
    public static final String START = "start";

    @ExceptionHandler(PartyServiceException.class)
    public ModelAndView handleIOException(PartyServiceException ex, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        LOGGER.warn("An error occurred in the service", ex);
        ErrorModel errorModel = new ErrorModel();
        errorModel.setThrowable(ex);
        return new ModelAndView("/error.jsp", "error", errorModel);
    }

    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleIOException(RuntimeException ex, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        LOGGER.warn("An error occurred in the service", ex);
        PartyServiceException partyServiceException = new PartyServiceException("An error occurred in the service", ex);
        ErrorModel errorModel = new ErrorModel();
        errorModel.setThrowable(partyServiceException);
        return new ModelAndView("/error.jsp", "error", errorModel);
    }


    @RequestMapping(value = "/search/{lon}/{lat}/{maxdistance}", method = RequestMethod.GET, produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    List<Party> search(@PathVariable("lon") Double lon, @PathVariable("lat") Double lat, @PathVariable("maxdistance") Double maxdistance, HttpServletRequest request) {
        LOGGER.info("Header:" + request.getHeader("Authorization"));
        List<Party> parties = partyService.searchParties(lon, lat, maxdistance);
        return parties;
    }


    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String create(@RequestBody Party party) {
        LOGGER.info("received party for: " + party.getOwner() + " in category: " +party.getCategory());
        String partyId = partyService.createParty(party);
        return partyId;
    }

    @RequestMapping(value = "/{partyId}", method = RequestMethod.PUT, consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody Party party, @PathVariable String partyId) {
        
        if(!party.getId().equals(partyId))
            throw new IllegalArgumentException("Id mismatch of requested party(id="+partyId+") and provided party(id="+party.getId()+") does not match");
        
        partyService.update(party);            
    }
    
    @RequestMapping(value = "/{partyId}/subscriptions", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void subscribe(@PathVariable String partyId, @RequestParam(value = "action", required = false) String action, @RequestBody String username) {

        if(action!=null && action.equals("cancel"))
            partyService.cancelParty(username, partyId);
        else
            partyService.joinParty(username, partyId);
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
        partyService.delete(partyId);        
    }

    @RequestMapping(value="/echo/{input}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody String echo(@PathVariable("input") String input) {
        return input;
    }

}
