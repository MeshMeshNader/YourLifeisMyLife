package com.abir.yourlifeismylife.User;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.Toast;

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


    KTLoadingButton mLocationBtn, mDopamineBtn, mSerotoninBtn, mPsychologicalBtn;
    Button mContinue;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);


        initViews();

    }

    private void initViews() {

        mLocationBtn = findViewById(R.id.location_permissions_btn);
        mDopamineBtn = findViewById(R.id.dopamine_permissions_btn);
        mSerotoninBtn = findViewById(R.id.serotonin_permissions_btn);
        mPsychologicalBtn = findViewById(R.id.psychological_permissions_btn);
        mContinue = findViewById(R.id.permissions_continue_btn);

        mDopamineBtn.setOnClickListener(v -> dopaminePermission());
        mSerotoninBtn.setOnClickListener(v -> serotoninPermission());
        mPsychologicalBtn.setOnClickListener(v -> psychologicalPermission());
        mLocationBtn.setOnClickListener(v -> getLocationPermission());
        mContinue.setOnClickListener(v -> goToMeasurement());
    }

    private void psychologicalPermission() {
        mPsychologicalBtn.startLoading();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mPsychologicalBtn.doResult(true, new Function1<KTLoadingButton, Unit>() {
                    @Override
                    public Unit invoke(KTLoadingButton ktLoadingButton) {
                        mPsychologicalBtn.setClickable(false);
                        i++;
                        return null;
                    }
                });
            }
        }, 700);
    }

    private void serotoninPermission() {
        mSerotoninBtn.startLoading();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSerotoninBtn.doResult(true, new Function1<KTLoadingButton, Unit>() {
                    @Override
                    public Unit invoke(KTLoadingButton ktLoadingButton) {
                        mSerotoninBtn.setClickable(false);
                        i++;
                        return null;
                    }
                });
            }
        }, 700);
    }

    private void dopaminePermission() {
        mDopamineBtn.startLoading();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mDopamineBtn.doResult(true, new Function1<KTLoadingButton, Unit>() {
                    @Override
                    public Unit invoke(KTLoadingButton ktLoadingButton) {
                        mDopamineBtn.setClickable(false);
                        i++;
                        return null;
                    }
                });
            }
        }, 700);
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
                    , Manifest.permission.ACCESS_COARSE_LOCATION};
        if (EasyPermissions.hasPermissions(this, locationPermmsions)) {
            mLocationBtn.setClickable(false);
            i++;
            mLocationBtn.doResult(true, new Function1<KTLoadingButton, Unit>() {
                @Override
                public Unit invoke(KTLoadingButton ktLoadingButton) {

                    return null;
                }
            });
            return true;
        } else {
            EasyPermissions.requestPermissions(this, "Please, Make Location Access All The Time"
                    , 1111, locationPermmsions);
        }
        return false;
    }

    private void locationDoResult() {
    }


    private void goToMeasurement() {
        if (i >= 4) {
            Intent measure = new Intent(Permissions.this, Measurement.class);
            measure.putExtra("finish", 0);
            startActivity(measure);
            finish();
        } else {
            Toast.makeText(this, "Please Enable All Permissions", Toast.LENGTH_SHORT).show();
        }
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
                    i++;
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