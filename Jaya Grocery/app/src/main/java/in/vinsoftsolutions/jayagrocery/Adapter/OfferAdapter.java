package in.vinsoftsolutions.jayagrocery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

import in.vinsoftsolutions.jayagrocery.Models.OfferModel;
import in.vinsoftsolutions.jayagrocery.Models.Orders_Detail_Model;
import in.vinsoftsolutions.jayagrocery.R;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.ImageViewHolder> {

    private Context mContext;
    private List<OfferModel> offerModels = new ArrayList<>();
    private int currentPosition = 0;

    public OfferAdapter(Context mContext, List<OfferModel> offerModels) {
        this.mContext = mContext;
        this.offerModels = offerModels;
    }

    @NonNull
    @Override
    public OfferAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_deals,parent,false);
        return new OfferAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferAdapter.ImageViewHolder holder, int position) {

        final  OfferModel offerModel = offerModels.get(position);

        holder.OFFER_NAME.setText(offerModel.getOFFER_NAME());
        holder.OFFER_TYPE.setText(offerModel.getOFFER_TYPE());
        holder.OFFER_STORAGE_LIFE.setText("Storage Life/"+offerModel.getOFFER_STORAGE_LIFE());
        holder.OFFER_PRICE.setText("₹"+offerModel.getOFFER_PRICE());
        holder.OFFER_PROD_RATE.setText("₹"+offerModel.getOFFER_PROD_RATE()+" per kg");
        holder.OFFER_DESCRIPTION.setText(offerModel.getOFFER_DESCRIPTION());
        holder.OFFER_PERCENTAGE.setText("-"+offerModel.getOFFER_PERCENTAGE()+"%");

        Glide.with(mContext).load(offerModel.getOFFER_IMAGE()).into(holder.OFFER_IMAGE);

    }

    @Override
    public int getItemCount() {
        return offerModels.size();
    }
    public void setCurrentPosition(int position) {
        currentPosition = position;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        private MaterialTextView OFFER_NAME,OFFER_TYPE,OFFER_STORAGE_LIFE,OFFER_PRICE,OFFER_PROD_RATE,OFFER_DESCRIPTION,OFFER_PERCENTAGE;
        private ImageView OFFER_IMAGE;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            OFFER_NAME = itemView.findViewById(R.id.dealsTitle);
            OFFER_TYPE = itemView.findViewById(R.id.dealsType);
            OFFER_STORAGE_LIFE = itemView.findViewById(R.id.dealsStorageLife);
            OFFER_PRICE = itemView.findViewById(R.id.dealsDiscountedPrice);
            OFFER_PROD_RATE = itemView.findViewById(R.id.dealsOriginalPrice);
            OFFER_DESCRIPTION = itemView.findViewById(R.id.dealsDescription);
            OFFER_PERCENTAGE = itemView.findViewById(R.id.dealsDiscountPercentage);
            OFFER_IMAGE = itemView.findViewById(R.id.dealsImage);
        }
    }
}
