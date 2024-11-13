package com.example.adminpanel.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminpanel.Model.ReportsDetailModel;
import com.example.adminpanel.Model.ReportsModel;
import com.example.adminpanel.R;

import java.util.ArrayList;
import java.util.List;

public class ReportsDetailAdapter extends RecyclerView.Adapter<ReportsDetailAdapter.ImageViewHolder>{
    private Context mContext;
    private List<ReportsDetailModel> reportsDetailModel = new ArrayList<>();

    public ReportsDetailAdapter(Context mContext, List<ReportsDetailModel> reportsDetailModel) {
        this.mContext = mContext;
        this.reportsDetailModel = reportsDetailModel;
    }

    @NonNull
    @Override
    public ReportsDetailAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_report_detail,parent,false);
        return new ReportsDetailAdapter.ImageViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ReportsDetailAdapter.ImageViewHolder holder, int position) {
        final ReportsDetailModel reportsDetailModels = reportsDetailModel.get(position);

        holder.REPORTS_PROD_NAME.setText(reportsDetailModels.getReport_prod_name());
        holder.REPORTS_SIZE_NAME.setText(reportsDetailModels.getReport_size_name());
        holder.REPORTS_RATE.setText(reportsDetailModels.getReport_rate());
        holder.REPORTS_QTY.setText(reportsDetailModels.getReport_qty());
        holder.REPORTS_AMOUNT.setText(""+reportsDetailModels.getReport_amount());
    }

    @Override
    public int getItemCount() {
        return reportsDetailModel.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        TextView REPORTS_PROD_NAME,REPORTS_SIZE_NAME,REPORTS_RATE,REPORTS_QTY,REPORTS_AMOUNT;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            REPORTS_PROD_NAME = itemView.findViewById(R.id.report_prod_name);
            REPORTS_SIZE_NAME = itemView.findViewById(R.id.report_weight);
            REPORTS_RATE = itemView.findViewById(R.id.report_detail_rate);
            REPORTS_QTY = itemView.findViewById(R.id.report_detail_qty);
            REPORTS_AMOUNT = itemView.findViewById(R.id.report_detail_amount);

        }
    }
}
