package in.vinsoftsolutions.jayagrocery.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import in.vinsoftsolutions.jayagrocery.Models.ProductModel;
import in.vinsoftsolutions.jayagrocery.ProductDetail;
import in.vinsoftsolutions.jayagrocery.R;

import java.util.ArrayList;
import java.util.List;

public class Cate_Prod_Adapter extends RecyclerView.Adapter<Cate_Prod_Adapter.ImageViewHolder>{

    private Context mContext;
    private List<ProductModel> cate_prod_models = new ArrayList<>();

    public Cate_Prod_Adapter(Context mContext, List<ProductModel> cate_prod_models) {
        this.mContext = mContext;
        this.cate_prod_models = cate_prod_models;
    }
    public void setFilteredList(List<ProductModel>filteredList){
        this.cate_prod_models=filteredList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public Cate_Prod_Adapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_cate_prod,parent,false);
        return new Cate_Prod_Adapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Cate_Prod_Adapter.ImageViewHolder holder, int position) {

        final ProductModel cate_prod_model = cate_prod_models.get(position);

        holder.CATE_PROD_NAME.setText(cate_prod_model.getProd_name());
        holder.CATE_PROD_TAMIL.setText(cate_prod_model.getProd_tam_name());
        holder.CATE_PROD_SIZE.setText(cate_prod_model.getSize_name());
        holder.CATE_PROD_PRICE.setText("Rs. "+cate_prod_model.getProd_price());

        Glide.with(mContext).load(cate_prod_model.getProd_image()).placeholder(R.drawable.prograss_animination).into(holder.CATE_PROD_IMG);

        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.slide_in_left);
        holder.itemView.startAnimation(animation);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ProductDetail.class);

                intent.putExtra("prod_id",cate_prod_model.getProd_id());
                intent.putExtra("prod_name",cate_prod_model.getProd_name());
                intent.putExtra("prod_tam_name",cate_prod_model.getProd_tam_name());
                intent.putExtra("rate",cate_prod_model.getProd_price());
                intent.putExtra("stock_type",cate_prod_model.getProd_stock());
                intent.putExtra("prod_img",cate_prod_model.getProd_image());

                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cate_prod_models.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        private ImageView CATE_PROD_IMG;
        private TextView CATE_PROD_NAME,CATE_PROD_TAMIL,CATE_PROD_PRICE,CATE_PROD_SIZE;
        private LinearLayout CATE_PROD_DEL_BTN;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            CATE_PROD_IMG = itemView.findViewById(R.id.cate_prod_img);
            CATE_PROD_NAME = itemView.findViewById(R.id.cate_prod_name);
            CATE_PROD_TAMIL = itemView.findViewById(R.id.cate_prod_tamil);
            CATE_PROD_SIZE = itemView.findViewById(R.id.cate_prod_size);
            CATE_PROD_PRICE = itemView.findViewById(R.id.cate_prod_price);
            CATE_PROD_DEL_BTN  = itemView.findViewById(R.id.detail_btn);
        }
    }
}
