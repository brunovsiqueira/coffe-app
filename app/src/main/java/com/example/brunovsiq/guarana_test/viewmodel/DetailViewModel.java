package com.example.brunovsiq.guarana_test.viewmodel;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.brunovsiq.guarana_test.model.Place;
import com.example.brunovsiq.guarana_test.service.ApiService;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.disposables.CompositeDisposable;

public class DetailViewModel extends AndroidViewModel {

    public MutableLiveData<Place> placeMutable = new MutableLiveData<Place>(); //mutable because we need to change it.
    public MutableLiveData<Boolean> placeLoadError = new MutableLiveData<Boolean>();
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<Boolean>();

    private CompositeDisposable disposable = new CompositeDisposable();

    public DetailViewModel(@NonNull Application application) {
        super(application);
    }

    public void refresh(Place place) {
        isLoading.setValue(true);
        fetchFromServer(place);
    }

    public void fetchFromServer(Place place) {

        AndroidNetworking.get(ApiService.BASE_URL + ApiService.venuesEndpoint + place.id)
                .addQueryParameter("client_id", ApiService.clientId)
                .addQueryParameter("client_secret", ApiService.clientSecret)
                .addQueryParameter("v", "20200101")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject venueObject = response.getJSONObject("response").getJSONObject("venue");
                            place.setNewAttributes(venueObject);
                            placeMutable.setValue(place);
                            placeLoadError.setValue(false);
                            isLoading.setValue(false);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        placeLoadError.setValue(true);
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
