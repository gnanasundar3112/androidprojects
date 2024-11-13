package com.example.sriganesh.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sriganesh.Model.CashModel;
import com.example.sriganesh.R;

import java.util.ArrayList;
import java.util.List;

public class CashAdapter extends RecyclerView.Adapter<CashAdapter.ViewHolder>{

    private Context mContext;
    private List<CashModel> cashModel = new ArrayList<>();


    public CashAdapter(Context mContext, List<CashModel> cashModel) {
        this.mContext = mContext;
        this.cashModel = cashModel;
    }

    @NonNull
    @Override
    public CashAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_detail_2,parent,false);
        return new CashAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CashAdapter.ViewHolder holder, int position) {

        final CashModel cashModels = cashModel.get(position);

        holder.NAME.setText(cashModels.getCash_Name());
        holder.AMOUNT.setText("₹"+cashModels.getCash_Amount());
    }

    @Override
    public int getItemCount() {
        return cashModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView NAME,AMOUNT;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            NAME = itemView.findViewById(R.id.name);
            AMOUNT = itemView.findViewById(R.id.amount);
        }
    }
}
