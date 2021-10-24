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


public class KnowledgeThirdPage extends Fragment {

    View view, firstPage, secondPage;
    ImageView mPrevBtn;
    Knowledge mKnowledge;
    Button mNext;


    public KnowledgeThirdPage() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_knowledge_third_page, container, false);

        initViews();

        return view;

    }

    private void initViews() {
        mKnowledge = new Knowledge();

        firstPage = view.findViewById(R.id.intro_first_box);
        secondPage = view.findViewById(R.id.intro_second_box);

        firstPage.setOnClickListener(v -> mKnowledge.loadOutFragment(new KnowledgeFirstPage(), getContext()));
        secondPage.setOnClickListener(v -> mKnowledge.loadOutFragment(new KnowledgeSecondPage(), getContext()));


        mPrevBtn = view.findViewById(R.id.prev_page_image);
        mPrevBtn.setOnClickListener(v -> prevFragment());

        mNext = view.findViewById(R.id.next_btn);
        mNext.setOnClickListener(v -> nextFragment());
    }

    private void prevFragment() {
        mKnowledge.loadOutFragment(new KnowledgeSecondPage(), getActivity());
    }

    private void nextFragment() {
        mKnowledge.loadOutFragment(new KnowledgeFourthPage(), getActivity());
    }

}