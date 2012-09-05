package com.comsysto.findparty;

/**
 * Created with IntelliJ IDEA.
 * com.comsysto.findparty.User: tim.hoheisel
 * Date: 05.09.12
 * Time: 13:12
 * To change this template use File | Settings | File Templates.
 */
public class Point {

    private Double lon;

    private Double lat;

    public Point(Double lon, Double lat) {
        this.lon = lon;
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public Double getLat() {
        return lat;
    }
}
