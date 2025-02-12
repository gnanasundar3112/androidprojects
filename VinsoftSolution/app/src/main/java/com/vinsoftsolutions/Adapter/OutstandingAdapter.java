package com.vinsoftsolutions.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vinsoftsolutions.Models.OutstandingModel;
import com.vinsoftsolutions.R;

import java.util.List;

public class OutstandingAdapter extends RecyclerView.Adapter<OutstandingAdapter.ImageView>{
    private Context context;
    private List<OutstandingModel> outstandingModels;

    public OutstandingAdapter(Context context, List<OutstandingModel> outstandingModels) {
        this.context = context;
        this.outstandingModels = outstandingModels;
    }

    @NonNull
    @Override
    public OutstandingAdapter.ImageView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_detailes,parent,false);
        return new OutstandingAdapter.ImageView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OutstandingAdapter.ImageView holder, int position) {
        final OutstandingModel outstandingModel = outstandingModels.get(position);

        holder.TYPE.setText(outstandingModel.getType());
        holder.NAME.setText(outstandingModel.getAc_name());
        holder.AMOUNT.setText(""+outstandingModel.getAmount());
    }

    @Override
    public int getItemCount() {
        return outstandingModels.size();
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
