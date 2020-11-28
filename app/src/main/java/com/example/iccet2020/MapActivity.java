package com.example.iccet2020;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;

public class MapActivity extends AppCompatActivity {
    MapView mapView;
    public MapboxMap mapboxMap;
    private PermissionsManager permissionsManager;
    private LocationComponent locationComponent;
    private CarmenFeature home;
    private CarmenFeature work;
    private String geohsonSourceLayerId = "geojsonSourceId";
    private String symbolIconId = "symbolIconId";
    private static final int REQUEST_CODE = 5678;
    String address;
    Point origin = Point.fromLngLat(54.704514, 20.500506);
    Point destination = Point.fromLngLat(54.707967, 20.504336);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
    }
}