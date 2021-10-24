package com.abir.yourlifeismylife.User.Home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.abir.yourlifeismylife.R;
import com.abir.yourlifeismylife.Splash;
import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity {

    FirebaseAuth mAuth;
    Button mLogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();

    }

    private void initViews() {
        mAuth = FirebaseAuth.getInstance();
        mLogOut = findViewById(R.id.logout_btn);

        mLogOut.setOnClickListener(v -> logout());
    }


    private void logout() {
        mAuth.signOut();
        Intent userLogout = new Intent(Home.this, Splash.class);
        userLogout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        userLogout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(userLogout);
        this.finish();

    }


}