package com.example.brunovsiq.guarana_test.service;

import com.example.brunovsiq.guarana_test.model.Place;

import java.util.ArrayList;

import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlacesApiService {

    private static final String BASE_URL = "https://api.foursquare.com/";

    private IPlacesApi api;

    public PlacesApiService() {
        api = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(IPlacesApi.class);
    }

    public Single<ArrayList<Place>> getPlaces(String latLng) {
        return api.getPlaces(latLng);
    }

}

