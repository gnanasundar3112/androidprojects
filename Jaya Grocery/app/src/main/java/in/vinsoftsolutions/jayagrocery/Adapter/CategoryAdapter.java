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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import in.vinsoftsolutions.jayagrocery.Cate_products;
import in.vinsoftsolutions.jayagrocery.Models.CategoryModel;
import in.vinsoftsolutions.jayagrocery.R;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ImageViewHolder> {

    private Context mContext;
    private List<CategoryModel> categoryModel = new ArrayList<>();
    public CategoryAdapter(Context mContext, List<CategoryModel> categoryModel) {
        this.mContext = mContext;
        this.categoryModel = categoryModel;
    }

    @NonNull
    @Override
    public CategoryAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_categories,parent,false);
        return new CategoryAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ImageViewHolder holder, int position) {
        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.slide_in_left);
        holder.itemView.startAnimation(animation);

        final CategoryModel categoryModels = categoryModel.get(position);

        holder.CATE_ENG_NAME.setText(categoryModels.getCate_eng_name());
        holder.CATE_TAM_NAME.setText(categoryModels.getCate_tam_name());

        Glide.with(mContext).load(categoryModels.getCate_image()).placeholder(R.drawable.prograss_animination).into(holder.CATE_IMAGE);

        holder.CATE_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, Cate_products.class);

                intent.putExtra("cate_id",categoryModels.getCate_id());
                intent.putExtra("cate_name",categoryModels.getCate_eng_name());
                intent.putExtra("tamil_name",categoryModels.getCate_tam_name());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryModel.size();
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageView CATE_IMAGE;
        TextView CATE_ENG_NAME,CATE_TAM_NAME;
        CardView CATE_BTN;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            CATE_IMAGE = itemView.findViewById(R.id.categoriesImage);
            CATE_ENG_NAME = itemView.findViewById(R.id.cate_eng_Name);
            CATE_TAM_NAME = itemView.findViewById(R.id.cate_tam_Name);
            CATE_BTN = itemView.findViewById(R.id.cate_btn);
        }
    }
}
