package com.example.brunovsiq.guarana_test.service;

import com.example.brunovsiq.guarana_test.model.Place;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IPlacesApi {

    @GET("venues/explore?client_id=UOJ24NDYP4VJG2SGVMEKRNE0D4EFT30T0EF0IEOSI4DB3DJ2&client_secret=SLZOPYABHLNEXAQMYNPQNASDHSKGOOBQHAUIN3MYE2WZSHXJ&v=20180323&query=coffe&limit=3&openNow=1")
    @SerializedName("response/groups/items")
    Single<ArrayList<Place>> getPlaces(@Query("ll") String latLong);

}
