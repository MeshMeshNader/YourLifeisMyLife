package com.abir.yourlifeismylife.User;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.abir.yourlifeismylife.R;
import com.abir.yourlifeismylife.User.Knowledge.Knowledge;

import de.timonknispel.ktloadingbutton.KTLoadingButton;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class Measurement extends AppCompatActivity {

    KTLoadingButton serotoninBtn, dopamineBtn , mConnect;
    TextView serotoninText, dopamineText;
    Button mContinue;
    int i = 0 ;
    int finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement);
        finish = getIntent().getIntExtra("finish", 0);
        initViews();


    }

    private void initViews() {
        serotoninBtn = findViewById(R.id.serotonin_btn);
        dopamineBtn = findViewById(R.id.dopamine_btn);
        serotoninText = findViewById(R.id.serotonin_level_text);
        dopamineText = findViewById(R.id.dopamine_level_text);
        mContinue = findViewById(R.id.measurements_continue_btn);
        mConnect = findViewById(R.id.measurements_connect_btn);
        
        mConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConnect.startLoading();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mConnect.doResult(true, new Function1<KTLoadingButton, Unit>() {
                            @Override
                            public Unit invoke(KTLoadingButton ktLoadingButton) {
                                mConnect.setClickable(false);
                                Toast.makeText(Measurement.this, "Connected Successfully", Toast.LENGTH_SHORT).show();
                                return null;
                            }
                        });
                    }
                }, 1000);
            }
        });


        serotoninBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serotoninBtn.startLoading();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        serotoninBtn.doResult(true, new Function1<KTLoadingButton, Unit>() {
                            @Override
                            public Unit invoke(KTLoadingButton ktLoadingButton) {
                                serotoninBtn.setClickable(false);
                                serotoninText.setVisibility(View.VISIBLE);
                                i++;
                                return null;
                            }
                        });
                    }
                }, 1000);
            }
        });

        dopamineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dopamineBtn.startLoading();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dopamineBtn.doResult(true, new Function1<KTLoadingButton, Unit>() {
                            @Override
                            public Unit invoke(KTLoadingButton ktLoadingButton) {
                                dopamineBtn.setClickable(false);
                                dopamineText.setVisibility(View.VISIBLE);
                                i++;
                                return null;
                            }
                        });
                    }
                }, 1000);
            }
        });


        if (finish == 0)
            mContinue.setOnClickListener(v -> goToKnowledge());
        else
            mContinue.setOnClickListener(v -> finish());

    }

    private void goToKnowledge() {

        if (i >= 2) {
            startActivity(new Intent(Measurement.this, Knowledge.class));
            finish();
        } else {
            Toast.makeText(this, "Please Enable All Measurements", Toast.LENGTH_SHORT).show();
        }


    }


}