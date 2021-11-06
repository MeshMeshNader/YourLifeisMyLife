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


public class KnowledgeFourthPage extends Fragment {

    View view;
    View firstPage, secondPage, thirdPage, fifthPage;
    ImageView mNextBtn, mPrevBtn;
    Knowledge mKnowledge;
    Button mNext;

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
        mKnowledge = new Knowledge();

        firstPage = view.findViewById(R.id.intro_first_box);
        secondPage = view.findViewById(R.id.intro_second_box);
        thirdPage = view.findViewById(R.id.intro_third_box);
        fifthPage = view.findViewById(R.id.intro_fifth_box);

        firstPage.setOnClickListener(v -> mKnowledge.loadOutFragmentSpecific(0));
        secondPage.setOnClickListener(v -> mKnowledge.loadOutFragmentSpecific(1));
        thirdPage.setOnClickListener(v -> mKnowledge.loadOutFragmentSpecific(2));
        fifthPage.setOnClickListener(v -> mKnowledge.loadOutFragmentSpecific(4));

        mNextBtn = view.findViewById(R.id.next_page_image);
        mNextBtn.setOnClickListener(v -> nextFragment());

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