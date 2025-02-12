package com.vinsoftsolutions.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vinsoftsolutions.Models.PurchaseModel;
import com.vinsoftsolutions.R;

import java.util.List;

public class PurchaseAdapter extends RecyclerView.Adapter<PurchaseAdapter.ImageView>{
    private Context context;
    private List<PurchaseModel> purchaseModels;

    public PurchaseAdapter(Context context, List<PurchaseModel> purchaseModels) {
        this.context = context;
        this.purchaseModels = purchaseModels;
    }

    @NonNull
    @Override
    public PurchaseAdapter.ImageView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_purchase,parent,false);
        return new PurchaseAdapter.ImageView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PurchaseAdapter.ImageView holder, int position) {
        final PurchaseModel purchaseModel = purchaseModels.get(position);
        holder.TYPE.setText(purchaseModel.getType());
        holder.AMOUNT.setText(purchaseModel.getAmount());
    }

    @Override
    public int getItemCount() {
        return purchaseModels.size();
    }

    public class ImageView extends RecyclerView.ViewHolder {
        TextView TYPE,AMOUNT;

        public ImageView(@NonNull View itemView) {
            super(itemView);
            TYPE = itemView.findViewById(R.id.type);
            AMOUNT = itemView.findViewById(R.id.amount);
        }
    }
}
