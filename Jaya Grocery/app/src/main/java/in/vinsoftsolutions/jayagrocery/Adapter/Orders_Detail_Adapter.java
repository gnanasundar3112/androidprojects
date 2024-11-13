package in.vinsoftsolutions.jayagrocery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import in.vinsoftsolutions.jayagrocery.Models.Orders_Detail_Model;
import in.vinsoftsolutions.jayagrocery.R;
import com.google.android.material.textview.MaterialTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Orders_Detail_Adapter extends RecyclerView.Adapter<Orders_Detail_Adapter.ImageViewHolder> {

    private Context mContext;
    private List<Orders_Detail_Model> orders_detail_model = new ArrayList<>();

    public Orders_Detail_Adapter(Context mContext, List<Orders_Detail_Model> orders_detail_model) {
        this.mContext = mContext;
        this.orders_detail_model = orders_detail_model;
    }

    @NonNull
    @Override
    public Orders_Detail_Adapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_my_orders_detaile,parent,false);
        return new Orders_Detail_Adapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Orders_Detail_Adapter.ImageViewHolder holder, int position) {

        final Orders_Detail_Model detail_model = orders_detail_model.get(position);

        holder.ORDER_DETAIL_NAME.setText(detail_model.getCancel_Name()+"  /  "+ detail_model.getCancel_Tamil());

        holder.ORDER_DETAIL_RATE.setText("₹ "+detail_model.getCancel_Rate());
        holder.ORDER_DETAIL_GRAM.setText("Size : "+detail_model.getCancel_Gram());

        double qty = Double.parseDouble(detail_model.getCancel_Qty());
        holder.ORDER_DETAIL_QTY.setText(String.valueOf("Quantity : "+qty));

        //holder.CANCEL_NAME.setTextColor(R.color.red);

        Glide.with(mContext).load(detail_model.getCancel_Image()).placeholder(R.drawable.prograss_animination).into(holder.ORDER_DETAIL_IMAGE);

        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.slide_in_left);
        holder.itemView.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return orders_detail_model.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        private MaterialTextView ORDER_DETAIL_NAME,ORDER_DETAIL_RATE,ORDER_DETAIL_GRAM,ORDER_DETAIL_QTY;
        private ImageView ORDER_DETAIL_IMAGE;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            ORDER_DETAIL_NAME = itemView.findViewById(R.id.order_detail_prod_name);
            ORDER_DETAIL_RATE = itemView.findViewById(R.id.order_detail_rate);
            ORDER_DETAIL_GRAM = itemView.findViewById(R.id.order_detail_gram);
            ORDER_DETAIL_QTY = itemView.findViewById(R.id.order_detail_qty);
            ORDER_DETAIL_IMAGE = itemView.findViewById(R.id.order_detail_image);
        }
    }
}
