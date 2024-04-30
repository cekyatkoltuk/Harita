package com.example.myapplication;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private GoogleMap mMap;
    private boolean locationPermissionGranted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        Button buttonCurrentLocation = findViewById(R.id.button_current_location);
        buttonCurrentLocation.setOnClickListener(v -> showCurrentLocation());

        Button buttonRandomLocation = findViewById(R.id.button_random_location);
        buttonRandomLocation.setOnClickListener(v -> showRandomLocation());
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        getLocationPermission();
    }

    private void showCurrentLocation() {
        if (mMap != null && locationPermissionGranted) {
            try {
                mMap.setMyLocationEnabled(true);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    private void showRandomLocation() {
        if (mMap != null && locationPermissionGranted) {
            // Rastgele bir tarih ve konum oluştur
            LatLng randomLocation = generateRandomLocation();

            // Konumu işaretle
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(randomLocation).title("Rastgele Tarihli Konum"));

            // Kamerayı konuma hareket ettir
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(randomLocation, 15));
        }
    }

    private LatLng generateRandomLocation() {
        // Rastgele bir konum oluştur (örneğin, dünya üzerinde)
        double minLat = -90;
        double maxLat = 90;
        double minLng = -180;
        double maxLng = 180;

        double lat = minLat + (maxLat - minLat) * new Random().nextDouble();
        double lng = minLng + (maxLng - minLng) * new Random().nextDouble();

        return new LatLng(lat, lng);
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
            } else {
                Toast.makeText(this, "Konum izni reddedildi. Uygulamanın çalışması için izin verilmesi gerekiyor.", Toast.LENGTH_LONG).show();
            }
        }
    }
}