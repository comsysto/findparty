package com.comsysto.findparty;

import java.util.Date;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * com.comsysto.findparty.User: tim.hoheisel
 * Date: 05.09.12
 * Time: 13:09
 * To change this template use File | Settings | File Templates.
 */
public class Party {

    private String id;

    private String name;

    private Integer size;

    private Point location;

    private Date startDate;

    private String level;

    private String category;

    private Set<String> participants;

    private Set<String> candidates;

    private String owner;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Set<String> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<String> participants) {
        this.participants = participants;
    }

    public Set<String> getCandidates() {
        return candidates;
    }

    public void setCandidates(Set<String> candidates) {
        this.candidates = candidates;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
