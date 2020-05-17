package com.example.brunovsiq.guarana_test.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.brunovsiq.guarana_test.R;
import com.example.brunovsiq.guarana_test.database.PlaceDatabase;
import com.example.brunovsiq.guarana_test.model.Place;
import com.example.brunovsiq.guarana_test.viewmodel.DetailViewModel;
import com.example.brunovsiq.guarana_test.viewmodel.HomeViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Place place;
    private DetailViewModel viewModel;

    @BindView(R.id.text_description)
    TextView textDescription;

    @BindView(R.id.text_address)
    TextView textAddress;

    @BindView(R.id.text_title)
    TextView textTitle;

    @BindView(R.id.phone_image)
    ImageView phoneImage;

    @BindView(R.id.button_website)
    Button websiteButton;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.imageView)
    ImageView coffePicture;

    @BindView(R.id.favorite)
    LikeButton favoriteButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

        place = (Place) getIntent().getSerializableExtra("place");
        textTitle.setText(place.name);
        textAddress.setText(place.address);

        PlaceDatabase
                .getInstance(getApplicationContext())
                .getPlaceDao()
                .getPlace(place.id);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
        viewModel.refresh(place);

        observeViewModel();
    }

    private void observeViewModel() {
        viewModel.placeMutable.observe(this, newPlace -> {
            populateView(newPlace);
            progressBar.setVisibility(View.GONE);
            favoriteButton.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
//                    PlaceDatabase
//                            .getInstance(getApplicationContext())
//                            .getPlaceDao()
//                            .insert(newPlace);
                    
                }

                @Override
                public void unLiked(LikeButton likeButton) {
//                    PlaceDatabase
//                            .getInstance(getApplicationContext())
//                            .getPlaceDao()
//                            .delete(newPlace);
                }
            });
        });

        viewModel.isLoading.observe(this, loading -> {
            progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        });
    }

    private void populateView(Place newPlace) {
        textDescription.setText(newPlace.description != null ? newPlace.description : "No description provided");
        if (newPlace.pictureUrl != null) {
            Picasso.get().load(newPlace.pictureUrl).into(coffePicture);
        }
        if (newPlace.phone != null){
            phoneImage.setOnClickListener(v -> {

                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + newPlace.phone)));
            });
        }
        if (newPlace.website != null) {
            websiteButton.setOnClickListener(v -> {
                Intent browserIntent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(newPlace.website));
                startActivity(browserIntent);
            });

        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(place.lat, place.lng))
                .title(place.name))
                .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(place.lat, place.lng), 13));
    }
}
