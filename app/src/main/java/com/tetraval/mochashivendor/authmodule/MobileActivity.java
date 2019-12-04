package com.tetraval.mochashivendor.authmodule;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.tetraval.mochashivendor.R;

import java.util.Objects;

public class MobileActivity extends AppCompatActivity {

    TextInputEditText tiMobileNumber;
    MaterialButton btnSendOTP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile);

        Toolbar toolbarMobile = findViewById(R.id.toolbarMobile);
        setSupportActionBar(toolbarMobile);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.login));
        toolbarMobile.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        Objects.requireNonNull(toolbarMobile.getOverflowIcon()).setColorFilter(Color.WHITE , PorterDuff.Mode.SRC_ATOP);

        tiMobileNumber = findViewById(R.id.tiMobileNumber);
        btnSendOTP = findViewById(R.id.btnSendOTP);

        btnSendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mobile_number = Objects.requireNonNull(tiMobileNumber.getText()).toString();
                if (TextUtils.isEmpty(mobile_number)){
                    tiMobileNumber.setError(getString(R.string.mob_empty));
                    return;
                }
                if (!TextUtils.isDigitsOnly(mobile_number)){
                    tiMobileNumber.setError(getString(R.string.mob_digit));
                    return;
                }
                if (mobile_number.length() < 10){
                    tiMobileNumber.setError(getString(R.string.mob_10_digit));
                    return;
                }
                sendMobileNumber(mobile_number);
            }
        });
    }

    private void sendMobileNumber(String mobile_number){
        Intent otpIntent = new Intent(getApplicationContext(), OTPActivity.class);
        Bundle otpBundle = new Bundle();
        otpBundle.putString(getString(R.string.mob_num), mobile_number);
        otpIntent.putExtras(otpBundle);
        startActivity(otpIntent);
        finish();
    }
}
