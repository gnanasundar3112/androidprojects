package in.vinsoftsolutions.jayagrocery.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import in.vinsoftsolutions.jayagrocery.Models.ProductModel;
import in.vinsoftsolutions.jayagrocery.ProductDetail;

import in.vinsoftsolutions.jayagrocery.R;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailAdapter extends RecyclerView.Adapter<ProductDetailAdapter.ImageViewHolder>{
    private Context mContext;
    private List<ProductModel> products = new ArrayList<>();

    public ProductDetailAdapter(Context context, List<ProductModel> products) {
        this.mContext = context;
        this.products = products;
    }

    @NonNull
    @Override
    public ProductDetailAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_detail_prod,parent,false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductDetailAdapter.ImageViewHolder holder, int position) {
        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.slide_in_left);
        holder.itemView.startAnimation(animation);

        final ProductModel productModels = products.get(position);

        holder.PROD_NAME.setText(productModels.getProd_name());
        holder.PROD_TAM_NAME.setText(productModels.getProd_tam_name());
        holder.PROD_SIZE.setText(productModels.getSize_name());
        holder.PROD_PRICE.setText("₹ "+productModels.getProd_price());

        Glide.with(mContext).load(productModels.getProd_image()).placeholder(R.drawable.prograss_animination).into(holder.PROD_IMAGE);

        holder.DETAIL_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ProductDetail.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                intent.putExtra("prod_id",productModels.getProd_id());
                intent.putExtra("prod_name",productModels.getProd_name());
                intent.putExtra("prod_tam_name",productModels.getProd_tam_name());
                intent.putExtra("rate",productModels.getProd_price());
                intent.putExtra("stock_type",productModels.getProd_stock());
                intent.putExtra("prod_img",productModels.getProd_image());

                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView PROD_IMAGE;
        MaterialTextView PROD_NAME,PROD_PRICE,PROD_TAM_NAME,PROD_SIZE;
        private CardView DETAIL_BTN;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            PROD_IMAGE = itemView.findViewById(R.id.del_prod_img);
            PROD_NAME = itemView.findViewById(R.id.del_prod_name);
            PROD_SIZE = itemView.findViewById(R.id.del_prod_size);
            PROD_PRICE = itemView.findViewById(R.id.del_prod_rate);
            PROD_TAM_NAME = itemView.findViewById(R.id.del_prod_tamil);
            DETAIL_BTN  = itemView.findViewById(R.id.del_det_btn);
        }
    }
}
