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

public class ProductMainAdapter extends RecyclerView.Adapter<ProductMainAdapter.ImageViewHolder>{
    private Context mContext;
    private List<ProductModel> products = new ArrayList<>();

    public ProductMainAdapter(Context mContext, List<ProductModel> products) {
        this.mContext = mContext;
        this.products = products;
    }

    public void setFilteredList(List<ProductModel>filteredList){
        this.products=filteredList;
        notifyDataSetChanged();
    }
    // filter from search bar End
    @NonNull
    @Override
    public ProductMainAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_by_product,parent,false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductMainAdapter.ImageViewHolder holder, int position) {
        final ProductModel productModels = products.get(position);

        holder.PROD_NAME.setText(productModels.getProd_name());
        holder.PROD_TAM_NAME.setText(productModels.getProd_tam_name());
        holder.PROD_PRICE.setText(""+productModels.getProd_price());
        holder.PROD_SIZE.setText(productModels.getSize_name());

        Glide.with(mContext).load(productModels.getProd_image()).placeholder(R.drawable.prograss_animination).into(holder.PROD_IMAGE);

        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.slide_in_left);
        holder.itemView.startAnimation(animation);

        // send item in detail Activity start
        holder.DETAIL_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = String.valueOf("0100001");

                Intent intent;

                intent = new Intent(mContext, ProductDetail.class);

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
        TextView PROD_NAME,PROD_SIZE,PROD_PRICE,PROD_TAM_NAME;
        private LinearLayout DETAIL_BTN;
        public ImageViewHolder(View itemView) {
            super(itemView);

            PROD_IMAGE = itemView.findViewById(R.id.main_prod_img);
            PROD_NAME = itemView.findViewById(R.id.main_prod_name);
            PROD_SIZE = itemView.findViewById(R.id.main_prod_size);
            PROD_PRICE = itemView.findViewById(R.id.main_prod_rate);
            PROD_TAM_NAME = itemView.findViewById(R.id.main_prod_tamil);
            DETAIL_BTN  = itemView.findViewById(R.id.main_det_btn);
        }
    }
}
