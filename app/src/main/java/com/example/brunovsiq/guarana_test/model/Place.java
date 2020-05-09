package com.example.brunovsiq.guarana_test.model;


public class Place {

    public String id;
    public String name;
    public String address;
    public double lat;
    public double lng;
    public String city;
    public String state;

    public Place(String id, String name, String address, double lat, double lng, String city, String state) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.city = city;
        this.state = state;
    }
}
