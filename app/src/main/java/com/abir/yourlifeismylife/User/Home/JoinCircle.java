package com.abir.yourlifeismylife.User.Home;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.abir.yourlifeismylife.DataModels.CircleJoin;
import com.abir.yourlifeismylife.DataModels.UserDataModel;
import com.abir.yourlifeismylife.R;
import com.abir.yourlifeismylife.Utils.Common;
import com.goodiebag.pinview.Pinview;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class JoinCircle extends AppCompatActivity {

    Pinview mCodePinView;
    Button mJoinButton;
    DatabaseReference UsersRef, CurrentUserRef, CircleRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String current_user_id, join_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_circle);

        initView();

    }

    private void initView() {


        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        UsersRef = FirebaseDatabase.getInstance().getReference(Common.USERS_INFORMATION);
        CurrentUserRef = FirebaseDatabase.getInstance().getReference(Common.USERS_INFORMATION).child(mUser.getUid());
        current_user_id = mUser.getUid();


        mCodePinView = findViewById(R.id.pin_code_view);

        mJoinButton = findViewById(R.id.join_btn);
        mJoinButton.setOnClickListener(v -> joinCircle());

    }

    private void joinCircle() {
        Query query = UsersRef.orderByChild("circleCode").equalTo(mCodePinView.getValue());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    UserDataModel user;
                    for (DataSnapshot child : snapshot.getChildren()) {
                        user = child.getValue(UserDataModel.class);
                        join_user_id = user.getUserID();

                        CircleRef = FirebaseDatabase.getInstance().getReference().child(Common.USERS_INFORMATION)
                                .child(join_user_id).child("CircleMembers");

                        CircleJoin circleJoin = new CircleJoin(current_user_id);



                        CircleRef.child(mUser.getUid()).setValue(circleJoin)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(JoinCircle.this, "User Join Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                    }
                } else {
                    Toast.makeText(JoinCircle.this, "Circle Code is invalid", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}