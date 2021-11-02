package com.abir.yourlifeismylife.Intro.IntroPages;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.abir.yourlifeismylife.Auth.Login;
import com.abir.yourlifeismylife.Auth.Signup;
import com.abir.yourlifeismylife.Intro.Intro;
import com.abir.yourlifeismylife.R;


public class IntroFirstPage extends Fragment {

    View view, secondPage, thirdPage, fourthPage;
    ImageView mNextBtn;
    Intro mIntro;
    Button mGetStarted;
    TextView mSignInBtn;


    public IntroFirstPage() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_first_page, container, false);

        initViews();

        return view;
    }


    private void initViews() {

        mIntro = new Intro();

        secondPage = view.findViewById(R.id.intro_second_box);
        thirdPage = view.findViewById(R.id.intro_third_box);
        fourthPage = view.findViewById(R.id.intro_fourth_box);


        secondPage.setOnClickListener(v -> mIntro.loadOutFragmentSpecific(1));
        thirdPage.setOnClickListener(v -> mIntro.loadOutFragmentSpecific(2));
        fourthPage.setOnClickListener(v -> mIntro.loadOutFragmentSpecific(3));


        mNextBtn = view.findViewById(R.id.next_page_image);
        mNextBtn.setOnClickListener(v -> nextFragment());

        mGetStarted = view.findViewById(R.id.get_started_btn);
        mSignInBtn = view.findViewById(R.id.intro_sign_in);

        mGetStarted.setOnClickListener(v -> getStarted());
        mSignInBtn.setOnClickListener(v -> signIn());


    }


    private void getStarted() {
        Intent x = new Intent(getActivity(), Signup.class);
        startActivity(x);
    }

    private void signIn() {
        Intent x = new Intent(getActivity(), Login.class);
        startActivity(x);

    }

    private void nextFragment() {
        mIntro.loadOutFragmentForward();
    }
}