package com.example.sriganesh.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.sriganesh.Model.RecieptModel;
import com.example.sriganesh.R;

import java.util.ArrayList;
import java.util.List;

public class RecieptAdapter extends RecyclerView.Adapter<RecieptAdapter.ViewHolder>{

    private Context mContext;
    private List<RecieptModel> recieptModel = new ArrayList<>();


    public RecieptAdapter(Context mContext, List<RecieptModel> recieptModel) {
        this.mContext = mContext;
        this.recieptModel = recieptModel;
    }

    @NonNull
    @Override
    public RecieptAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_detailes,parent,false);
        return new RecieptAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecieptAdapter.ViewHolder holder, int position) {

        final RecieptModel recieptModels = recieptModel.get(position);

        holder.TYPE.setText(recieptModels.getReciept_Type());
        holder.NAME.setText(recieptModels.getReciept_Name());
        holder.AMOUNT.setText("₹"+recieptModels.getReciept_Amount());
    }

    @Override
    public int getItemCount() {
        return recieptModel.size();
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
