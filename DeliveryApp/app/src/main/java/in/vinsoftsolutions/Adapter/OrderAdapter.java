package in.vinsoftsolutions.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.vinsoftsolutions.Model.OrderModel;
import in.vinsoftsolutions.Orders_Status_Detail;
import in.vinsoftsolutions.R;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ImageViewHolder> {

    private Context mContext;
    private List<OrderModel> orderModel = new ArrayList<>();

    public OrderAdapter(Context mContext, List<OrderModel> orderModel) {
        this.mContext = mContext;
        this.orderModel = orderModel;
    }
    @NonNull
    @Override
    public OrderAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_order,parent,false);
        return new OrderAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ImageViewHolder holder, int position) {
        final OrderModel orderModels = orderModel.get(position);

        //date change format start
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date Orederd_date = inputFormat.parse(orderModels.getOrder_Date());
            String kdt = outputFormat.format(Orederd_date);

            holder.ORDER_DATE.setText(kdt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //date change format end

        holder.ORDER_NO.setText(orderModels.getOrder_No());
        holder.CUSTOMER_PHONE.setText(orderModels.getOrder_Add_Mobile());

        holder.ORDER_BUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, Orders_Status_Detail.class);

                intent.putExtra("order_no",orderModels.getOrder_No());
                intent.putExtra("order_date",orderModels.getOrder_Date());
                intent.putExtra("cust_id",orderModels.getOrder_Cust_Id());
                intent.putExtra("amount",orderModels.getOrder_Amount());
                intent.putExtra("qty",orderModels.getOrder_Qty());
                intent.putExtra("deli_type",orderModels.getOrder_Deli_Type());
                intent.putExtra("add_id",orderModels.getOrder_Add_id());
                intent.putExtra("add_name",orderModels.getOrder_Add_Name());
                intent.putExtra("add_mobile",orderModels.getOrder_Add_Mobile());
                intent.putExtra("add_address",orderModels.getOrder_Add_Address());
                intent.putExtra("add_city",orderModels.getOrder_Add_City());
                intent.putExtra("add_state",orderModels.getOrder_Add_State());
                intent.putExtra("add_pincode",orderModels.getOrder_Add_Pincode());

                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderModel.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        TextView ORDER_NO, ORDER_DATE, CUSTOMER_PHONE;
        LinearLayout ORDER_BUTTON;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            ORDER_NO = itemView.findViewById(R.id.item_order_no);
            ORDER_DATE = itemView.findViewById(R.id.item_order_date);
            CUSTOMER_PHONE = itemView.findViewById(R.id.item_order_phone);
            ORDER_BUTTON = itemView.findViewById(R.id.order_btn);
        }
    }
}
