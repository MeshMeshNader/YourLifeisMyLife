package com.abir.yourlifeismylife;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.abir.yourlifeismylife.DataModels.UserDataModel;
import com.abir.yourlifeismylife.Intro.Intro;
import com.abir.yourlifeismylife.User.Home.Home;
import com.abir.yourlifeismylife.Utils.Common;
import com.abir.yourlifeismylife.Utils.NotificationReceiver;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;

public class Splash extends AppCompatActivity {


    FirebaseAuth mAuth;
    DatabaseReference User;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();
        User = FirebaseDatabase.getInstance().getReference(Common.USERS_INFORMATION);
        splashTimer();

        myAlarm();

    }


    private void splashTimer() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkUser();

            }
        }, 500);
    }

    void checkUser() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            sendUserToLogin();
        } else {
            User.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Common.trackingUser = snapshot.getValue(UserDataModel.class);
                        sendUserToHome();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

    void sendUserToLogin() {
        startActivity(new Intent(Splash.this, Intro.class));
        finish();
    }


    void sendUserToHome() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        Intent x = new Intent(Splash.this, Home.class);
        x.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        x.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        x.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        x.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(x);
        finish();
    }

    public void myAlarm() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.getTime().compareTo(new Date()) < 0)
            calendar.add(Calendar.DAY_OF_MONTH, 1);

        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        //alarmManager.setExact(AlarmManager.RTC_WAKEUP , calendar.getTimeInMillis() ,pendingIntent);
        }

    }

}