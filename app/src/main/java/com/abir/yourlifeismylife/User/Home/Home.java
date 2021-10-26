package com.abir.yourlifeismylife.User.Home;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home extends AppCompatActivity {

    FirebaseAuth mAuth;
//    TextView mCode;
//    Button mMap, mJoin;

    DrawerLayout mDrawer;
    ImageView mMenuDrawerBtn;
    TextView mUserName, mCircleCode, mJoinCircle, mMyCircle, mMeasurements, mShareLocation, mEditAccount, mLogOut;
    private CircleImageView mProfileImage;

    CustomProgress mCustomProgress;
    String fullName, image = "", circleCode;

    DatabaseReference User;
    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();

    }

    private void initViews() {
        mAuth = FirebaseAuth.getInstance();
        mCustomProgress = CustomProgress.getInstance();
        //mLogOut = findViewById(R.id.logout_btn);
//        mMap = findViewById(R.id.map_btn);
//        mCode = findViewById(R.id.code_text);
//        mJoin = findViewById(R.id.join_btn);
//
//        mJoin.setOnClickListener(v -> openJoin());
//        mMap.setOnClickListener(v -> openMap());
        //mLogOut.setOnClickListener(v -> logout());

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
    }

    private void openMeasurments() {
        Intent join = new Intent(Home.this, Measurement.class);
        startActivity(join);
    }

    private void openEditAccount() {
        Intent edit = new Intent(Home.this, EditProfile.class);
        startActivity(edit);
    }

    private void shareLocation() {
    }

    private void openMap() {
        Common.trackingUser = new UserDataModel();
        Intent MapView = new Intent(Home.this, MapsActivity.class);
        startActivity(MapView);
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
        }
    }

}