package com.tetraval.mochashivendor.chashivendor.view.fragment;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.tetraval.mochashivendor.R;
import com.tetraval.mochashivendor.chashivendor.model.ChashiOrdersModel;
import com.tetraval.mochashivendor.chashivendor.view.adapter.ChashiOrderAdapter;

import java.util.ArrayList;
import java.util.List;

public class OrdersFragment extends Fragment {

    RecyclerView recyclerOrders;
    List<ChashiOrdersModel> chashiOrdersModelList;
    ChashiOrderAdapter chashiOrderAdapter;
    FirebaseFirestore db;
    ProgressDialog progressDialog;
    SharedPreferences profile;
    TextView txtNoOrder;

    public OrdersFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        db = FirebaseFirestore.getInstance();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        profile = getContext().getSharedPreferences("USER_PROFILE", 0);

        txtNoOrder = view.findViewById(R.id.txtNoOrder);
        recyclerOrders = view.findViewById(R.id.recyclerOrders);
        recyclerOrders.setLayoutManager(new LinearLayoutManager(getContext()));
        chashiOrdersModelList = new ArrayList<>();
        chashiOrdersModelList.clear();

        progressDialog.show();
        fetchOrders();

        return view;
    }


    private void fetchOrders(){

        Query queryOrders = db.collection("chashi_orders");
        queryOrders.orderBy("o_date", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult() != null){
                        chashiOrdersModelList.clear();
                        for (DocumentSnapshot document : task.getResult()){
                            if (document.getString("o_chashi_uid").equals(profile.getString("p_uid", ""))){

                                ChashiOrdersModel chashiOrdersModel = new ChashiOrdersModel();
                                chashiOrdersModel.setO_timestamp(document.getString("o_timestamp"));
                                chashiOrdersModel.setO_uid(document.getString("o_uid"));
                                chashiOrdersModel.setO_p_uid(document.getString("o_p_uid"));
                                chashiOrdersModel.setO_p_category(document.getString("o_p_category"));
                                chashiOrdersModel.setO_customer_uid(document.getString("o_customer_uid"));
                                chashiOrdersModel.setO_customer_name(document.getString("o_customer_name"));
                                chashiOrdersModel.setO_customer_address(document.getString("o_customer_address"));
                                chashiOrdersModel.setO_chashi_uid(document.getString("o_chashi_uid"));
                                chashiOrdersModel.setO_chashi_name(document.getString("o_chashi_name"));
                                chashiOrdersModel.setO_chashi_photo(document.getString("o_chashi_photo"));
                                chashiOrdersModel.setO_chashi_address(document.getString("o_chashi_address"));
                                chashiOrdersModel.setO_chashi_rating(document.getString("o_chashi_rating"));
                                chashiOrdersModel.setO_rate(document.getString("o_rate"));
                                chashiOrdersModel.setO_quantity(document.getString("o_quantity"));
                                chashiOrdersModel.setO_shipping(document.getString("o_shipping"));
                                chashiOrdersModel.setO_total(document.getString("o_total"));
                                chashiOrdersModel.setO_homedelivery(document.getString("o_homedelivery"));
                                chashiOrdersModel.setO_pickup(document.getString("o_pickup"));
                                chashiOrdersModel.setO_status(document.getString("o_status"));
                                chashiOrdersModel.setO_unit(document.getString("o_unit"));
                                chashiOrdersModel.setO_lat(document.getString("o_lat"));
                                chashiOrdersModel.seto_long(document.getString("o_long"));
                                chashiOrdersModelList.add(chashiOrdersModel);

                            }

                        }

                        if (chashiOrdersModelList.isEmpty()){
                            txtNoOrder.setVisibility(View.VISIBLE);
                            recyclerOrders.setVisibility(View.GONE);
                        }else {
                            txtNoOrder.setVisibility(View.GONE);
                            recyclerOrders.setVisibility(View.VISIBLE);
                        }

                        chashiOrderAdapter = new ChashiOrderAdapter(getContext(), chashiOrdersModelList);
                        recyclerOrders.setAdapter(chashiOrderAdapter);
                        chashiOrderAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }
                }else {
                    Toast.makeText(getContext(), "Database Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


}
