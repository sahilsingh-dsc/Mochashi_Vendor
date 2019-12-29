package com.tetraval.mochashivendor.chashivendor.view.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tetraval.mochashivendor.R;
import com.tetraval.mochashivendor.chashivendor.model.ChashiProductModel;
import com.tetraval.mochashivendor.chashivendor.view.activity.AddChashiProductActivity;
import com.tetraval.mochashivendor.chashivendor.view.adapter.ChashiProductAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProductFragment extends Fragment {

    private RecyclerView recyclerChashiProduct;
    private ProgressDialog progressDialog;
    private FirebaseFirestore db;
    private List<ChashiProductModel> chashiProductModelList;
    private ChashiProductAdapter chashiProductAdapter;
    private LinearLayoutManager linearLayoutManager;
    SharedPreferences profile;
    TextView txtNoProducts;
//    ImageView imgExpand;
//    CardView cardView;
//    String expand_state;
//    LinearLayout lvExpandBar;
//    TextView textView7;

    public ProductFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        profile = getContext().getSharedPreferences("USER_PROFILE", 0);

        recyclerChashiProduct = view.findViewById(R.id.recyclerChashiProduct);
        txtNoProducts = view.findViewById(R.id.txtNoProducts);
//        cardView = view.findViewById(R.id.cardView);
//        lvExpandBar = view.findViewById(R.id.lvExpandBar);
//        textView7 = view.findViewById(R.id.textView7);
//
//        expand_state = "unexpanded";
//        imgExpand = view.findViewById(R.id.imgExpand);
//        imgExpand.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (expand_state.equals("expanded")){
//                    lvExpandBar.setVisibility(View.VISIBLE);
//                    textView7.setVisibility(View.GONE);
//                    imgExpand.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_drop_down_black_24dp));
//                    cardView.setVisibility(View.GONE);
//                    expand_state = "unexpanded";
//                } else {
//                    lvExpandBar.setVisibility(View.GONE);
//                    textView7.setVisibility(View.VISIBLE);
//                    imgExpand.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_drop_up_black_24dp));
//                    cardView.setVisibility(View.VISIBLE);
//                    expand_state = "expanded";
//                }
//            }
//        });
        MaterialButton btnAddProduct = view.findViewById(R.id.btnAddProduct);
        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addIntent = new Intent(getContext(), AddChashiProductActivity.class);
                startActivity(addIntent);
            }
        });

        init();
        progressDialog.show();
        getProductList();
        return view;
    }

    private void init(){
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerChashiProduct.setLayoutManager(linearLayoutManager);
        chashiProductModelList = new ArrayList<>();
        chashiProductModelList.clear();
        db = FirebaseFirestore.getInstance();
    }

    private void getProductList(){
        Query queryProducts = db.collection("chashi_products");
        queryProducts.whereEqualTo("p_chashi_uid", profile.getString("p_uid", "")).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult() != null){
                        chashiProductModelList.clear();
                        for (DocumentSnapshot document : task.getResult()){
                            ChashiProductModel chashiProductModel = new ChashiProductModel(
                                    document.getDate("p_timestamp"),
                                    document.getString("p_uid"),
                                    document.getString("p_image1"),
                                    document.getString("p_image2"),
                                    document.getString("p_image3"),
                                    document.getString("p_image4"),
                                    document.getString("p_category"),
                                    document.getString("p_hquantity"),
                                    document.getString("p_bquantity"),
                                    document.getString("p_unit"),
                                    document.getString("p_rate"),
                                    document.getString("p_homedelivery"),
                                    document.getString("p_chashi_uid"),
                                    document.getString("p_chashi_name"),
                                    document.getString("p_chashi_photo"),
                                    document.getString("p_chashi_rating"),
                                    document.getString("p_chashi_address"),
                                    document.getString("p_delivery_status"),
                                    document.getString("p_lat"),
                                    document.getString("p_long"),
                                    document.getString("p_received_qty")
                            );
                            chashiProductModelList.add(chashiProductModel);
                        }
                        if (chashiProductModelList.isEmpty()){
                            txtNoProducts.setVisibility(View.VISIBLE);
                            recyclerChashiProduct.setVisibility(View.GONE);
                        }else {
                            txtNoProducts.setVisibility(View.GONE);
                            recyclerChashiProduct.setVisibility(View.VISIBLE);
                        }
                        chashiProductAdapter = new ChashiProductAdapter(getContext(), chashiProductModelList);
                        recyclerChashiProduct.setAdapter(chashiProductAdapter);
                        chashiProductAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }
                }else {
                    Toast.makeText(getContext(), "Database Error", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
    }



}
