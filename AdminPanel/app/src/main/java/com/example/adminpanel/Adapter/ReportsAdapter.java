package com.example.adminpanel.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminpanel.Model.Cate_Model;
import com.example.adminpanel.Model.ReportsModel;
import com.example.adminpanel.R;
import com.example.adminpanel.ReportsDetail;

import java.util.ArrayList;
import java.util.List;

public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.ImageViewHolder>{
    private Context mContext;
    private List<ReportsModel> reportsModel = new ArrayList<>();

    public ReportsAdapter(Context mContext, List<ReportsModel> reportsModel) {
        this.mContext = mContext;
        this.reportsModel = reportsModel;
    }

    @NonNull
    @Override
    public ReportsAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_reports,parent,false);
        return new ReportsAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportsAdapter.ImageViewHolder holder, int position) {
        final ReportsModel reportsModels = reportsModel.get(position);

        holder.REPORTS_ORDER_NO.setText(reportsModels.getReport_order_no());
        holder.REPORTS_ORDER_DATE.setText(reportsModels.getReport_order_date());
        holder.REPORTS_MOBILE_NO.setText(reportsModels.getReport_mobile_no());
        holder.REPORTS_STATUS.setText(reportsModels.getReport_status());
        holder.REPORTS_QTY.setText(reportsModels.getReport_qty());
        holder.REPORTS_AMOUNT.setText(""+reportsModels.getReport_amount());


        String ORDER_STATUS = String.valueOf("Ordered");
        String DELI_STATUS = String.valueOf("Delivered");
        String CANCEL_STATUS = String.valueOf("Cancel");

        if (ORDER_STATUS.equalsIgnoreCase(reportsModels.getReport_status())){
            holder.REPORTS_STATUS.setTextColor(Color.parseColor("#1B5E20"));
        }
        if (DELI_STATUS.equalsIgnoreCase(reportsModels.getReport_status())){
            holder.REPORTS_STATUS.setTextColor(Color.parseColor("#1C37E6"));
        }
        if (CANCEL_STATUS.equalsIgnoreCase(reportsModels.getReport_status())){
            holder.REPORTS_STATUS.setTextColor(Color.parseColor("#EA495F"));
        }


        holder.REPORTS_VIEW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ReportsDetail.class);
                intent.putExtra("order_no",reportsModels.getReport_order_no());
                intent.putExtra("order_date",reportsModels.getReport_order_date());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reportsModel.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        TextView REPORTS_ORDER_NO,REPORTS_ORDER_DATE,REPORTS_MOBILE_NO,REPORTS_STATUS,REPORTS_QTY,REPORTS_AMOUNT;
        ImageButton  REPORTS_VIEW;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            REPORTS_ORDER_NO = itemView.findViewById(R.id.report_order_no);
            REPORTS_ORDER_DATE = itemView.findViewById(R.id.report_order_date);
            REPORTS_MOBILE_NO = itemView.findViewById(R.id.report_phone_no);
            REPORTS_STATUS = itemView.findViewById(R.id.report_status);
            REPORTS_QTY = itemView.findViewById(R.id.report_qty);
            REPORTS_AMOUNT = itemView.findViewById(R.id.report_amount);
            REPORTS_VIEW = itemView.findViewById(R.id.report_view_btn);
        }
    }
}
