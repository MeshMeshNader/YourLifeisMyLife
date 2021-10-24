package com.abir.yourlifeismylife.User;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.abir.yourlifeismylife.R;
import com.abir.yourlifeismylife.User.Knowledge.Knowledge;

import de.timonknispel.ktloadingbutton.KTLoadingButton;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class Measurement extends AppCompatActivity {

    KTLoadingButton serotoninBtn, dopamineBtn;
    TextView serotoninText, dopamineText;
    Button mContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement);

        initViews();

    }

    private void initViews() {
        serotoninBtn = findViewById(R.id.serotonin_btn);
        dopamineBtn = findViewById(R.id.dopamine_btn);
        serotoninText = findViewById(R.id.serotonin_level_text);
        dopamineText = findViewById(R.id.dopamine_level_text);
        mContinue = findViewById(R.id.measurements_continue_btn);


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
                                return null;
                            }
                        });
                    }
                },3000);
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
                                return null;
                            }
                        });
                    }
                },3000);
            }
        });


        mContinue.setOnClickListener(v -> goToKnowledge());

    }

    private void goToKnowledge() {
        startActivity(new Intent(Measurement.this, Knowledge.class));
        finish();
    }


}