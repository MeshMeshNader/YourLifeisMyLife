package com.abir.yourlifeismylife.User.Home;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.abir.yourlifeismylife.R;
import com.abir.yourlifeismylife.Utils.Common;
import com.abir.yourlifeismylife.Utils.MarkerData;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, ValueEventListener {

    MarkerData mMarkerData;
    FusedLocationProviderClient mFusedLocationProviderClient;
    float DEFALT_ZOOM = 15f;
    DatabaseReference trackingUserLocation;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapp);
        mapFragment.getMapAsync(this);


        checkPermmisions();
        regesterEventRealTime();


        //mMarkerData = (MarkerData) getIntent().getExtras().getSerializable("markerObj");

    }

    private void regesterEventRealTime() {
        trackingUserLocation = FirebaseDatabase.getInstance()
                .getReference(Common.PUBLIC_LOCATION)
                .child("JrgldSEyCZNf98xCrAktB6TtKd42");
        trackingUserLocation.addValueEventListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        getDeviceLocation();
        mMap.setMyLocationEnabled(true);


    }


    public void getDeviceLocation() {

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {

            Task location = mFusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Log.e("Success", "done getting location in map ");


                        android.location.Location currentLocation = (android.location.Location) task.getResult();

                        if (Common.trackingUser.getUserID() == null)
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFALT_ZOOM);

                        Log.e("Success", "This is the location of the res");


                    } else {
                        String message = task.getException().toString();
                        Log.e("Error:", message);
                    }
                }
            });

        } catch (SecurityException e) {

        }

    }


    private void moveCamera(LatLng latLng, float zoom) {

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

    }

    protected GoogleMap createMarker(double latitude, double longitude,
                                     String title, String snippet, int iconID) {
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title(title)
                .snippet(snippet)
                .icon(BitmapDescriptorFactory.fromResource(iconID)));
        moveCamera(new LatLng(latitude, longitude),
                DEFALT_ZOOM);

        return mMap;
    }


    @AfterPermissionGranted(1111)
    private boolean checkPermmisions() {

        String[] locationPermmsions = new String[0];
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            locationPermmsions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION
                    , Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION};
        if (EasyPermissions.hasPermissions(this, locationPermmsions)) {
            return true;
        } else {
            EasyPermissions.requestPermissions(this, "Location Access"
                    , 1111, locationPermmsions);
        }
        return false;
    }

    @Override
    protected void onStop() {
        trackingUserLocation.removeEventListener(this);
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        trackingUserLocation.addValueEventListener(this);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if (snapshot.getValue() != null) {
            MyLocation location = snapshot.getValue(MyLocation.class);
            mMarkerData = new MarkerData(location.getLatitude()
                    , location.getLongitude()
                    , Common.trackingUser.getFirstName() + Common.trackingUser.getLastName()
                    , Common.getDateFormatted(Common.convertTimeStampToDate(location.getTime()))
                    , R.drawable.pin32);

            createMarker(mMarkerData.getLatitude(), mMarkerData.getLongitude()
                    , mMarkerData.getTitle(), mMarkerData.getSnippet()
                    , mMarkerData.getIconResId());

        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

}