package com.comsysto.findparty.web;

import com.comsysto.findparty.Party;

import java.util.Date;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: tim.hoheisel
 * Date: 05.09.12
 * Time: 14:48
 * To change this template use File | Settings | File Templates.
 */
public interface PartyService {

    public Set<Party> searchParties(Double lon, Double lat);

    public Party showDetails(String partyId);

    public void cancelParty(String username, String partyId);

    public void joinParty(String username, String partyId);

    public void createParty(Party party);

}
