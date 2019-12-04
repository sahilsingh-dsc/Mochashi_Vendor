package com.tetraval.mochashivendor.chashivendor.view.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
import com.tetraval.mochashivendor.chashivendor.view.activity.ChashiDashboardActivity;
import com.tetraval.mochashivendor.chashivendor.view.adapter.ChashiOrderAdapter;

public class HomeFragment extends Fragment {

    double per_value = 0, total_value = 0;
    FirebaseFirestore db;
    TextView txtTotalValue, txtTotalSale;
    double o_per_value = 0, o_total_value = 0;

    public HomeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        db = FirebaseFirestore.getInstance();

        txtTotalValue = view.findViewById(R.id.txtTotalValue);
        txtTotalSale = view.findViewById(R.id.txtTotalSale);

        getProductList();
        fetchOrders();

        return view;
    }


    private void getProductList(){
        Query queryProducts = db.collection("chashi_products");
        queryProducts.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult() != null){
                        for (DocumentSnapshot document : task.getResult()){
                            double local_total_quantity = Double.parseDouble(document.getString("p_hquantity"));
                            double local_total_amount = Double.parseDouble(document.getString("p_rate"));
                            per_value = local_total_amount*local_total_quantity;
                            total_value = per_value+total_value;
                        }
                        txtTotalValue.setText("₹"+total_value);
                    }
                }else {
                    Toast.makeText(getContext(), "Database Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fetchOrders(){

        Query queryOrders = db.collection("chashi_orders");
        queryOrders.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult() != null){
                       for (DocumentSnapshot document : task.getResult()){
                           double local_total_quantity = Double.parseDouble(document.getString("o_quantity"));
                           double local_total_amount = Double.parseDouble(document.getString("o_rate"))+Double.parseDouble(document.getString("o_shipping"));
                           o_per_value = local_total_amount*local_total_quantity;
                           o_total_value = o_per_value+o_total_value;
                       }
                       txtTotalSale.setText("₹"+o_total_value);
                    }
                }else {
                    Toast.makeText(getContext(), "Database Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


}
