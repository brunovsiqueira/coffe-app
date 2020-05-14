package com.example.brunovsiq.guarana_test.view.activity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.brunovsiq.guarana_test.R;
import com.example.brunovsiq.guarana_test.model.Place;

import java.text.DecimalFormat;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlaceViewHolder> {

    private ArrayList<Place> placesList;
    private Context context;

    public PlacesAdapter(ArrayList<Place> placesList, Context context) {
        this.placesList = placesList;
    }

    public void updatePlacesList(ArrayList<Place> newDogsList) {
        placesList.clear();
        placesList.addAll(newDogsList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        Place place = placesList.get(position);
        holder.name.setText(place.name);
        String kmDistance = new DecimalFormat("#.##").format(place.distance/1000);
        holder.distance.setText(kmDistance + "KM away");
    }

    @Override
    public int getItemCount() {
        return placesList.size();
    }

    class PlaceViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView distance;

        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.place_name);
            distance = itemView.findViewById(R.id.place_distance);

            itemView.setOnClickListener(v -> {
                Intent intentToDetail = new Intent(itemView.getContext(), DetailActivity.class);
                intentToDetail.putExtra("place", placesList.get(getAdapterPosition()));
                itemView.getContext().startActivity(intentToDetail);
            });
        }
    }
}

