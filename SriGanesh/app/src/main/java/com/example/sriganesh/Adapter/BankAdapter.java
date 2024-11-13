package com.example.sriganesh.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sriganesh.Model.BankModel;
import com.example.sriganesh.R;

import java.util.ArrayList;
import java.util.List;

public class BankAdapter extends RecyclerView.Adapter<BankAdapter.ViewHolder>{

    private Context mContext;
    private List<BankModel> bankModel = new ArrayList<>();


    public BankAdapter(Context mContext, List<BankModel> bankModel) {
        this.mContext = mContext;
        this.bankModel = bankModel;
    }

    @NonNull
    @Override
    public BankAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_detailes,parent,false);
        return new BankAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BankAdapter.ViewHolder holder, int position) {

        final BankModel bankModels = bankModel.get(position);

        holder.TYPE.setText(bankModels.getBank_Type());
        holder.NAME.setText(bankModels.getBank_Name());
        holder.AMOUNT.setText("₹"+bankModels.getBank_Amount());
    }

    @Override
    public int getItemCount() {
        return bankModel.size();
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
