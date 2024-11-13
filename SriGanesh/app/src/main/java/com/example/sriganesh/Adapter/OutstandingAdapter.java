package com.example.sriganesh.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sriganesh.Model.CashModel;
import com.example.sriganesh.Model.OutstandingModel;
import com.example.sriganesh.R;

import java.util.ArrayList;
import java.util.List;

public class OutstandingAdapter extends RecyclerView.Adapter<OutstandingAdapter.ViewHolder>{

    private Context mContext;
    private List<OutstandingModel> outstandingModel = new ArrayList<>();


    public OutstandingAdapter(Context mContext, List<OutstandingModel> outstandingModel) {
        this.mContext = mContext;
        this.outstandingModel = outstandingModel;
    }

    @NonNull
    @Override
    public OutstandingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_detailes,parent,false);
        return new OutstandingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OutstandingAdapter.ViewHolder holder, int position) {

        final OutstandingModel outstandingModels = outstandingModel.get(position);

        holder.TYPE.setText(outstandingModels.getOutstanding_Type());
        holder.NAME.setText(outstandingModels.getOutstanding_Name());
        holder.AMOUNT.setText("₹"+outstandingModels.getOutstanding_Amount());
    }

    @Override
    public int getItemCount() {
        return outstandingModel.size();
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
