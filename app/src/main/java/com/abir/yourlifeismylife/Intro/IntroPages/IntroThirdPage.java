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


public class IntroThirdPage extends Fragment {

    View view, firstPage, secondPage, fourthPage;
    ImageView mNextBtn, mPrevBtn;
    Intro mIntro;
    Button mGetStarted;
    TextView mSignInBtn;

    public IntroThirdPage() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_third_page, container, false);

        initViews();

        return view;
    }

    private void initViews() {
        mIntro = new Intro();

        firstPage = view.findViewById(R.id.intro_first_box);
        secondPage = view.findViewById(R.id.intro_second_box);
        fourthPage = view.findViewById(R.id.intro_fourth_box);


        firstPage.setOnClickListener(v -> mIntro.loadOutFragmentSpecific(0));
        secondPage.setOnClickListener(v -> mIntro.loadOutFragmentSpecific(1));
        fourthPage.setOnClickListener(v -> mIntro.loadOutFragmentSpecific(3));


        mNextBtn = view.findViewById(R.id.next_page_image);
        mNextBtn.setOnClickListener(v -> nextFragment());

        mPrevBtn = view.findViewById(R.id.prev_page_image);
        mPrevBtn.setOnClickListener(v -> prevFragment());

        mGetStarted = view.findViewById(R.id.get_started_btn);
        mSignInBtn = view.findViewById(R.id.intro_sign_in);

        mGetStarted.setOnClickListener(v -> getStarted());
        mSignInBtn.setOnClickListener(v -> signIn());


    }


    private void getStarted() {
        Intent x = new Intent(getActivity(), Signup.class);
        startActivity(x);
        getActivity().finish();
    }

    private void signIn() {
        Intent x = new Intent(getActivity(), Login.class);
        startActivity(x);
        getActivity().finish();
    }


    private void prevFragment() {
        mIntro.loadOutFragmentBack();
    }

    private void nextFragment() {
        mIntro.loadOutFragmentForward();
    }


}