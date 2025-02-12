package com.vinsoftsolutions.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vinsoftsolutions.Models.PaymentModel;
import com.vinsoftsolutions.R;

import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ImageView>{
    private Context context;
    private List<PaymentModel> paymentModels;

    public PaymentAdapter(Context context, List<PaymentModel> paymentModels) {
        this.context = context;
        this.paymentModels = paymentModels;
    }

    @NonNull
    @Override
    public PaymentAdapter.ImageView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_detailes,parent,false);
        return new PaymentAdapter.ImageView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentAdapter.ImageView holder, int position) {
        final PaymentModel paymentModel = paymentModels.get(position);

        holder.TYPE.setText(paymentModel.getType());
        holder.NAME.setText(paymentModel.getAc_name());
        holder.AMOUNT.setText(""+paymentModel.getAmount());
    }

    @Override
    public int getItemCount() {
        return paymentModels.size();
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
