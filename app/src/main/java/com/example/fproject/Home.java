package com.example.fproject;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    private ImageView profileImage;
    private EditText searchBar;
    private Button nearbyButton;
    private RecyclerView citiesRecycler;

    private List<CityModel> cityList;
    private CitiesAdapter citiesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        profileImage = findViewById(R.id.profileImage);
        searchBar = findViewById(R.id.searchBar);
        nearbyButton = findViewById(R.id.nearbyButton);
        citiesRecycler = findViewById(R.id.citiesRecycler);

        // Initialize city list
        cityList = new ArrayList<>();
        loadCities();

        // Setup RecyclerView
        citiesAdapter = new CitiesAdapter(this, cityList);
        citiesRecycler.setLayoutManager(new LinearLayoutManager(this));
        citiesRecycler.setAdapter(citiesAdapter);

        // Profile click -> open ProfileFragment
        profileImage.setOnClickListener(v -> openFragment(new ProfileFragment()));

        // Nearby button -> open MapFragment correctly
        nearbyButton.setOnClickListener(v -> openFragment(new MapFragment()));

        // You can implement searchBar text change listener later
    }

    private void loadCities() {
        // Example data, add your cities here
        cityList.add(new CityModel("New York", R.drawable.city_placeholder));
        cityList.add(new CityModel("Paris", R.drawable.city_placeholder));
        cityList.add(new CityModel("Tokyo", R.drawable.city_placeholder));
        cityList.add(new CityModel("London", R.drawable.city_placeholder));
        cityList.add(new CityModel("Dubai", R.drawable.city_placeholder));
    }

    private void openFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, fragment) // replaces Home layout
                .addToBackStack(null) // so back button returns to Home
                .commit();
    }
}
