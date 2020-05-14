package com.example.brunovsiq.guarana_test.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.example.brunovsiq.guarana_test.R;
import com.example.brunovsiq.guarana_test.model.Place;
import com.example.brunovsiq.guarana_test.viewmodel.HomeViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.jacksonandroidnetworking.JacksonParserFactory;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback {

    FusedLocationProviderClient fusedLocationClient;
    private GoogleMap mMap;
    private HomeViewModel viewModel;
    private double latitude;
    private double longitude;

    private RecyclerView placesRecyclerView;
    private ProgressBar progressBar;
    private PlacesAdapter placesAdapter = new PlacesAdapter(new ArrayList<>(), this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        findViewItems();

        AndroidNetworking.initialize(getApplicationContext());
        AndroidNetworking.setParserFactory(new JacksonParserFactory());

        requestPermission();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.home_map);
        mapFragment.getMapAsync(this);

    }

    private void findViewItems() {
        placesRecyclerView = findViewById(R.id.places_rv);
        progressBar = findViewById(R.id.home_pb);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        placesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        placesRecyclerView.setAdapter(placesAdapter);
        observeViewModel();
    }

    private void observeViewModel() {
        viewModel.placesList.observe(this, placeArrayList -> {
            if (placeArrayList.size() > 0) {
                addMarkers(placeArrayList);
                placesAdapter.updatePlacesList(placeArrayList);
                progressBar.setVisibility(View.GONE);
            }
        });

        viewModel.isLoading.observe(this, placeArrayList -> {
            progressBar.setVisibility(View.VISIBLE);
        });
    }

    private void addMarkers(ArrayList<Place> placeArrayList) {
        for (Place place : placeArrayList) {
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(place.lat, place.lng))
                    .title(place.name))
                    .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay! Do the
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                }

            } else {
                // permission denied, boo!
                Toast.makeText(this, "Permission needed!", Toast.LENGTH_LONG).show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        // yourMethod();
                    }
                }, 3000);   //3 seconds
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        0);

            }
        }
    }

    private void requestPermission() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
        } else {
            // Permission has already been granted

        }


    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


        } else {
            if (fusedLocationClient.getLastLocation() != null) {
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(location -> {
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                viewModel.refresh(latitude + "," + longitude);
                                moveCamera(location.getLatitude(), location.getLongitude());
                            }


                        });
            }
        }

    }

    private void moveCamera(double latitude, double longitude) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 13));
    }
}
