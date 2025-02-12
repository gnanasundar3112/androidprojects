package com.vinsoftsolutions.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vinsoftsolutions.Models.SalesModel;
import com.vinsoftsolutions.R;

import java.util.List;

public class SalesAdapter extends RecyclerView.Adapter<SalesAdapter.ImageView>{
    private Context context;
    private List<SalesModel> salesModels;

    public SalesAdapter(Context context, List<SalesModel> salesModels) {
        this.context = context;
        this.salesModels = salesModels;
    }

    @NonNull
    @Override
    public SalesAdapter.ImageView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_purchase,parent,false);
        return new SalesAdapter.ImageView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SalesAdapter.ImageView holder, int position) {
        final SalesModel salesModel = salesModels.get(position);
        holder.TYPE.setText(salesModel.getType());
//        holder.NAME.setText(salesModel.getAc_name());
        holder.AMOUNT.setText(""+salesModel.getAmount());
    }

    @Override
    public int getItemCount() {
        return salesModels.size();
    }

    public class ImageView extends RecyclerView.ViewHolder {
        TextView TYPE,NAME,AMOUNT;
        public ImageView(@NonNull View itemView) {
            super(itemView);
            TYPE = itemView.findViewById(R.id.type);
//            NAME = itemView.findViewById(R.id.name);
            AMOUNT = itemView.findViewById(R.id.amount);
        }
    }
}
