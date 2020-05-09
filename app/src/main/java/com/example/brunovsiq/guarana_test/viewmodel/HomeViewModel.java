package com.example.brunovsiq.guarana_test.viewmodel;

import android.app.Application;

import com.example.brunovsiq.guarana_test.model.Place;
import com.example.brunovsiq.guarana_test.service.PlacesApiService;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class HomeViewModel extends AndroidViewModel {

    public MutableLiveData<ArrayList<Place>> placesList = new MutableLiveData<ArrayList<Place>>(); //mutable because we need to change it.
    public MutableLiveData<Boolean> dogLoadError = new MutableLiveData<Boolean>();
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<Boolean>();

    private PlacesApiService dogsService = new PlacesApiService();
    private CompositeDisposable disposable = new CompositeDisposable();

    public HomeViewModel(@NonNull Application application) {
        super(application);
    }

    public void refresh(String latLng) {
        isLoading.setValue(true);
        fetchFromServer(latLng);
    }

    public void fetchFromServer(String latLng) {
        disposable.add(
                dogsService.getPlaces(latLng)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<ArrayList<Place>>() {
                            @Override
                            public void onSuccess(ArrayList<Place> dogBreeds) {
                                placesList.setValue(dogBreeds);
                                dogLoadError.setValue(false);
                                isLoading.setValue(false);
                            }

                            @Override
                            public void onError(Throwable e) {
                                dogLoadError.setValue(true);
                                isLoading.setValue(false);
                            }
                        })
        );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
