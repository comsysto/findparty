package com.comsysto.findbuddies.android.map;

import com.comsysto.findparty.Party;

/**
 * Created with IntelliJ IDEA.
 * User: stefandjurasic
 * Date: 21.11.12
 * Time: 15:52
 * To change this template use File | Settings | File Templates.
 */
public class PartyOverlayItem { //} extends OverlayItem{
    private final Party party;

    public PartyOverlayItem(String s, String s2, Party party) {
//        super(geoPoint, s, s2);
        this.party = party;
    }


    public Party getParty() {
        return party;
    }
}
