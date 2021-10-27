package com.abir.yourlifeismylife.User.Knowledge.KonwledgePages;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.abir.yourlifeismylife.R;
import com.abir.yourlifeismylife.User.Home.Home;


public class KnowledgeFourthPage extends Fragment {


    View view;
    Button mContinue;

    public KnowledgeFourthPage() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_knowledge_fourth_page, container, false);

        initViews();

        return view;


    }

    private void initViews() {
        mContinue = view.findViewById(R.id.continue_btn);
        mContinue.setOnClickListener(v -> goToHomePage());
    }

    private void goToHomePage() {
        startActivity(new Intent(getActivity(), Home.class));
        getActivity().finish();
    }


}