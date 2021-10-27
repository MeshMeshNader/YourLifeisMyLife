package com.abir.yourlifeismylife.User.Knowledge.KonwledgePages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.abir.yourlifeismylife.R;
import com.abir.yourlifeismylife.User.Knowledge.Knowledge;

public class KnowledgeFirstPage extends Fragment {


    View view;
    View secondPage, thirdPage;
    ImageView mNextBtn;
    Knowledge mKnowledge;
    Button mNext;

    public KnowledgeFirstPage() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_knowledge_first_page, container, false);

        initViews();

        return view;

    }

    private void initViews() {

        mKnowledge = new Knowledge();

        secondPage = view.findViewById(R.id.intro_second_box);
        thirdPage = view.findViewById(R.id.intro_third_box);

        secondPage.setOnClickListener(v -> mKnowledge.loadOutFragmentSpecific(1));
        thirdPage.setOnClickListener(v -> mKnowledge.loadOutFragmentSpecific(2));

        mNextBtn = view.findViewById(R.id.next_page_image);
        mNextBtn.setOnClickListener(v -> nextFragment());

        mNext = view.findViewById(R.id.next_btn);
        mNext.setOnClickListener(v -> nextFragment());

    }


    private void nextFragment() {
        mKnowledge.loadOutFragmentForward();
    }


}