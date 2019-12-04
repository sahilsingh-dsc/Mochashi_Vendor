package com.tetraval.mochashivendor.authmodule;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tetraval.mochashivendor.R;
import com.tetraval.mochashivendor.chashivendor.view.activity.ChashiDashboardActivity;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class OTPActivity extends AppCompatActivity {

    TextView txtMobileNumber;
    MaterialButton btnVerifyAndLogin, btnReSendOTP;
    TextInputEditText tiOTP;
    String mobile_number;
    FirebaseAuth firebaseAuth;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    ProgressDialog progressDialog;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        Toolbar toolbarOTP = findViewById(R.id.toolbarOTP);
        setSupportActionBar(toolbarOTP);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Verify OTP");
        toolbarOTP.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        toolbarOTP.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        toolbarOTP.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        Objects.requireNonNull(toolbarOTP.getOverflowIcon()).setColorFilter(Color.WHITE , PorterDuff.Mode.SRC_ATOP);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        db = FirebaseFirestore.getInstance();

        txtMobileNumber = findViewById(R.id.txtMobileNumber);
        btnVerifyAndLogin = findViewById(R.id.btnVerifyAndLogin);
        btnReSendOTP = findViewById(R.id.btnReSendOTP);
        tiOTP = findViewById(R.id.tiOTP);

        Bundle otpBundle = getIntent().getExtras();
        mobile_number = otpBundle.getString("mobile_number");
        txtMobileNumber.setText("+91"+mobile_number);

        btnVerifyAndLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otpcode = tiOTP.getText().toString();
                if (TextUtils.isEmpty(otpcode) || !TextUtils.isDigitsOnly(otpcode) || otpcode.length() != 6){
                    Toast.makeText(OTPActivity.this, "Please enter valid OTP.", Toast.LENGTH_SHORT).show();
                }
                progressDialog.show();
                verifyPhoneNumberWithCode(mVerificationId, otpcode);
            }
        });

        btnReSendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendVerificationCode(mobile_number, mResendToken);
                Toast.makeText(OTPActivity.this, "OTP re-sent", Toast.LENGTH_SHORT).show();
            }
        });
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                progressDialog.show();
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(OTPActivity.this, "" + e, Toast.LENGTH_SHORT).show();
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(OTPActivity.this, "Enter valid mobile number", Toast.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Toast.makeText(OTPActivity.this, "Quota Exceeded", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };
        startPhoneNumberVerification(mobile_number);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(OTPActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            String uid = firebaseAuth.getCurrentUser().getUid();
                            DocumentReference profileRef = db.collection("vendor_profiles").document(uid);
                            profileRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()){
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()){
                                            progressDialog.dismiss();
                                            startActivity(new Intent(getApplicationContext(), ChashiDashboardActivity.class));
                                            finish();
                                        }else {
                                            startActivity(new Intent(getApplicationContext(), UserActivity.class));
                                            finish();
                                        }
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(OTPActivity.this, "database error (otp->profile)", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(OTPActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(OTPActivity.this, "Invalid code", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
        Toast.makeText(this, "Code Resent", Toast.LENGTH_SHORT).show();
    }


}
