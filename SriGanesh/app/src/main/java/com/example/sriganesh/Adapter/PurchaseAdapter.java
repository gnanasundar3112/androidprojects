package com.example.sriganesh.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sriganesh.Model.PurchaseModel;
import com.example.sriganesh.R;

import java.util.ArrayList;
import java.util.List;

public class PurchaseAdapter extends RecyclerView.Adapter<PurchaseAdapter.ViewHolder>{

    private Context mContext;
    private List<PurchaseModel> purchaseModel = new ArrayList<>();


    public PurchaseAdapter(Context mContext, List<PurchaseModel> purchaseModel) {
        this.mContext = mContext;
        this.purchaseModel = purchaseModel;
    }

    @NonNull
    @Override
    public PurchaseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_detailes,parent,false);
        return new PurchaseAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PurchaseAdapter.ViewHolder holder, int position) {

        final PurchaseModel purchaseModels = purchaseModel.get(position);

        holder.TYPE.setText(purchaseModels.getPurchase_Type());
        holder.NAME.setText(purchaseModels.getPurchase_Name());
        holder.AMOUNT.setText("₹"+purchaseModels.getPurchase_Amount());
    }

    @Override
    public int getItemCount() {
        return purchaseModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView TYPE,NAME,AMOUNT;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            TYPE = itemView.findViewById(R.id.type);
            NAME = itemView.findViewById(R.id.name);
            AMOUNT = itemView.findViewById(R.id.amount);
        }
    }
}
