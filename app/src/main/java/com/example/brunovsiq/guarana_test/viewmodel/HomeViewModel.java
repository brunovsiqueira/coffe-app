package com.example.brunovsiq.guarana_test.viewmodel;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.brunovsiq.guarana_test.model.Place;
import com.example.brunovsiq.guarana_test.service.ApiService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.disposables.CompositeDisposable;

public class HomeViewModel extends AndroidViewModel {

    public MutableLiveData<ArrayList<Place>> placesList = new MutableLiveData<ArrayList<Place>>(); //mutable because we need to change it.
    public MutableLiveData<Boolean> placesLoadError = new MutableLiveData<Boolean>();
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<Boolean>();

    private CompositeDisposable disposable = new CompositeDisposable();

    public HomeViewModel(@NonNull Application application) {
        super(application);
    }

    public void refresh(String latLng) {
        isLoading.setValue(true);
        fetchFromServer(latLng);
    }

    public void fetchFromServer(String latLng) {

        AndroidNetworking.get(ApiService.BASE_URL + ApiService.venuesExploreEndpoint + "&ll=" + latLng)
                //.addQueryParameter("limit", "3")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ArrayList<Place> placeList = new ArrayList<>();
                        try {
                            JSONArray itemArray = response.getJSONObject("response").getJSONArray("groups").getJSONObject(0).getJSONArray("items");
                            for (int i=0; i < itemArray.length(); i++) {
                                try {
                                    if (itemArray.getJSONObject(i).has("venue")) {
                                        Place place = new Place(itemArray.getJSONObject(i));
                                        placeList.add(place);

                                        placesList.setValue(placeList);
                                        placesLoadError.setValue(false);
                                        isLoading.setValue(false);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        placesLoadError.setValue(true);
                        isLoading.setValue(false);
                    }
                });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
