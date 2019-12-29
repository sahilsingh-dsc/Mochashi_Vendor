package com.tetraval.mochashivendor.chashivendor.view.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tetraval.mochashivendor.R;
import com.tetraval.mochashivendor.chashivendor.model.ChashiProductModel;
import com.tetraval.mochashivendor.chashivendor.view.activity.ChashiDashboardActivity;
import com.tetraval.mochashivendor.chashivendor.view.activity.CustomerMapsActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChashiProductAdapter extends RecyclerView.Adapter<ChashiProductAdapter.ChashiProductHolder> {

    Context context;
    List<ChashiProductModel> chashiProductModelList;
    String expand_state;
    FirebaseFirestore db;
    ProgressDialog progressDialog;

    public ChashiProductAdapter(Context context, List<ChashiProductModel> chashiProductModelList) {
        this.context = context;
        this.chashiProductModelList = chashiProductModelList;
    }

    @NonNull
    @Override
    public ChashiProductAdapter.ChashiProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chashi_product_list_item, parent, false);
        db = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        return new ChashiProductHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChashiProductAdapter.ChashiProductHolder holder, int position) {
        ChashiProductModel chashiProductModel = chashiProductModelList.get(position);
        Glide.with(context).load(chashiProductModel.getP_image1()).into(holder.imgProductImage);
        holder.txtChashiCategoryName.setText(chashiProductModel.getP_category());
        holder.txtChashiProductRateTop.setText("₹"+chashiProductModel.getP_rate()+"/"+chashiProductModel.getP_unit());
        holder.txtChashiProductHostedQty.setText(chashiProductModel.getP_hquantity()+" "+chashiProductModel.getP_unit());
        holder.txtChashiProductBookedQty.setText(chashiProductModel.getP_bquantity()+" "+chashiProductModel.getP_unit());
        double avl_quantity = Double.parseDouble(chashiProductModel.getP_hquantity()) - Double.parseDouble(chashiProductModel.getP_bquantity());
        holder.txtChashiProductRemainQty.setText(avl_quantity+" "+chashiProductModel.getP_unit());
        holder.txtChashiHomeDelivery.setText(chashiProductModel.getP_homedelivery());
        holder.txtChashiProductRate.setText("₹"+chashiProductModel.getP_rate()+"/"+chashiProductModel.getP_unit());
        holder.txtChashiProductAvlQty.setText("Avaliable: "+avl_quantity+" "+chashiProductModel.getP_unit());
        expand_state = "unexpanded";
        holder.imgExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (expand_state.equals("expanded")){
                    holder.lvExpandBar.setVisibility(View.VISIBLE);
                    holder.txtEditProduct.setVisibility(View.GONE);
                    holder.imgExpand.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_arrow_drop_down_black_24dp));
                    holder.cardView.setVisibility(View.GONE);
                    expand_state = "unexpanded";
                } else if (expand_state.equals("unexpanded")){
                    holder.lvExpandBar.setVisibility(View.GONE);
                    holder.txtEditProduct.setVisibility(View.VISIBLE);
                    holder.imgExpand.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_arrow_drop_up_black_24dp));
                    holder.cardView.setVisibility(View.VISIBLE);
                    expand_state = "expanded";
                }
            }
        });

        holder.txtEditProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater factory = LayoutInflater.from(context);
                final View updateDialogView = factory.inflate(R.layout.edit_chashi_product_alert, null);
                TextInputLayout tlHostedQ;
                TextInputEditText tiHostedQ;
                MaterialButton btnUpdateProduct;
                tlHostedQ = updateDialogView.findViewById(R.id.tlHostedQ);
                tlHostedQ.setSuffixText(chashiProductModel.getP_unit());
                tiHostedQ = updateDialogView.findViewById(R.id.tiHostedQ);
                btnUpdateProduct = updateDialogView.findViewById(R.id.btnUpdateProduct);
                double b_qty = Double.parseDouble(chashiProductModel.getP_bquantity());
                double a_qty = Double.parseDouble(chashiProductModel.getP_hquantity()) - b_qty;
                tiHostedQ.setText(""+a_qty);

                final AlertDialog deleteDialog = new AlertDialog.Builder(context).create();
                btnUpdateProduct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        progressDialog.show();
                        String hq = tiHostedQ.getText().toString();
                        double b_qty = Double.parseDouble(chashiProductModel.getP_bquantity());
                        double a_qty = Double.parseDouble(chashiProductModel.getP_hquantity()) - b_qty;
                     //   tiHostedQ.setText(""+a_qty);
