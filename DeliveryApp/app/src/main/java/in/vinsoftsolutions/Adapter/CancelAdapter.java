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

import in.vinsoftsolutions.Model.CancelModel;
import in.vinsoftsolutions.R;

public class CancelAdapter extends RecyclerView.Adapter<CancelAdapter.ImageViewHolder> {

    private Context mContext;
    private List<CancelModel> cancelModel = new ArrayList<>();

    public CancelAdapter(Context mContext, List<CancelModel> cancelModel) {
        this.mContext = mContext;
        this.cancelModel = cancelModel;
    }

    @NonNull
    @Override
    public CancelAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_cancel,parent,false);
        return new CancelAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CancelAdapter.ImageViewHolder holder, int position) {
        final CancelModel cancelModels = cancelModel.get(position);

        String STATUS = "3";
        if (STATUS.equalsIgnoreCase(cancelModels.getCancel_Status())){
            holder.CANCEL_STATUS.setText("Canceled");
        }

        holder.ORDER_NO.setText(cancelModels.getOrder_No());
        //date change format start
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");

        try {
            Date cancel_date = inputFormat.parse(cancelModels.getCancel_Date());
            String kdt = outputFormat.format(cancel_date);

            holder.CANCEL_DATE.setText(kdt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //date change format end
    }

    @Override
    public int getItemCount() {
        return cancelModel.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        TextView ORDER_NO,CANCEL_DATE,CANCEL_STATUS;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            ORDER_NO = itemView.findViewById(R.id.order_no);
            CANCEL_DATE = itemView.findViewById(R.id.cancel_date);
            CANCEL_STATUS = itemView.findViewById(R.id.cancel_status);
        }
    }
}
