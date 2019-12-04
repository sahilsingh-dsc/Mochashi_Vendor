package com.tetraval.mochashivendor.authmodule;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rtchagas.pingplacepicker.PingPlacePicker;
import com.tetraval.mochashivendor.R;
import com.tetraval.mochashivendor.authmodule.model.ProfileModel;
import com.tetraval.mochashivendor.chashivendor.view.activity.ChashiDashboardActivity;

import java.io.ByteArrayOutputStream;
import java.util.Objects;
import java.util.UUID;

public class UserActivity extends AppCompatActivity {

    ImageView imgProfileImage;
    TextInputEditText tiFirstName, tiLastName, tiEmail, tiFetchLocation;
    MaterialButton btnUpdateProfile;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    String imgurl;
    Boolean i;
    FirebaseStorage storage;
    StorageReference storageReference;
    ProgressDialog progressDialog;
    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;

    String l_lat, l_long, l_address;

    private static final int PLACE_PICKER_REQ_CODE = 1;

    private static final String PROFILE_KEY = "vendor_profiles";
    private static final String PROFILE_IMAGE_URL_KEY = "p_image";
    private static final String PROFILE_FIRST_NAME = "p_fname";
    private static final String PROFILE_LAST_NAME = "p_lname";
    private static final String PROFILE_EMAIL = "p_email";
    private static final String PROFILE_ADDRESS = "p_address";
    private static final String PROFILE_LAT = "p_lat";
    private static final String PROFILE_LONG = "p_long";

    private final String[] permissions = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbarRegister = findViewById(R.id.toolbarRegister);
        setSupportActionBar(toolbarRegister);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Update Profile");
        toolbarRegister.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        Objects.requireNonNull(toolbarRegister.getOverflowIcon()).setColorFilter(Color.WHITE , PorterDuff.Mode.SRC_ATOP);

        imgProfileImage = findViewById(R.id.imgProfileImage);
        tiFirstName = findViewById(R.id.tiFirstName);
        tiLastName = findViewById(R.id.tiLastName);
        tiEmail = findViewById(R.id.tiEmail);
        tiFetchLocation = findViewById(R.id.tiFetchLocation);
        tiFetchLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPlacePicker();
            }
        });
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        final String p_uid = firebaseAuth.getCurrentUser().getUid();

        if (ActivityCompat.checkSelfPermission(UserActivity.this, permissions[0]) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(UserActivity.this, permissions[1]) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(UserActivity.this, permissions[2]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(UserActivity.this, permissions, MY_CAMERA_PERMISSION_CODE);
        }


        imgProfileImage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                i = false;
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String p_image = imgurl;
                String p_fname = tiFirstName.getText().toString();
                String p_lname = tiLastName.getText().toString();
                String p_email = tiEmail.getText().toString();
                String p_address = tiFetchLocation.getText().toString();
                if (TextUtils.isEmpty(p_image)){
                    Toast.makeText(UserActivity.this, "Please click you profile image.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(p_fname)){
                    tiFirstName.setError("First Name must not be empty.");
                    return;
                }
                if (TextUtils.isEmpty(p_lname)){
                    tiLastName.setError("Last Name must not be empty.");
                    return;
                }
                if (TextUtils.isEmpty(p_email)){
                    tiEmail.setError("Email must not be empty.");
                    return;
                }
                if (TextUtils.isEmpty(p_address)){
                    tiFetchLocation.setError("Address must not be empty.");
                    return;
                }
//                if (TextUtils.isEmpty(p_lat)){
//                    Toast.makeText(UserActivity.this, "Something went wrong (profile->lat)", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if (TextUtils.isEmpty(p_long)){
//                    Toast.makeText(UserActivity.this, "Something went wrong (profile->long)", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                setProfile(p_uid, p_image, p_fname, p_lname, p_email, p_address);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {
            Bitmap bitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            assert bitmap != null;
            Uri filePath = getImageUri(getApplicationContext(), bitmap);
            imgProfileImage.setImageBitmap(bitmap);
            i = true;
            if (filePath != null){
                uploadImage(filePath);
            }
        }


        if ((requestCode == PLACE_PICKER_REQ_CODE) && (resultCode == RESULT_OK)) {
            Place place = PingPlacePicker.getPlace(data);
            if (place != null) {
                Toast.makeText(this, ""+place.getAddress(), Toast.LENGTH_SHORT).show();
                tiFetchLocation.setText(place.getAddress());
                l_lat = String.valueOf(place.getLatLng().latitude);
                l_long = String.valueOf(place.getLatLng().longitude);
                l_address = place.getAddress();
            }
        }

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Profile", null);
        return Uri.parse(path);
    }

    private void uploadImage(Uri filePath) {

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        if (filePath != null) {
            progressDialog.setMessage("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("profiles/" + UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(UserActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    imgurl = uri.toString();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(UserActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }

    private void setProfile(String p_uid, String p_image, String p_fname, String p_lname, String p_email, String p_address){

        ProfileModel profileModel = new ProfileModel();
        profileModel.setP_uid(p_uid);
        profileModel.setP_image(p_image);
        profileModel.setP_fname(p_fname);
        profileModel.setP_lname(p_lname);
        profileModel.setP_email(p_email);
        profileModel.setP_address(p_address);
        profileModel.setP_lat(Double.parseDouble(l_lat));
        profileModel.setP_long(Double.parseDouble(l_long));
        profileModel.setP_active("inactive");

        db.collection(PROFILE_KEY).document(p_uid)
                .set(profileModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(UserActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), ChashiDashboardActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserActivity.this, "Database Error (profile->db): "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void showPlacePicker() {
        PingPlacePicker.IntentBuilder builder = new PingPlacePicker.IntentBuilder();
        builder.setAndroidApiKey("AIzaSyAsSY0Kk-PHj8BYktZY74V2MlyEyTVmc_Y")
                .setMapsApiKey("AIzaSyAsSY0Kk-PHj8BYktZY74V2MlyEyTVmc_Y");

        try {
            Intent placeIntent = builder.build(UserActivity.this);
            startActivityForResult(placeIntent, PLACE_PICKER_REQ_CODE);
        }
        catch (Exception ex) {
        }
    }

}
