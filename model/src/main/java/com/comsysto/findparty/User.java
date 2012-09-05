package com.comsysto.findparty;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * com.comsysto.findparty.User: tim.hoheisel
 * Date: 05.09.12
 * Time: 13:15
 * To change this template use File | Settings | File Templates.
 */
public class User {

    private String id;

    private String username;

    private Picture picture;

    private Set<Party> joinedParties;

    private Set<Party> waitingParties;

    private Set<Party> myParties;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public Set<Party> getJoinedParties() {
        return joinedParties;
    }

    public void setJoinedParties(Set<Party> joinedParties) {
        this.joinedParties = joinedParties;
    }

    public Set<Party> getWaitingParties() {
        return waitingParties;
    }

    public void setWaitingParties(Set<Party> waitingParties) {
        this.waitingParties = waitingParties;
    }

    public Set<Party> getMyParties() {
        return myParties;
    }

    public void setMyParties(Set<Party> myParties) {
        this.myParties = myParties;
    }
}
