package com.example.sriganesh.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sriganesh.Model.PaymentModel;
import com.example.sriganesh.R;

import java.util.ArrayList;
import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder>{

    private Context mContext;
    private List<PaymentModel> paymentModel = new ArrayList<>();


    public PaymentAdapter(Context mContext, List<PaymentModel> paymentModel) {
        this.mContext = mContext;
        this.paymentModel = paymentModel;
    }

    @NonNull
    @Override
    public PaymentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_detailes,parent,false);
        return new PaymentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentAdapter.ViewHolder holder, int position) {

        final PaymentModel paymentModels = paymentModel.get(position);

        holder.TYPE.setText(paymentModels.getPayment_Type());
        holder.NAME.setText(paymentModels.getPayment_Name());
        holder.AMOUNT.setText("₹"+paymentModels.getPayment_Amount());
    }

    @Override
    public int getItemCount() {
        return paymentModel.size();
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
