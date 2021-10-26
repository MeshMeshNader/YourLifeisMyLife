package com.abir.yourlifeismylife.User;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.abir.yourlifeismylife.R;

import java.util.List;

import de.timonknispel.ktloadingbutton.KTLoadingButton;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class Permissions extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {


    KTLoadingButton mLocationBtn;
    Button mContinue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);


        initViews();

    }

    private void initViews() {

        mLocationBtn = findViewById(R.id.location_btn);
        mContinue = findViewById(R.id.permissions_continue_btn);

        mLocationBtn.setOnClickListener(v -> getLocationPermission());
        mContinue.setOnClickListener(v -> goToMeasurement());
    }

    private void getLocationPermission() {
        mLocationBtn.startLoading();
        checkPermmisions();
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


    private void goToMeasurement() {
        startActivity(new Intent(Permissions.this, Measurement.class));
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if (requestCode == 1111) {
            mLocationBtn.doResult(true, new Function1<KTLoadingButton, Unit>() {
                @Override
                public Unit invoke(KTLoadingButton ktLoadingButton) {
                    mLocationBtn.setClickable(false);
                    return null;
                }
            });
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (requestCode == 1111) {
            mLocationBtn.doResult(false, new Function1<KTLoadingButton, Unit>() {
                @Override
                public Unit invoke(KTLoadingButton ktLoadingButton) {
                    mLocationBtn.setClickable(true);
                    return null;
                }
            });
        }
    }
}