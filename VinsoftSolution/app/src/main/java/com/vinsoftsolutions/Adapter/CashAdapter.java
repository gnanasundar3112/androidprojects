package com.vinsoftsolutions.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vinsoftsolutions.Models.CashModel;
import com.vinsoftsolutions.R;

import java.util.List;

public class CashAdapter extends RecyclerView.Adapter<CashAdapter.ImageView> {
    private Context context;
    private List<CashModel> cashModels;

    public CashAdapter(Context context, List<CashModel> cashModels) {
        this.context = context;
        this.cashModels = cashModels;
    }

    @NonNull
    @Override
    public CashAdapter.ImageView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cash,parent,false);
        return new CashAdapter.ImageView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CashAdapter.ImageView holder, int position) {
        final CashModel cashModel = cashModels.get(position);

        holder.AC_NAME.setText(cashModel.getAc_name());
        holder.AMOUNT.setText(""+cashModel.getAmount());
    }

    @Override
    public int getItemCount() {
        return cashModels.size();
    }

    public class ImageView extends RecyclerView.ViewHolder {
        private TextView AC_NAME,AMOUNT;
        public ImageView(@NonNull View itemView) {
            super(itemView);
            AC_NAME = itemView.findViewById(R.id.ac_name);
            AMOUNT = itemView.findViewById(R.id.amount);
        }
    }
}
