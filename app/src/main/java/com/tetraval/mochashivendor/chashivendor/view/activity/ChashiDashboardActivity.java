package com.tetraval.mochashivendor.chashivendor.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.tetraval.mochashivendor.R;
import com.tetraval.mochashivendor.authmodule.MobileActivity;
import com.tetraval.mochashivendor.chashivendor.model.ChashiProductModel;
import com.tetraval.mochashivendor.chashivendor.view.adapter.ChashiProductAdapter;
import com.tetraval.mochashivendor.chashivendor.view.fragment.HomeFragment;
import com.tetraval.mochashivendor.chashivendor.view.fragment.OrdersFragment;
import com.tetraval.mochashivendor.chashivendor.view.fragment.ProductFragment;
import com.tetraval.mochashivendor.chashivendor.view.fragment.ProfileFragment;

import java.util.List;
import java.util.Objects;

public class ChashiDashboardActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    Toolbar toolbarVendor;
    FirebaseFirestore db;
    FirebaseAuth firebaseAuth, auth;
    String p_uid, uid;
    ProgressDialog progressDialog;
    double total_quantity = 0, per_value = 0, total_value = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chashi_dashboard);

        toolbarVendor = findViewById(R.id.toolbarVendor);
        setSupportActionBar(toolbarVendor);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Chashi Home");
        toolbarVendor.setTitleTextColor(getResources().getColor(R.color.colorWhite));

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(ChashiDashboardActivity.this);
        bottomNavigationView.setSelectedItemId(R.id.menu_home);

        db = FirebaseFirestore.getInstance();
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        p_uid = firebaseAuth.getCurrentUser().getUid();

        progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null){
            uid = auth.getCurrentUser().getUid();
            setProfileData();
        }




    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to Mochashi Vendor", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()){

            case R.id.menu_home :
                fragment = new HomeFragment();
                Objects.requireNonNull(getSupportActionBar()).setTitle("Chashi Home");
                break;

            case R.id.menu_orders :
                fragment = new OrdersFragment();
                Objects.requireNonNull(getSupportActionBar()).setTitle("Chashi Orders");
                break;

            case R.id.menu_products :
                fragment = new ProductFragment();
                Objects.requireNonNull(getSupportActionBar()).setTitle("Chashi Products");
                break;


            case R.id.menu_profile :
                fragment = new ProfileFragment();
                Objects.requireNonNull(getSupportActionBar()).setTitle("Chashi Profile");
                break;

        }

        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_main, fragment)
                    .commit();
            return true;
        }

        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null){
            uid = auth.getCurrentUser().getUid();
            setProfileData();
        }

    }

    private void setProfileData(){
        CollectionReference profileCol = db.collection("vendor_profiles");
        profileCol.document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult().exists()){
                        DocumentSnapshot document = task.getResult();
                        SharedPreferences profile = getSharedPreferences("USER_PROFILE", 0);
                        SharedPreferences.Editor editor = profile.edit();
                        editor.putString("p_active", document.getString("p_active"));
                        editor.putString("p_address", document.getString("p_address"));
                        editor.putString("p_email", document.getString("p_email"));
                        editor.putString("p_lat", String.valueOf(document.getDouble("p_lat")));
                        editor.putString("p_long", String.valueOf(document.getDouble("p_long")));
                        editor.putString("p_fname", document.getString("p_fname"));
                        editor.putString("p_lname", document.getString("p_lname"));
                        editor.putString("p_uid", document.getString("p_uid"));
                        editor.putString("p_image", document.getString("p_image"));
                        editor.apply();
                     //   Toast.makeText(ChashiDashboardActivity.this, ""+profile.getString("p_active",""), Toast.LENGTH_SHORT).show();
                        if (profile.getString("p_active", "inactive").equals("inactive")){
                            new AlertDialog.Builder(ChashiDashboardActivity.this)
                                    .setTitle("Approval Pending")
                                    .setMessage("Dear Chashi,\nYou account is still not approved by admin, please check back later.")
                                    .setCancelable(false)
                                    .show();
                            //FirebaseAuth.getInstance().signOut();
                        }
                    } else {
                        Toast.makeText(ChashiDashboardActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ChashiDashboardActivity.this, "Database Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void userLogout(){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        startActivity(new Intent(ChashiDashboardActivity.this, MobileActivity.class));
        finish();
    }


}
