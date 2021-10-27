package com.abir.yourlifeismylife.Intro;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.abir.yourlifeismylife.Adapters.ViewPagerAdapter;
import com.abir.yourlifeismylife.Intro.IntroPages.IntroFirstPage;
import com.abir.yourlifeismylife.Intro.IntroPages.IntroFourthPage;
import com.abir.yourlifeismylife.Intro.IntroPages.IntroSecondPage;
import com.abir.yourlifeismylife.Intro.IntroPages.IntroThirdPage;
import com.abir.yourlifeismylife.R;


public class Intro extends AppCompatActivity {


    public static ViewPager mViewPager;
    public static ViewPagerAdapter mViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);


        mViewPager = (ViewPager) findViewById(R.id.intro_pager);
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());


        mViewPagerAdapter.addFragment(new IntroFirstPage());
        mViewPagerAdapter.addFragment(new IntroSecondPage());
        mViewPagerAdapter.addFragment(new IntroThirdPage());
        mViewPagerAdapter.addFragment(new IntroFourthPage());

        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setCurrentItem(0, true);


    }

    public void loadOutFragmentBack() {
        mViewPager.setCurrentItem(getItem(-1), true);
    }

    public void loadOutFragmentForward() {
        mViewPager.setCurrentItem(getItem(+1), true);
    }

    public void loadOutFragmentSpecific(int position) {
        mViewPager.setCurrentItem(position, true);
    }

    private int getItem(int i) {
        return mViewPager.getCurrentItem() + i;
    }


}