package com.tetraval.mochashivendor.chashivendor.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.tetraval.mochashivendor.R;
import com.tetraval.mochashivendor.chashivendor.model.ChashiOrdersModel;
import com.tetraval.mochashivendor.chashivendor.view.activity.CustomerMapsActivity;

import java.util.List;

public class ChashiOrderAdapter extends RecyclerView.Adapter<ChashiOrderAdapter.OrderViewHolder> {

    Context context;
    List<ChashiOrdersModel> chashiOrdersModelList;

    public ChashiOrderAdapter(Context context, List<ChashiOrdersModel> chashiOrdersModelList) {
        this.context = context;
        this.chashiOrdersModelList = chashiOrdersModelList;
    }

    @NonNull
    @Override
    public ChashiOrderAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_list_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override

    public void onBindViewHolder(@NonNull ChashiOrderAdapter.OrderViewHolder holder, int position) {
        ChashiOrdersModel chashiOrdersModel = chashiOrdersModelList.get(position);
        holder.imgUserDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater factory = LayoutInflater.from(context);
                final View deleteDialogView = factory.inflate(R.layout.show_customer_details_alert, null);
                TextView txtCustomerID, txtCustomerName;
                MaterialButton btnShowOnMap;
                txtCustomerID = deleteDialogView.findViewById(R.id.txtCustomerID);
                txtCustomerName = deleteDialogView.findViewById(R.id.txtCustomerName);
                btnShowOnMap = deleteDialogView.findViewById(R.id.btnShowOnMap);
                txtCustomerID.setText(chashiOrdersModel.getO_customer_uid());
                txtCustomerName.setText(chashiOrdersModel.getO_customer_name());
                btnShowOnMap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putString("c_name", chashiOrdersModel.getO_customer_name());
                        bundle.putString("c_address", chashiOrdersModel.getO_customer_address());
                        bundle.putString("c_lat", chashiOrdersModel.getO_lat());
                        bundle.putString("c_long", chashiOrdersModel.geto_long());
                        Intent intent = new Intent(context, CustomerMapsActivity.class);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                });
                final AlertDialog deleteDialog = new AlertDialog.Builder(context).create();
                deleteDialog.setView(deleteDialogView);
                deleteDialog.setTitle("Customer Details");
                deleteDialog.setMessage("Customer Address :\n\n"+chashiOrdersModel.getO_customer_address());
                deleteDialog.show();

            }
        });
        holder.txtOrderId.setText(chashiOrdersModel.getO_uid());
        holder.txtOrderDate.setText(chashiOrdersModel.getO_timestamp());
        holder.txtOrderCategory.setText(chashiOrdersModel.getO_p_category());
        holder.txtOrderAmount.setText("₹"+chashiOrdersModel.getO_total());
        holder.txtOrderQuantity.setText(chashiOrdersModel.getO_quantity() +" "+chashiOrdersModel.getO_unit());
        if (chashiOrdersModel.getO_homedelivery().equals("No")){
            if (chashiOrdersModel.getO_pickup().equals("Yes")){
                holder.txtPickupBy.setText("Customer");
                holder.txtPickupBy.setText("Customer");
            } else if (chashiOrdersModel.getO_pickup().equals("No")){
                holder.txtPickupBy.setText("Delivery Man");
            }
        } else {
            holder.txtPickupBy.setText("Chashi");
        }
    }

    @Override
    public int getItemCount() {
        return chashiOrdersModelList.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {

        ImageView imgUserDetails;
        TextView txtOrderCategory, txtOrderQuantity, txtOrderAmount, txtPickupBy, txtOrderId, txtOrderDate;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            imgUserDetails = itemView.findViewById(R.id.imgUserDetails);
            txtOrderQuantity = itemView.findViewById(R.id.txtOrderQuantity);
            txtOrderCategory = itemView.findViewById(R.id.txtOrderCategory);
            txtOrderAmount = itemView.findViewById(R.id.txtOrderAmount);
            txtPickupBy = itemView.findViewById(R.id.txtPickupBy);
            txtOrderId = itemView.findViewById(R.id.txtOrderId);
            txtOrderDate = itemView.findViewById(R.id.txtOrderDate);

        }
    }
}
