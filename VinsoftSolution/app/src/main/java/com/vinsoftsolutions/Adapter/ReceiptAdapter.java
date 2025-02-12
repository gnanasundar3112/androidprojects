package com.vinsoftsolutions.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vinsoftsolutions.Models.ReceiptModel;
import com.vinsoftsolutions.R;

import java.util.List;

public class ReceiptAdapter extends RecyclerView.Adapter<ReceiptAdapter.ImageView>{
    private Context context;
    private List<ReceiptModel> receiptModels;

    public ReceiptAdapter(Context context, List<ReceiptModel> receiptModels) {
        this.context = context;
        this.receiptModels = receiptModels;
    }

    @NonNull
    @Override
    public ReceiptAdapter.ImageView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_detailes,parent,false);
        return new ReceiptAdapter.ImageView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiptAdapter.ImageView holder, int position) {
        final ReceiptModel receiptModel = receiptModels.get(position);
        holder.TYPE.setText(receiptModel.getType());
        holder.NAME.setText(receiptModel.getAc_name());
        holder.AMOUNT.setText(""+receiptModel.getAmount());
    }

    @Override
    public int getItemCount() {
        return receiptModels.size();
    }

    public class ImageView extends RecyclerView.ViewHolder {
        TextView TYPE,NAME,AMOUNT;
        public ImageView(@NonNull View itemView) {
            super(itemView);
            TYPE = itemView.findViewById(R.id.type);
            NAME = itemView.findViewById(R.id.name);
            AMOUNT = itemView.findViewById(R.id.amount);
        }
    }
}
