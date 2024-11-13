package in.vinsoftsolutions.jayagrocery.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import in.vinsoftsolutions.jayagrocery.Models.My_order_Model;
import in.vinsoftsolutions.jayagrocery.My_Orders_Detail;
import in.vinsoftsolutions.jayagrocery.R;
import com.google.android.material.textview.MaterialTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class My_order_Adapter extends RecyclerView.Adapter<My_order_Adapter.ImageViewHolder> {

    private Context mContext;
    private List<My_order_Model> orderModelList = new ArrayList<>();

    public My_order_Adapter(Context mContext, List<My_order_Model> orderModelList) {
        this.mContext = mContext;
        this.orderModelList = orderModelList;
    }
    public void setFilteredList(List<My_order_Model>filteredList){
        this.orderModelList=filteredList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public My_order_Adapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_my_orders,parent,false);
        return new My_order_Adapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull My_order_Adapter.ImageViewHolder holder, int position) {

        final My_order_Model orderModel  = orderModelList.get(position);

        //date change format start
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");

        try {
            Date Orederd_date = inputFormat.parse(orderModel.getMy_order_date());
            String kdt = outputFormat.format(Orederd_date);

            holder.MY_ORDER_DATE.setText("Ordered On :   "+kdt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //date change format end

        holder.MY_ORDER_NUMBER.setText("Order No :   "+orderModel.getMy_order_number());
        holder.MY_ORDER_STATUS.setText(orderModel.getMy_order_status());

        Glide.with(mContext).load(orderModel.getMy_order_image()).placeholder(R.drawable.prograss_animination).into(holder.MY_ORDER_IMAGE);

        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.slide_in_left);
        holder.itemView.startAnimation(animation);

        String STATUS = String.valueOf("Canceled");
        if (STATUS.equalsIgnoreCase(orderModel.getMy_order_status())) {
            holder.MY_ORDER_STATUS.setTextColor(Color.parseColor("#EA495F"));
        } else {
            holder.MY_ORDER_STATUS.setTextColor(Color.parseColor("#1B5E20"));
        }

        holder.MY_ORDER_DETAILS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, My_Orders_Detail.class);
                intent.putExtra("order_no",orderModel.getMy_order_number());
                intent.putExtra("order_date",orderModel.getMy_order_date());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderModelList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        private ImageView MY_ORDER_IMAGE;
        private MaterialTextView MY_ORDER_DATE,MY_ORDER_NUMBER,MY_ORDER_STATUS;
        private LinearLayout MY_ORDER_DETAILS;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            MY_ORDER_IMAGE = itemView.findViewById(R.id.My_orderImage);
            MY_ORDER_DATE = itemView.findViewById(R.id.My_orderDate);
            MY_ORDER_NUMBER = itemView.findViewById(R.id.My_orderNumber);
            MY_ORDER_DETAILS = itemView.findViewById(R.id.order_detail);
            MY_ORDER_STATUS = itemView.findViewById(R.id.status_order);
        }
    }
}
