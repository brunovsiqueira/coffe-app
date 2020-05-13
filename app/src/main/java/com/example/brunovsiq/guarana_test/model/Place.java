package com.example.brunovsiq.guarana_test.model;


import org.json.JSONException;
import org.json.JSONObject;

public class Place {

    public String id;
    public String name;
    public String address;
    public double lat;
    public double lng;
    public String city;
    public String state;
    public int distance;


    public Place(JSONObject jsonObject) {
        try {
            JSONObject venuesJson = jsonObject.getJSONObject("venue");
            this.id = venuesJson.getString("id");
            this.name = venuesJson.getString("name");
            JSONObject locationJson = venuesJson.getJSONObject("location");
            this.address = locationJson.getString("address");
            this.city = locationJson.getString("city");
            this.state = locationJson.getString("state");
            this.lat = locationJson.getDouble("lat");
            this.lng = locationJson.getDouble("lng");
            this.distance = locationJson.getInt("distance");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
