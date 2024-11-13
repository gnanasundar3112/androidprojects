package in.vinsoftsolutions.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.vinsoftsolutions.Model.DeliveryModel;
import in.vinsoftsolutions.R;

public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryAdapter.ImageViewHoldert> {
    private Context mContext;
    private List<DeliveryModel> deliveryModel = new ArrayList<>();

    public DeliveryAdapter(Context mContext, List<DeliveryModel> deliveryModel) {
        this.mContext = mContext;
        this.deliveryModel = deliveryModel;
    }
    @NonNull
    @Override
    public DeliveryAdapter.ImageViewHoldert onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_delivered,parent,false);
        return new DeliveryAdapter.ImageViewHoldert(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveryAdapter.ImageViewHoldert holder, int position) {
        final DeliveryModel deliveryModels = deliveryModel.get(position);

        String STATUS = "2";
        if (STATUS.equalsIgnoreCase(deliveryModels.getDelivery_Status())){
            holder.DELIVERY_STATUS.setText("Delivered");
        }
        holder.ORDER_NO.setText(deliveryModels.getOrder_No());

        //date change format start
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");

        try {
            Date deliver_date = inputFormat.parse(deliveryModels.getDelivery_Date());
            String kdt = outputFormat.format(deliver_date);

            holder.DELIVERY_DATE.setText(kdt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //date change format end
    }

    @Override
    public int getItemCount() {
        return deliveryModel.size();
    }

    public class ImageViewHoldert extends RecyclerView.ViewHolder {
        TextView ORDER_NO,DELIVERY_DATE,DELIVERY_STATUS;
        public ImageViewHoldert(@NonNull View itemView) {
            super(itemView);
            ORDER_NO = itemView.findViewById(R.id.order_no);
            DELIVERY_DATE = itemView.findViewById(R.id.delivery_date);
            DELIVERY_STATUS = itemView.findViewById(R.id.delivery_status);
        }
    }
}
