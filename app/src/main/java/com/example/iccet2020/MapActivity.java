package com.example.iccet2020;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Color;
import android.graphics.RenderNode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.SyncStateContract;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.core.constants.Constants;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.Line;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.v5.navigation.MapboxNavigation;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, Callback<DirectionsResponse>, PermissionsListener {
    MapView mapView;
    public MapboxMap mapboxMap;
    private PermissionsManager permissionsManager;
    private LocationComponent locationComponent;
    private CarmenFeature home;
    private CarmenFeature work;
    private String geohsonSourceLayerId = "geojsonSourceId";
    private String symbolIconId = "symbolIconId";
    private static final int REQUEST_CODE = 5678;
    MapboxNavigation navigation;
    private MapboxDirections client;
    int c = 0;
    double distance;
    String st;
    String startLocation = "";
    String endLocation = "";
    String address;
    Point origin = Point.fromLngLat(54.704514, 20.500506);
    Point destination = Point.fromLngLat(54.707967, 20.504336);

    private static final String ROUT_LAYER_ID = "route-layer-id";
    private static final String ROUT_SOURCE_ID = "route-source-id";
    private static final String ICON_LAYER_ID = "icon-layer-id";
    private static final String ICON_SOURCE_ID = "icon-source-id";
    private static final String RED_PIN_ICON_ID = "red-pin-icon-id";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, "pk.eyJ1IjoiZmVzaGFydDUwMCIsImEiOiJja2kwcTQyc3MwaXY4Mnltc3RjdGo0cWV1In0.nciMMKY38q8p37kx3uwzmw");
        setContentView(R.layout.activity_map);
        navigation = new MapboxNavigation(this, "pk.eyJ1IjoiZmVzaGFydDUwMCIsImEiOiJja2kwcTQyc3MwaXY4Mnltc3RjdGo0cWV1In0.nciMMKY38q8p37kx3uwzmw");
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


    }


    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;

        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                enableLocationComponent(style);
                initSearchFab();
                addUserLocation();

                Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_location_on_24, null);
                Bitmap bitmap = BitmapUtils.getBitmapFromDrawable(drawable);
                style.addImage(symbolIconId, bitmap);
                setUpSource(style);
                setUpLayer(style);
                initSource(style);
                initLayers(style);

                mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
                    LatLng source;
                    @Override
                    public boolean onMapClick(@NonNull LatLng point) {
                        if(c == 0){
                            origin = Point.fromLngLat(point.getLongitude(), point.getLatitude());
                            source = point;
                            MarkerOptions options = new MarkerOptions();
                            options.position(point);
                            options.title("Source");
                            mapboxMap.addMarker(options);
                            reverseGeocodeFunc(point, c);
                        }
                        if(c == 1){
                            destination = Point.fromLngLat(point.getLongitude(), point.getLatitude());
                            getRoute(mapboxMap, origin, destination);
                            MarkerOptions options2 = new MarkerOptions();
                            options2.position(point);
                            options2.title("destination");
                            mapboxMap.addMarker(options2);
                            reverseGeocodeFunc(point, c);
                            getRoute(mapboxMap, origin, destination);

                        }
                        if(c > 1){
                            c = 0;
                            recreate();
                        }
                        c++;
                        return true;
                    }
                });
            }
        });
    }


    private void reverseGeocodeFunc(LatLng point, int c){
        MapboxGeocoding geocode = MapboxGeocoding.builder()
                .accessToken("pk.eyJ1IjoiZmVzaGFydDUwMCIsImEiOiJja2kwcTQyc3MwaXY4Mnltc3RjdGo0cWV1In0.nciMMKY38q8p37kx3uwzmw")
                .query(Point.fromLngLat(point.getLongitude(), point.getLatitude()))
                .geocodingTypes(GeocodingCriteria.TYPE_ADDRESS)
                .build();
        geocode.enqueueCall(new Callback<GeocodingResponse>(){

            @Override
            public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
                List<CarmenFeature> results = response.body().features();
                if(results.size() > 0){
                    CarmenFeature feature;
                    Point firstResultPoint = results.get(0).center();
                    feature = results.get(0);
                    if(c == 0){
                        startLocation += feature.placeName();
                        TextView tv = findViewById(R.id.s);
                        tv.setText(startLocation);
                    }if(c == 1){
                        endLocation += feature.placeName();
                        TextView tv2 = findViewById(R.id.d);
                        tv2.setText(endLocation);
                    }
                    Toast.makeText(MapActivity.this, "" + feature.placeName(), Toast.LENGTH_SHORT).show();


                }else{
                    Toast.makeText(MapActivity.this, "Not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GeocodingResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void initLayers(Style loadedMapStyle) {
        LineLayer routeLayer = new LineLayer(ROUT_LAYER_ID, ROUT_SOURCE_ID);
        routeLayer.setProperties(
                PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
                lineWidth(5f),
                lineColor(Color.parseColor("#009688"))
        );
        loadedMapStyle.addLayer(routeLayer);
        loadedMapStyle.addImage(RED_PIN_ICON_ID, BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.ic_baseline_location_on_24)));

        loadedMapStyle.addLayer(new SymbolLayer(ICON_LAYER_ID, ICON_SOURCE_ID).withProperties(
                iconImage(RED_PIN_ICON_ID),
                PropertyFactory.iconIgnorePlacement(true),
                PropertyFactory.iconAllowOverlap(true),
                iconOffset(new Float[]{0f, -9f})));


    }

    private void initSource(Style loadedMapStyle){
        loadedMapStyle.addSource(new GeoJsonSource(ROUT_SOURCE_ID));

        GeoJsonSource iconGeoJsonSource = new GeoJsonSource(ICON_SOURCE_ID, FeatureCollection.fromFeatures(new Feature[]{
            Feature.fromGeometry(Point.fromLngLat(origin.longitude(), origin.latitude())),
            Feature.fromGeometry(Point.fromLngLat(destination.longitude(), destination.latitude()))}));
        loadedMapStyle.addSource(iconGeoJsonSource);
    }

    private void getRoute(final MapboxMap mapboxMap, Point origin, final Point destination){
        client = MapboxDirections.builder()
                .origin(origin)
                .destination(destination)
                .overview(DirectionsCriteria.OVERVIEW_FULL)
                .profile(DirectionsCriteria.PROFILE_DRIVING)
                .accessToken("pk.eyJ1IjoiZmVzaGFydDUwMCIsImEiOiJja2kwcTQyc3MwaXY4Mnltc3RjdGo0cWV1In0.nciMMKY38q8p37kx3uwzmw")
                .build();
        client.enqueueCall(this);
    }

    private void navigationRoute(){
        NavigationRoute.builder(this)
                .accessToken("pk.eyJ1IjoiZmVzaGFydDUwMCIsImEiOiJja2kwcTQyc3MwaXY4Mnltc3RjdGo0cWV1In0.nciMMKY38q8p37kx3uwzmw")
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>(){


                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        if(response.body() == null){
                            Toast.makeText(MapActivity.this, "No routs found", Toast.LENGTH_SHORT).show();
                            return;
                        }else if(response.body().routes().size() < 1){
                            Toast.makeText(MapActivity.this, "No routes found", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        DirectionsRoute route = response.body().routes().get(0);
                        boolean simulateRoute = true;
                        NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                                .directionsRoute(route)
                                .shouldSimulateRoute(simulateRoute)
                                .build();
                        NavigationLauncher.startNavigation(MapActivity.this, options);

                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable t) {

                    }
                });
    }

    @Override
    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
        if(response.body() == null){
            Toast.makeText(MapActivity.this, "No routs found", Toast.LENGTH_SHORT).show();
            return;
        }else if(response.body().routes().size() < 1){
            Toast.makeText(MapActivity.this, "No routes found", Toast.LENGTH_SHORT).show();
            return;
        }
        final DirectionsRoute currentRoute = response.body().routes().get(0);
        distance = currentRoute.distance() / 1000;
        st = String.format("%.2f K.M", distance);
        TextView dv = findViewById(R.id.distanceView);
        dv.setText(st);
        if(mapboxMap != null){
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    GeoJsonSource source = style.getSourceAs(ROUT_SOURCE_ID);
                    if(source != null){
                        source.setGeoJson(LineString.fromPolyline(currentRoute.geometry(), Constants.PRECISION_6));
                    }
                }
            });
        }
    }

    public void confirmed(View view){
        navigationRoute();
    }

    private void initSearchFab(){
        findViewById(R.id.fab_location_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new PlaceAutocomplete.IntentBuilder()
                        .accessToken(Mapbox.getAccessToken() != null ? Mapbox.getAccessToken() : "pk.eyJ1IjoiZmVzaGFydDUwMCIsImEiOiJja2kwcTQyc3MwaXY4Mnltc3RjdGo0cWV1In0.nciMMKY38q8p37kx3uwzmw")
                        .placeOptions(PlaceOptions.builder()
                                .backgroundColor(Color.parseColor("#EEEEEE"))
                                .limit(10)
                                .addInjectedFeature(home)
                                .addInjectedFeature(work)
                                .build(PlaceOptions.MODE_CARDS))
                        .build(MapActivity.this);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    private void addUserLocation(){
        home = CarmenFeature.builder().text("home")
                .geometry(Point.fromLngLat(54.704514, 20.500506))
                .placeName("Kaliningrad")
                .id("mapbox-sf")
                .properties(new JsonObject())
                .build();
        work = CarmenFeature.builder().text("work")
                .placeName("work")
                .geometry(Point.fromLngLat(54.707967, 20.504336))
                .id("mapbox-dc")
                .properties(new JsonObject())
                .build();
    }

    private void setUpSource(Style loadedMapStyle){
        loadedMapStyle.addSource(new GeoJsonSource(geohsonSourceLayerId));
    }
    private void setUpLayer(Style loadedMapStyle){
        loadedMapStyle.addLayer(new SymbolLayer("SYMBOL_LAYER_ID", geohsonSourceLayerId).withProperties(
                iconImage(symbolIconId),
                iconOffset(new Float[]{0f, -8f})
        ));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            CarmenFeature selectedCarmenFeeature = PlaceAutocomplete.getPlace(data);
            if(mapboxMap != null){
                Style style = mapboxMap.getStyle();
                if(style != null){
                    GeoJsonSource source = style.getSourceAs(geohsonSourceLayerId);
                    if(source != null){
                        source.setGeoJson(FeatureCollection.fromFeatures(
                                new Feature[]{Feature.fromJson(selectedCarmenFeeature.toJson())}));
                    }
                    mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                            .target(new LatLng(((Point) selectedCarmenFeeature.geometry()).latitude(),
                                    ((Point) selectedCarmenFeeature.geometry()).longitude()))
                            .zoom(14)
                            .build()), 4000);

                }
            }
        }
    }

    private void enableLocationComponent(Style loadedMapStyle){
        if(PermissionsManager.areLocationPermissionsGranted(MapActivity.this)){
            LocationComponent locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(MapActivity.this, loadedMapStyle).build()
            );
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                return;
            }
            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setCameraMode(CameraMode.TRACKING);
            locationComponent.setRenderMode(RenderMode.COMPASS);
        }else{
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onFailure(Call<DirectionsResponse> call, Throwable t) {

    }


    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if(granted){
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        }else{
            finish();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

}