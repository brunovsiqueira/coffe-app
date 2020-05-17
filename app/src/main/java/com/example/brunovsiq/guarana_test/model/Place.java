package com.example.brunovsiq.guarana_test.model;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Place implements Serializable {

    @PrimaryKey
    @NonNull
    public String id;

    public String name;
    public String address;
    public double lat;
    public double lng;
    public String city;
    public String state;
    public int distance;
    public String phone;
    public String pictureUrl; //prefix + original + suffix
    public String description;

    public String website; //canonicalUrl

    public Place() {

    }

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

    public void setNewAttributes(JSONObject jsonObject) {
        //set phone, pictureUrl, description and website
        try {
            if (jsonObject.getJSONObject("contact").has("phone")) {
                this.phone = jsonObject.getJSONObject("contact").getString("phone");
            }
            if (jsonObject.getJSONObject("photos").getJSONArray("groups").length() > 0) {
                this.pictureUrl = jsonObject.getJSONObject("photos").getJSONArray("groups").getJSONObject(0).getJSONArray("items").getJSONObject(0).getString("prefix") + "original" + jsonObject.getJSONObject("photos").getJSONArray("groups").getJSONObject(0).getJSONArray("items").getJSONObject(0).getString("suffix");
            }
            if (jsonObject.has("description")) {
                this.description = jsonObject.getString("description");
            }
            if (jsonObject.getString("canonicalUrl") != null) {
                this.website = jsonObject.getString("canonicalUrl");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
