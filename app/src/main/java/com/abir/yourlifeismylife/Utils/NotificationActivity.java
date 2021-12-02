package com.abir.yourlifeismylife.Utils;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.abir.yourlifeismylife.R;
import com.abir.yourlifeismylife.Splash;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Intent intent = new Intent(NotificationActivity.this , Splash.class);
        startActivity(intent);
        finish();
    }
}