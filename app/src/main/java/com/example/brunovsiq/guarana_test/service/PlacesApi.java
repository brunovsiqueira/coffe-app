package com.example.brunovsiq.guarana_test.service;

import com.example.brunovsiq.guarana_test.model.Place;

import java.util.ArrayList;

import io.reactivex.Single;

public class PlacesApi implements IPlacesApi {

    @Override
    public Single<ArrayList<Place>> getPlaces(String latLong) {
        return null;
    }

}
