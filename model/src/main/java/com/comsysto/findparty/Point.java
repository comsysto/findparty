package com.comsysto.findparty;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * com.comsysto.findparty.User: tim.hoheisel
 * Date: 05.09.12
 * Time: 13:12
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("serial")
public class Point implements Serializable {

    private Double lon;

    private Double lat;
    
    public Double getLon() {
        return lon;
    }

    public Double getLat() {
        return lat;
    }
    
    public void setLon(Double lon) {
    	this.lon = lon;
    }
    
    public void setLat(Double lat) {
    	this.lat = lat;
    }
}