//                        Toast.makeText(context, "h: "+Double.parseDouble(chashiProductModel.getP_hquantity())+" b: "+b_qty, Toast.LENGTH_SHORT).show();
                   //     double total_h_qty = h_qty + Double.parseDouble(chashiProductModel.getP_hquantity());
                        if (TextUtils.isEmpty(hq)){
                            progressDialog.dismiss();
                            tiHostedQ.setError("Hosted quantity required");
                            return;
                        }
                        double h_qty = Double.parseDouble(tiHostedQ.getText().toString());
                        if (h_qty < b_qty || h_qty < 0){
                            progressDialog.dismiss();
                            tiHostedQ.setError("Hosted quantity cannot be less than booked quantity: "+chashiProductModel.getP_bquantity()+chashiProductModel.getP_unit());
                            return;
                        }

                        Map update = new HashMap();
                        update.put("p_hquantity", ""+h_qty);
                        CollectionReference collectionReference = db.collection("chashi_products");
                        DocumentReference documentReference = collectionReference.document(chashiProductModel.getP_uid());
                        documentReference.update(update)
                                .addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if (task.isSuccessful()){
                                            progressDialog.dismiss();
                                            Toast.makeText(context, "Product Updated!", Toast.LENGTH_SHORT).show();
                                            context.startActivity(new Intent(context, ChashiDashboardActivity.class));
                                            deleteDialog.dismiss();
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(context, "Something went wrong...", Toast.LENGTH_SHORT).show();
                                        deleteDialog.dismiss();
                                    }
                                });
                    }
                });
                deleteDialog.setView(updateDialogView);
                deleteDialog.setTitle("Update "+chashiProductModel.getP_category());
                deleteDialog.setMessage("You can update hosted quantity here, but not less than already booked quantity. \n\nCurrently booked: "+chashiProductModel.getP_bquantity()+chashiProductModel.getP_unit());
                deleteDialog.show();

            }
        });


    }

    @Override
    public int getItemCount() {
        return chashiProductModelList.size();
    }

    public class ChashiProductHolder extends RecyclerView.ViewHolder {

        ImageView imgProductImage, imgExpand;
        TextView txtChashiCategoryName, txtChashiProductRateTop, txtChashiProductHostedQty, txtChashiProductBookedQty, txtChashiProductRemainQty;
        TextView txtChashiHomeDelivery, txtChashiProductRate, txtChashiProductAvlQty, txtEditProduct;
        LinearLayout lvExpandBar;
        CardView cardView;

        public ChashiProductHolder(@NonNull View itemView) {
            super(itemView);

            imgProductImage = itemView.findViewById(R.id.imgProductImage);
            imgExpand = itemView.findViewById(R.id.imgExpand);
            txtChashiCategoryName = itemView.findViewById(R.id.txtChashiCategoryName);
            txtChashiProductRateTop = itemView.findViewById(R.id.txtChashiProductRateTop);
            txtChashiProductHostedQty = itemView.findViewById(R.id.txtChashiProductHostedQty);
            txtChashiProductBookedQty = itemView.findViewById(R.id.txtChashiProductBookedQty);
            txtChashiProductRemainQty = itemView.findViewById(R.id.txtChashiProductRemainQty);
            txtChashiHomeDelivery = itemView.findViewById(R.id.txtChashiHomeDelivery);
            txtChashiProductRate = itemView.findViewById(R.id.txtChashiProductRate);
            txtChashiProductAvlQty = itemView.findViewById(R.id.txtChashiProductAvlQty);
            txtEditProduct = itemView.findViewById(R.id.txtEditProduct);
            lvExpandBar = itemView.findViewById(R.id.lvExpandBar);
            cardView = itemView.findViewById(R.id.cardView);

        }
    }


}
