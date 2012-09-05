package com.comsysto.findparty.web;

import java.util.Set;

import com.comsysto.findparty.Party;

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
