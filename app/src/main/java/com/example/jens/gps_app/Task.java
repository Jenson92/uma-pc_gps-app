package com.example.jens.gps_app;

/**
 * Created by Jens on 04.04.2016.
 */

public class Task {

    private int id;
    private String title;
    private double lat;
    private double lng;
    private int range;
    private String desc;

    public Task() {
    }

    public Task(String title, double lat, double lng, int range, String desc) {
        super();
        this.title = title;
        this.lat = lat;
        this.lng = lng;
        this.range = range;
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    @Override
    public String toString() {
        return "Task [id=" + id + ", title=" + title + ", lat=" + lat + ", lng=" + lng + ", range=" + range + ", Desc=" + desc + "]";
    }

}