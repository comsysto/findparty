package com.comsysto.findparty.web;

import java.util.Collection;
import java.util.List;

import com.comsysto.findparty.Party;
import com.comsysto.findparty.Picture;
import com.comsysto.findparty.User;

/**
 * Created with IntelliJ IDEA.
 * User: tim.hoheisel
 * Date: 05.09.12
 * Time: 14:48
 * To change this template use File | Settings | File Templates.
 */
public interface PartyService {

    public List<Party> searchParties(Double lon, Double lat, Double maxdistance);

    public Party showDetails(String partyId);

    public String createParty(Party party);

    public void update(Party party);
    
    public List<Party> getAllParties(String username);

    public String echo(String input);

    void deleteParty(String partyId);

}
