package com.tetraval.mochashivendor.chashivendor.view.fragment;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tetraval.mochashivendor.R;
import com.tetraval.mochashivendor.chashivendor.view.activity.ChashiDashboardActivity;

public class ProfileFragment extends Fragment {

    ImageView imgProfileImage;
    TextInputEditText tiFirstName, tiLastName, tiEmail, tiMobileNumber, tiFetchLocation;
    private static final String PROFILE_KEY = "vendor_profiles";
    private static final String PROFILE_IMAGE_URL_KEY = "p_image";
    private static final String PROFILE_FIRST_NAME = "p_fname";
    private static final String PROFILE_LAST_NAME = "p_lname";
    private static final String PROFILE_EMAIL = "p_email";
    private static final String PROFILE_ADDRESS = "p_address";
    ProgressDialog progressDialog;
    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    String p_uid;

    public ProfileFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        imgProfileImage = view.findViewById(R.id.imgProfileImage);
        tiFirstName = view.findViewById(R.id.tiFirstName);
        tiLastName = view.findViewById(R.id.tiLastName);
        tiEmail = view.findViewById(R.id.tiEmail);
        tiMobileNumber = view.findViewById(R.id.tiMobileNumber);
        tiFetchLocation = view.findViewById(R.id.tiFetchLocation);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        p_uid = firebaseAuth.getCurrentUser().getUid();

        MaterialButton btnLogout = view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                ((ChashiDashboardActivity)getActivity()).userLogout();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Logout");
                builder.setMessage("Are you sure, want to Logout?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

        progressDialog.show();
        getProfile();

        return view;
    }

    private void getProfile(){
        DocumentReference vendorProfileRef = db.collection(PROFILE_KEY).document(p_uid);
        vendorProfileRef.get().addOnCompleteListener(new OnCompleteListener< DocumentSnapshot >() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    Glide.with(getContext()).load(doc.getString(PROFILE_IMAGE_URL_KEY)).into(imgProfileImage);
                    tiFirstName.setText(doc.getString(PROFILE_FIRST_NAME));
                    tiLastName.setText(doc.getString(PROFILE_LAST_NAME));
                    tiEmail.setText(doc.getString(PROFILE_EMAIL));
                    tiMobileNumber.setText(firebaseAuth.getCurrentUser().getPhoneNumber());
                    tiFetchLocation.setText(doc.getString(PROFILE_ADDRESS));
                    progressDialog.dismiss();
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Database Error (profile->db) : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
    }

}
