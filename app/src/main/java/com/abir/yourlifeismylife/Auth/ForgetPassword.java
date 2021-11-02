package com.abir.yourlifeismylife.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.abir.yourlifeismylife.R;
import com.abir.yourlifeismylife.Utils.CustomProgress;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {

    //Views
    EditText mEmail;
    Button mReset;
    TextView mCreateAnAccount;

    //Fire base
    FirebaseAuth mAuth;
    private CustomProgress mCustomProgress = CustomProgress.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        initView();


    }

    private void initView() {
        mEmail = findViewById(R.id.reset_email_et);
        mReset = findViewById(R.id.rest_password_btn);
        mReset.setOnClickListener(v -> resetPassword());
        mCreateAnAccount = findViewById(R.id.reset_create_an_acc);
        mCreateAnAccount.setOnClickListener(v -> createAnAccount());

        mAuth = FirebaseAuth.getInstance();
    }


    private void createAnAccount() {
        startActivity(new Intent(ForgetPassword.this, Signup.class));
        finish();
    }

    private void resetPassword() {
        if (mEmail.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter your email address", Toast.LENGTH_LONG).show();
        } else {
            mCustomProgress.showProgress(this, "Please Wait!...", true);
            String userEmail = mEmail.getText().toString();

            mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        mCustomProgress.hideProgress();
                        Toast.makeText(ForgetPassword.this, "Please Check your Email to reset your password", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(ForgetPassword.this, Login.class));
                        finish();
                    } else {
                        String message = task.getException().toString();
                        Toast.makeText(ForgetPassword.this, "Error Occurrd : " + message, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }


}