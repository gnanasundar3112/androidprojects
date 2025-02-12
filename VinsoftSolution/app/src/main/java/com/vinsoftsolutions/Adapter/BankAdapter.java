package com.vinsoftsolutions.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vinsoftsolutions.Models.BankModel;
import com.vinsoftsolutions.R;

import java.util.List;

public class BankAdapter extends RecyclerView.Adapter<BankAdapter.ImageView>{
    private Context context;
    private List<BankModel> bankModels;

    public BankAdapter(Context context, List<BankModel> bankModels) {
        this.context = context;
        this.bankModels = bankModels;
    }

    @NonNull
    @Override
    public BankAdapter.ImageView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_detailes,parent,false);
        return new BankAdapter.ImageView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BankAdapter.ImageView holder, int position) {
        final BankModel bankModel = bankModels.get(position);

        holder.TYPE.setText(bankModel.getType());
        holder.NAME.setText(bankModel.getAc_name());
        holder.AMOUNT.setText(""+bankModel.getAmount());
    }

    @Override
    public int getItemCount() {
        return bankModels.size();
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
