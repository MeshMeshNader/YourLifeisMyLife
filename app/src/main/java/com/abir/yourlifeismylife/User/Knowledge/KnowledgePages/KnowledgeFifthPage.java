package com.abir.yourlifeismylife.User.Knowledge.KnowledgePages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.abir.yourlifeismylife.R;
import com.abir.yourlifeismylife.User.Knowledge.Knowledge;


public class KnowledgeFifthPage extends Fragment {

    View view;
    View firstPage, secondPage, thirdPage, fourthPage;
    ImageView mPrevBtn;
    Knowledge mKnowledge;
    Button mNext;

    public KnowledgeFifthPage() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_knowledge_fifth_page, container, false);

        initViews();

        return view;
    }

    private void initViews() {
        mKnowledge = new Knowledge();

        firstPage = view.findViewById(R.id.intro_first_box);
        secondPage = view.findViewById(R.id.intro_second_box);
        thirdPage = view.findViewById(R.id.intro_third_box);
        fourthPage = view.findViewById(R.id.intro_fourth_box);

        firstPage.setOnClickListener(v -> mKnowledge.loadOutFragmentSpecific(0));
        secondPage.setOnClickListener(v -> mKnowledge.loadOutFragmentSpecific(1));
        thirdPage.setOnClickListener(v -> mKnowledge.loadOutFragmentSpecific(2));
        fourthPage.setOnClickListener(v -> mKnowledge.loadOutFragmentSpecific(3));


        mPrevBtn = view.findViewById(R.id.prev_page_image);
        mPrevBtn.setOnClickListener(v -> prevFragment());

        mNext = view.findViewById(R.id.next_btn);
        mNext.setOnClickListener(v -> nextFragment());
    }

    private void prevFragment() {
        mKnowledge.loadOutFragmentBack();
    }

    private void nextFragment() {
        mKnowledge.loadOutFragmentForward();
    }

}