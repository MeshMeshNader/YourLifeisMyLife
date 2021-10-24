package com.abir.yourlifeismylife.Intro;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.abir.yourlifeismylife.Intro.IntroPages.IntroFirstPage;
import com.abir.yourlifeismylife.R;
import com.abir.yourlifeismylife.Splash;


public class Intro extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        loadFragment(new IntroFirstPage());
    }



    public void loadOutFragment(Fragment fragment, Context context) {
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
       // transaction.setCustomAnimations(R.anim.enter_from_right, 0, 0, 0);
        transaction.replace(R.id.intro_page_container, fragment).commit();
    }

    void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_down, 0, 0, 0);
        transaction.replace(R.id.intro_page_container, fragment).commit();
    }


}