package com.abir.yourlifeismylife.User.Knowledge.KonwledgePages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.abir.yourlifeismylife.Intro.IntroPages.IntroFirstPage;
import com.abir.yourlifeismylife.Intro.IntroPages.IntroThirdPage;
import com.abir.yourlifeismylife.R;
import com.abir.yourlifeismylife.User.Knowledge.Knowledge;


public class KnowledgeSecondPage extends Fragment {

    View view, firstPage, thirdPage;
    ImageView mNextBtn, mPrevBtn;
    Knowledge mKnowledge;
    Button mNext;

    public KnowledgeSecondPage() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_knowledge_second_page, container, false);

        initViews();

        return view;
    }

    private void initViews() {

        mKnowledge = new Knowledge();

        firstPage = view.findViewById(R.id.intro_first_box);
        thirdPage = view.findViewById(R.id.intro_third_box);

        firstPage.setOnClickListener(v -> mKnowledge.loadOutFragment(new KnowledgeFirstPage(), getContext()));
        thirdPage.setOnClickListener(v -> mKnowledge.loadOutFragment(new KnowledgeThirdPage(), getContext()));


        mNextBtn = view.findViewById(R.id.next_page_image);
        mNextBtn.setOnClickListener(v -> nextFragment());

        mPrevBtn = view.findViewById(R.id.prev_page_image);
        mPrevBtn.setOnClickListener(v -> prevFragment());

        mNext = view.findViewById(R.id.next_btn);
        mNext.setOnClickListener(v -> nextFragment());

    }

    private void prevFragment() {
        mKnowledge.loadOutFragment(new KnowledgeFirstPage(), getActivity());
    }

    private void nextFragment() {
        mKnowledge.loadOutFragment(new KnowledgeThirdPage(), getActivity());
    }

}