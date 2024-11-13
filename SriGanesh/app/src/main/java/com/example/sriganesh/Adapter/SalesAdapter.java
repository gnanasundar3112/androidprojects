package com.example.sriganesh.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sriganesh.Model.SalesModel;
import com.example.sriganesh.R;

import java.util.ArrayList;
import java.util.List;

public class SalesAdapter extends RecyclerView.Adapter<SalesAdapter.ViewHolder>{

    private Context mContext;
    private List<SalesModel> salesModel = new ArrayList<>();


    public SalesAdapter(Context mContext, List<SalesModel> salesModel) {
        this.mContext = mContext;
        this.salesModel = salesModel;
    }

    @NonNull
    @Override
    public SalesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_detailes,parent,false);
        return new SalesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SalesAdapter.ViewHolder holder, int position) {

        final SalesModel salesModels = salesModel.get(position);

        holder.TYPE.setText(salesModels.getSales_Type());
        holder.NAME.setText(salesModels.getSales_Name());
        holder.AMOUNT.setText("₹"+salesModels.getSales_Amount());
    }

    @Override
    public int getItemCount() {
        return salesModel.size();
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
