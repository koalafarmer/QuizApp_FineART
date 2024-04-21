package com.example.quizapp_elfadil;

import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.Random;

public class LeaderboardActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add mock users with their scores at random locations
        addMockUser(new LatLng(37.7749, -122.4194), "Pipo", 15);
        addMockUser(new LatLng(40.7128, -74.0060), "Bomboclat", 20);
        addMockUser(new LatLng(34.0522, -118.2437), "Mysunshine", 12);
        addMockUser(new LatLng(41.8781, -87.6298), "Eepy", 25);
    }

    private void addMockUser(LatLng location, String userName, int score) {
        mMap.addMarker(new MarkerOptions().position(location).title(userName + " Score: " + score));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
    }
}
