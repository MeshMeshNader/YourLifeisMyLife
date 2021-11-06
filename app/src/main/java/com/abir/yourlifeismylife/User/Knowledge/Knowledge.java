package com.abir.yourlifeismylife.User.Knowledge;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.abir.yourlifeismylife.Adapters.ViewPagerAdapter;
import com.abir.yourlifeismylife.R;
import com.abir.yourlifeismylife.User.Knowledge.KnowledgePages.KnowledgeFifthPage;
import com.abir.yourlifeismylife.User.Knowledge.KnowledgePages.KnowledgeFirstPage;
import com.abir.yourlifeismylife.User.Knowledge.KnowledgePages.KnowledgeFourthPage;
import com.abir.yourlifeismylife.User.Knowledge.KnowledgePages.KnowledgeSecondPage;
import com.abir.yourlifeismylife.User.Knowledge.KnowledgePages.KnowledgeSixthPage;
import com.abir.yourlifeismylife.User.Knowledge.KnowledgePages.KnowledgeThirdPage;

public class Knowledge extends FragmentActivity {

    public static ViewPager mViewPager;
    public static ViewPagerAdapter mViewPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge);


        mViewPager = (ViewPager) findViewById(R.id.knowledge_pager);
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());


        mViewPagerAdapter.addFragment(new KnowledgeFirstPage());
        mViewPagerAdapter.addFragment(new KnowledgeSecondPage());
        mViewPagerAdapter.addFragment(new KnowledgeThirdPage());
        mViewPagerAdapter.addFragment(new KnowledgeFourthPage());
        mViewPagerAdapter.addFragment(new KnowledgeFifthPage());
        mViewPagerAdapter.addFragment(new KnowledgeSixthPage());

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