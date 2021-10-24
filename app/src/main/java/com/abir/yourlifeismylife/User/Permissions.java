package com.abir.yourlifeismylife.User;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.abir.yourlifeismylife.R;

public class Permissions extends AppCompatActivity {


    Button mContinue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);


        initViews();

    }

    private void initViews() {
        mContinue = findViewById(R.id.permissions_continue_btn);
        mContinue.setOnClickListener(v -> goToPermissions());
    }

    private void goToPermissions() {
        startActivity(new Intent(Permissions.this, Measurement.class));
        finish();
    }


}