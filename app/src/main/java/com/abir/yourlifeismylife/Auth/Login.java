package com.abir.yourlifeismylife.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.abir.yourlifeismylife.DataModels.UserDataModel;
import com.abir.yourlifeismylife.R;
import com.abir.yourlifeismylife.User.Permissions;
import com.abir.yourlifeismylife.Utils.Common;
import com.abir.yourlifeismylife.Utils.CustomProgress;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class Login extends AppCompatActivity {

    Button mLogin;
    EditText mEmail, mPassword;
    TextView mCreateAnAccount;
    FirebaseAuth mAuth;
    DatabaseReference UsersRef;
    CustomProgress mCustomProgress = CustomProgress.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();

    }

    private void initViews() {

        Paper.init(this);

        mEmail = findViewById(R.id.login_email_et);
        mPassword = findViewById(R.id.login_password_et);
        mLogin = findViewById(R.id.login_login_btn);
        mLogin.setOnClickListener(v -> loginToTheAccount());
        mCreateAnAccount = findViewById(R.id.login_create_an_acc);
        mCreateAnAccount.setOnClickListener(v -> createAnAccount());
        mAuth = FirebaseAuth.getInstance();
        UsersRef = FirebaseDatabase.getInstance().getReference(Common.USERS_INFORMATION);



        mPassword.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            loginToTheAccount();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

    }

    private void createAnAccount() {
        startActivity(new Intent(Login.this, Signup.class));
        finish();
    }


    private void loginToTheAccount() {
        if (validate()) {
            String email = mEmail.getText().toString();
            String password = mPassword.getText().toString();

            mCustomProgress.showProgress(this, "Logging in...!", false);


            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        VerifyEmailAddress();
                    } else {
                        String messsage = task.getException().getMessage();
                        Toast.makeText(Login.this, "Error Occurred: " + messsage, Toast.LENGTH_LONG).show();
                        mCustomProgress.hideProgress();
                    }
                }
            });
        }
    }

    private void VerifyEmailAddress() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        firebaseUser.reload();

        UsersRef.orderByKey()
                .equalTo(firebaseUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Common.loggedUser = snapshot.child(firebaseUser.getUid()).getValue(UserDataModel.class);
                            Common.trackingUser = snapshot.child(firebaseUser.getUid()).getValue(UserDataModel.class);
                            Paper.book().write(Common.USER_UID_SAVED_KEY, Common.loggedUser.getUserID());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        mCustomProgress.hideProgress();
        Toast.makeText(Login.this, "Welcome to " + getResources().getString(R.string.app_name), Toast.LENGTH_LONG).show();
        startActivity(new Intent(Login.this, Permissions.class));
        finish();


    }

    private boolean validate() {
        if (mEmail.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter your email", Toast.LENGTH_LONG).show();
            return false;
        } else if (mPassword.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter your password", Toast.LENGTH_LONG).show();
            return false;
        } else if (mPassword.getText().toString().length() < 8) {
            mPassword.setError("Password < 8");
            Toast.makeText(this, "Password cannot be less than 8 characters", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


}