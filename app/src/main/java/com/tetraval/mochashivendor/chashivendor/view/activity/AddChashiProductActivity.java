package com.tetraval.mochashivendor.chashivendor.view.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tetraval.mochashivendor.BuildConfig;
import com.tetraval.mochashivendor.R;
import com.tetraval.mochashivendor.chashivendor.model.ChashiCategoryModel;
import com.tetraval.mochashivendor.chashivendor.model.ChashiProductModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static java.security.AccessController.getContext;

public class AddChashiProductActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private final String[] permissions = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

    ImageView img1, img2, img3, img4;
    AutoCompleteTextView tiACChashiCategory, tiACChashiUnit;
    TextInputEditText tiChashiQuantity, tiChashiRate;
    Switch switchHomeDelivery;
    MaterialButton btnAddProduct;
    Boolean i1 = false, i2 = false, i3 = false, i4 = false;
    String imgurl1, imgurl2, imgurl3, imgurl4;
    String imgstate = "null";
    String p_homedelivery = "No";
    ProgressDialog progressDialog;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseFirestore db;
    SharedPreferences master;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_chashi_product);

        Toolbar toolbarAddProduct = findViewById(R.id.toolbarAddProduct);
        setSupportActionBar(toolbarAddProduct);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Add Product");
        toolbarAddProduct.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        toolbarAddProduct.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbarAddProduct.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        master = getSharedPreferences("USER_PROFILE", 0);

        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        img4 = findViewById(R.id.img4);
        tiACChashiCategory = findViewById(R.id.tiACChashiCategory);
        tiACChashiUnit = findViewById(R.id.tiACChashiUnit);
        tiChashiQuantity = findViewById(R.id.tiChashiQuantity);
        tiChashiRate = findViewById(R.id.tiChashiRate);
        switchHomeDelivery = findViewById(R.id.switchHomeDelivery);
        btnAddProduct = findViewById(R.id.btnAddProduct);

        if (ActivityCompat.checkSelfPermission(AddChashiProductActivity.this, permissions[0]) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(AddChashiProductActivity.this, permissions[1]) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(AddChashiProductActivity.this, permissions[2]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddChashiProductActivity.this, permissions, MY_CAMERA_PERMISSION_CODE);
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        db = FirebaseFirestore.getInstance();

        String[] UNITS = new String[] {"Kg", "M", "L"};
        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(AddChashiProductActivity.this, R.layout.support_simple_spinner_dropdown_item, UNITS);
        tiACChashiUnit.setAdapter(unitAdapter);

        fetchChashiCategory();

        img1.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                i1 = false;
                imgstate = "img1";
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                i2 = false;
                imgstate = "img2";
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });

        img3.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                i3 = false;
                imgstate = "img3";
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }

        });

        img4.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                i4 = false;
                imgstate = "img4";
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });

        Toast.makeText(AddChashiProductActivity.this, ""+p_homedelivery, Toast.LENGTH_SHORT).show();
        switchHomeDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (p_homedelivery.equals("Yes")){
                    p_homedelivery = "No";
                    Toast.makeText(AddChashiProductActivity.this, ""+p_homedelivery, Toast.LENGTH_SHORT).show();
                }else if (p_homedelivery.equals("No")){
                    p_homedelivery = "Yes";
                    Toast.makeText(AddChashiProductActivity.this, ""+p_homedelivery, Toast.LENGTH_SHORT).show();
                }
            }
        });


        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!i1) {
                    Toast.makeText(AddChashiProductActivity.this, "Please upload 1st image.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!i2) {
                    Toast.makeText(AddChashiProductActivity.this, "Please upload 2nd image.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!i3) {
                    Toast.makeText(AddChashiProductActivity.this, "Please upload 3rd image.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!i4) {
                    Toast.makeText(AddChashiProductActivity.this, "Please upload 4th image.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String p_category = tiACChashiCategory.getText().toString();
                String p_quantity = tiChashiQuantity.getText().toString();
                String p_unit = tiACChashiUnit.getText().toString();
                String p_rate = tiChashiRate.getText().toString();

                if (TextUtils.isEmpty(p_category) || p_category.equals("Select Category")) {
                    Toast.makeText(AddChashiProductActivity.this, "Please select product category.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!TextUtils.isDigitsOnly(p_quantity) || TextUtils.isEmpty(p_quantity)) {
                    Toast.makeText(AddChashiProductActivity.this, "Please enter valid quantity.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(p_unit) || p_unit.equals("Unit")) {
                    Toast.makeText(AddChashiProductActivity.this, "Please select product unit.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!TextUtils.isDigitsOnly(p_rate) || TextUtils.isEmpty(p_rate)) {
                    Toast.makeText(AddChashiProductActivity.this, "Please enter valid rate.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String p_image1 = imgurl1;
                String p_image2 = imgurl2;
                String p_image3 = imgurl3;
                String p_image4 = imgurl4;

                addProductToFireStore(
                        p_image1,
                        p_image2,
                        p_image3,
                        p_image4,
                        p_category,
                        p_quantity,
                        p_unit,
                        p_rate,
                        p_homedelivery
                );

            }
        });


    }

    private void fetchChashiCategory(){
        CollectionReference categoryRef = db.collection("chashi_categories");
        final List<String> CATEGORIES = new ArrayList<>();
        final ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, CATEGORIES);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tiACChashiCategory.setAdapter(categoryAdapter);
        categoryRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String subject = document.getString("c_name");
                        CATEGORIES.add(subject);
                    }
                    categoryAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            Uri filePath = getImageUri(getApplicationContext(), bitmap);
            Log.e("vendor", "filepath===" + filePath);

            if (imgstate.equals("img1")) {
                img1.setImageBitmap(bitmap);
                i1 = true;
                if (filePath != null) {
                    uploadImage1(filePath);
                }
            }
            if (imgstate.equals("img2")) {
                img2.setImageBitmap(bitmap);
                i2 = true;
                uploadImage2(filePath);
            }
            if (imgstate.equals("img3")) {
                img3.setImageBitmap(bitmap);
                i3 = true;
                uploadImage3(filePath);
            }
            if (imgstate.equals("img4")) {
                img4.setImageBitmap(bitmap);
                i4 = true;
                uploadImage4(filePath);
            }
        }
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    private void uploadImage1(Uri filePath) {

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        if (filePath != null) {
            progressDialog.setMessage("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(AddChashiProductActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    imgurl1 = uri.toString();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddChashiProductActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }


    private void uploadImage2(Uri filePath) {

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        if (filePath != null) {
            progressDialog.setMessage("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(AddChashiProductActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    imgurl2 = uri.toString();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddChashiProductActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }

    private void uploadImage3(Uri filePath) {

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        if (filePath != null) {
            progressDialog.setMessage("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(AddChashiProductActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    imgurl3 = uri.toString();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddChashiProductActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }

    private void uploadImage4(Uri filePath) {

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        if (filePath != null) {
            progressDialog.setMessage("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(AddChashiProductActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    imgurl4 = uri.toString();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddChashiProductActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void addProductToFireStore(String p_image1, String p_image2, String p_image3, String p_image4, String p_category, String p_quantity, String p_unit, String p_rate, String p_homedelivery){
        CollectionReference productColRef = db.collection("chashi_products");
        DocumentReference productDocRef = productColRef.document();
        String p_uid = productDocRef.getId();
        ChashiProductModel chashiProductModel = new ChashiProductModel();
        chashiProductModel.setP_uid(p_uid);
        chashiProductModel.setP_image1(p_image1);
        chashiProductModel.setP_image2(p_image2);
        chashiProductModel.setP_image3(p_image3);
        chashiProductModel.setP_image4(p_image4);
        chashiProductModel.setP_category(p_category);
        chashiProductModel.setP_hquantity(p_quantity);
        chashiProductModel.setP_bquantity("0");
        chashiProductModel.setP_unit(p_unit);
        chashiProductModel.setP_rate(p_rate);
        chashiProductModel.setP_homedelivery(p_homedelivery);
        chashiProductModel.setP_chashi_uid(master.getString("p_uid", ""));
        chashiProductModel.setP_chashi_name(master.getString("p_fname", "")+" "+master.getString("p_lname", ""));
        chashiProductModel.setP_chashi_photo(master.getString("p_image", ""));
        chashiProductModel.setP_chashi_address(master.getString("p_address", ""));
        chashiProductModel.setP_chashi_rating("5");
        chashiProductModel.setP_delivery_status("0");
        chashiProductModel.setP_lat(master.getString("p_lat", ""));
        chashiProductModel.setP_long(master.getString("p_long", ""));
        chashiProductModel.setP_received_qty("0");
        productColRef.document(p_uid).set(chashiProductModel);
        Toast.makeText(this, "Product Added!", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

}
