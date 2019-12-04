package com.tetraval.mochashivendor.genericmodule;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tetraval.mochashivendor.R;
import com.tetraval.mochashivendor.authmodule.MobileActivity;
import com.tetraval.mochashivendor.authmodule.UserActivity;
import com.tetraval.mochashivendor.chashivendor.view.activity.ChashiDashboardActivity;

import static com.tetraval.mochashivendor.chashivendor.utils.Constants.SPLASH_DEALY;

public class SplashActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;
    String p_uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (firebaseAuth.getCurrentUser() != null){
                    String uid = firebaseAuth.getCurrentUser().getUid();
                    DocumentReference profileRef = db.collection("vendor_profiles").document(uid);
                    profileRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()){
                                    startActivity(new Intent(getApplicationContext(), ChashiDashboardActivity.class));
                                    finish();
                                }else {
                                    startActivity(new Intent(getApplicationContext(), UserActivity.class));
                                    finish();
                                }
                            } else {
                                Toast.makeText(SplashActivity.this, "database error (otp->profile)", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
//                    startActivity(new Intent(getApplicationContext(), UserActivity.class));
//                    finish();
                }else {
                    startActivity(new Intent(getApplicationContext(), MobileActivity.class));
                    finish();
                }

            }
        }, SPLASH_DEALY);



    }
}
