package com.abir.yourlifeismylife.User.Home;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abir.yourlifeismylife.Adapters.UserAdapter;
import com.abir.yourlifeismylife.DataModels.UserDataModel;
import com.abir.yourlifeismylife.R;
import com.abir.yourlifeismylife.Utils.Common;
import com.abir.yourlifeismylife.Utils.CustomProgress;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyCircle extends AppCompatActivity {


    ArrayList<UserDataModel> mUsersList;
    UserDataModel dataModel;
    RecyclerView mUsersRecyclerView;
    LinearLayoutManager layoutManager;
    UserAdapter userAdapter;
    CustomProgress mCustomProgress = CustomProgress.getInstance();
    TextView mNoResults;
    String circleMemberId = "";


    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference UsersRef;
    DatabaseReference CircleRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_circle);

        initViews();

    }

    private void initViews() {
        mUsersList = new ArrayList<>();

        mNoResults = findViewById(R.id.users_no_result);

        mUsersRecyclerView = findViewById(R.id.my_circle_recyclerview);
        mUsersRecyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(MyCircle.this, RecyclerView.VERTICAL, false);
        mUsersRecyclerView.setLayoutManager(layoutManager);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        CircleRef = FirebaseDatabase.getInstance().getReference(Common.USERS_INFORMATION).child(user.getUid())
                .child(Common.USER_CIRCLE_MEMBERS);


        UsersRef = FirebaseDatabase.getInstance().getReference(Common.USERS_INFORMATION);
        userAdapter = new UserAdapter(MyCircle.this, mUsersList);
        mUsersRecyclerView.setAdapter(userAdapter);


        getData();

    }

    void getData() {
        CircleRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUsersList.clear();

                if (snapshot.exists()) {
                    for (DataSnapshot dss : snapshot.getChildren()) {
                        circleMemberId = dss.child("circleMemberId").getValue(String.class);
                        UsersRef.child(circleMemberId)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        dataModel = snapshot.getValue(UserDataModel.class);
                                        mUsersList.add(dataModel);
                                        userAdapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                    }
                    if (mNoResults.getVisibility() == View.VISIBLE)
                        mNoResults.setVisibility(View.GONE);
                    showUsers();
                } else {
                    mNoResults.setVisibility(View.VISIBLE);
                    mCustomProgress.hideProgress();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showUsers() {
        userAdapter = new UserAdapter(MyCircle.this, mUsersList);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mUsersRecyclerView.setAdapter(userAdapter);
        mCustomProgress.hideProgress();
    }

}


