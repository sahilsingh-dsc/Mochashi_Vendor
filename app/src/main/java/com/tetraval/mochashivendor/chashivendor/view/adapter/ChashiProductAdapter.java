package com.tetraval.mochashivendor.chashivendor.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tetraval.mochashivendor.R;
import com.tetraval.mochashivendor.chashivendor.model.ChashiProductModel;

import java.util.List;

public class ChashiProductAdapter extends RecyclerView.Adapter<ChashiProductAdapter.ChashiProductHolder> {

    Context context;
    List<ChashiProductModel> chashiProductModelList;
    String expand_state;


    public ChashiProductAdapter(Context context, List<ChashiProductModel> chashiProductModelList) {
        this.context = context;
        this.chashiProductModelList = chashiProductModelList;
    }

    @NonNull
    @Override
    public ChashiProductAdapter.ChashiProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chashi_product_list_item, parent, false);
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
