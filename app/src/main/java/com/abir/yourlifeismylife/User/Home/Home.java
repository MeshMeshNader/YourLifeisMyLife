package com.abir.yourlifeismylife.User.Home;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.abir.yourlifeismylife.DataModels.UserDataModel;
import com.abir.yourlifeismylife.R;
import com.abir.yourlifeismylife.Service.MyLocationReceiver;
import com.abir.yourlifeismylife.Splash;
import com.abir.yourlifeismylife.User.EditProfile;
import com.abir.yourlifeismylife.User.Measurement;
import com.abir.yourlifeismylife.Utils.Common;
import com.abir.yourlifeismylife.Utils.CustomProgress;
import com.abir.yourlifeismylife.Utils.MarkerData;
import com.abir.yourlifeismylife.Utils.NotificationReceiver;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class Home extends AppCompatActivity implements OnMapReadyCallback, ValueEventListener {

    FirebaseAuth mAuth;

    DrawerLayout mDrawer;
    ImageView mMenuDrawerBtn;
    TextView mUserName, mCircleCode, mJoinCircle, mMyCircle, mMeasurements, mShareLocation, mEditAccount, mLogOut;
    CustomProgress mCustomProgress;
    String fullName, image = "", circleCode;
    DatabaseReference User;
    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationProviderClient;
    MarkerData mMarkerData;
    FusedLocationProviderClient mFusedLocationProviderClient;
    float DEFALT_ZOOM = 15f;
    DatabaseReference trackingUserLocation;
    private CircleImageView mProfileImage;
    private GoogleMap mMap;
    private long backPressedTime;
    private Toast backToast;
    LatLng currentUserLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapp);
        mapFragment.getMapAsync(this);

        initViews();

        checkPermmisions();
        regesterEventRealTime();


    }

    private void initViews() {
        mAuth = FirebaseAuth.getInstance();
        mCustomProgress = CustomProgress.getInstance();

        User = FirebaseDatabase.getInstance().getReference(Common.USERS_INFORMATION);


        mMenuDrawerBtn = findViewById(R.id.menu_btn);
        mMenuDrawerBtn.setOnClickListener(v -> openMenu());
        mDrawer = findViewById(R.id.drawer_layout);

        mUserName = findViewById(R.id.user_name_nav);
        mProfileImage = findViewById(R.id.profile_picture_nav);
        mCircleCode = findViewById(R.id.user_circle_id_nav);


        mJoinCircle = findViewById(R.id.join_circle_nav);
        mMyCircle = findViewById(R.id.my_circle_nav);
        mMeasurements = findViewById(R.id.measurements_nav);
        mShareLocation = findViewById(R.id.share_location_nav);
        mEditAccount = findViewById(R.id.edit_acc_nav);
        mLogOut = findViewById(R.id.log_out_nav);

        mProfileImage.setOnClickListener(v -> openEditAccount());
        mUserName.setOnClickListener(v -> openEditAccount());
        mJoinCircle.setOnClickListener(v -> openJoin());
        mMyCircle.setOnClickListener(v -> openMyCircle());
        mMeasurements.setOnClickListener(v -> openMeasurments());
        mShareLocation.setOnClickListener(v -> shareLocation());
        mEditAccount.setOnClickListener(v -> openEditAccount());
        mLogOut.setOnClickListener(v -> logout());

        getData();
        updateLocation();

    }

    private void openMenu() {
        mDrawer.openDrawer(Gravity.LEFT);
    }

    private void openJoin() {
        Intent join = new Intent(Home.this, JoinCircle.class);
        startActivity(join);
    }

    private void openMyCircle() {
        Intent myCircle = new Intent(Home.this, MyCircle.class);
        startActivity(myCircle);
    }

    private void openMeasurments() {
        Intent measure = new Intent(Home.this, Measurement.class);
        measure.putExtra("finish", 1);
        startActivity(measure);
    }

    private void openEditAccount() {
        Intent edit = new Intent(Home.this, EditProfile.class);
        startActivity(edit);
    }

    private void shareLocation() {

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {

            Task location = mFusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Log.e("Success", "done getting location in map ");


                        android.location.Location currentLocation = (android.location.Location) task.getResult();


                        currentUserLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                        String uri = "https://www.google.com/maps/search/?api=1&query=" + currentLocation.getLatitude()+ "," +currentLocation.getLongitude();
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("text/plain");
                        i.putExtra(Intent.EXTRA_TEXT, "My Location is: " + uri);
                        startActivity(Intent.createChooser(i, "Share Using: "));
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


    private void logout() {
        mAuth.signOut();
        Intent userLogout = new Intent(Home.this, Splash.class);
        userLogout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        userLogout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(userLogout);
        this.finish();

    }


    private void getData() {

        mCustomProgress.showProgress(this, "Please Wait", true);

        User.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    UserDataModel userData = snapshot.getValue(UserDataModel.class);
                    fullName = userData.getFirstName() + " " + userData.getLastName();
                    circleCode = userData.getCircleCode();
                    image = userData.getProfileImage();
                    mUserName.setText(fullName);
                    mCircleCode.setText("Circle Code: " + circleCode);
                    try {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (image.equals("none") || image.equals("") || image == null)
                                    Glide.with(getApplicationContext()).load(R.drawable.user).into(mProfileImage);
                                else
                                    Glide.with(getApplicationContext()).load(image).into(mProfileImage);
                            }
                        }, 100);
                    } catch (Exception e) {
                        Log.e("Home", "onDataChange: getData : " + e.getMessage());
                    }

                    mCustomProgress.hideProgress();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mCustomProgress.hideProgress();
    }


    private void updateLocation() {
        buildLocationRequest();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, getPendingIntent());
    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(Home.this, MyLocationReceiver.class);
        intent.setAction(MyLocationReceiver.ACTION);
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setSmallestDisplacement(10f);
        locationRequest.setFastestInterval(3000);
        locationRequest.setInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(Gravity.LEFT)) {
            mDrawer.closeDrawer(Gravity.LEFT);
            return;
        } else if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }

        backPressedTime = System.currentTimeMillis();
    }


    private void regesterEventRealTime() {
        trackingUserLocation = FirebaseDatabase.getInstance()
                .getReference(Common.PUBLIC_LOCATION)
                .child(Common.trackingUser.getUserID());
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

                        if (Common.trackingUser.getUserID() == null) {
                            currentUserLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                            //  moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFALT_ZOOM);
                        }
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
            EasyPermissions.requestPermissions(this, "Please, Make Location Access All The Time"
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
                    , Common.trackingUser.getFirstName() + " " + Common.trackingUser.getLastName()
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

}