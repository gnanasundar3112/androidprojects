package in.vinsoftsolutions.jayagrocery.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import in.vinsoftsolutions.jayagrocery.Address;
import in.vinsoftsolutions.jayagrocery.Models.AddressModel;

import in.vinsoftsolutions.jayagrocery.R;

import java.util.ArrayList;
import java.util.List;

public class Order_add_Adapter extends RecyclerView.Adapter<Order_add_Adapter.ImageViewHolder> {
    private Context mContext;
    private List<AddressModel> addressModel = new ArrayList<>();
    Dialog DIALOG;
    ArrayList<String> add_list = new ArrayList<>();
    ArrayList<String> add_id_list = new ArrayList<>();
    ArrayAdapter<String> add_adapter;
    public Order_add_Adapter(Context mContext, List<AddressModel> addressModel) {
        this.mContext = mContext;
        this.addressModel = addressModel;
    }

    @NonNull
    @Override
    public Order_add_Adapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_add_conf_pag,parent,false);
        return new Order_add_Adapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Order_add_Adapter.ImageViewHolder holder, int position) {
        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.slide_in_left);
        holder.itemView.startAnimation(animation);

        final AddressModel addressModels = addressModel.get(position);

        holder.ADD_NAME.setText(addressModels.getAdd_name());
        holder.ADD_MOBILE.setText(addressModels.getAdd_mobile());
        holder.ADD_ADDRESS.setText(addressModels.getAdd_address()+","+addressModels.getAdd_city()+
                ","+addressModels.getAdd_state()+","+addressModels.getAdd_pinCode());

        holder.CHANGE_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof Activity) {
                    Intent intent = new Intent(mContext, Address.class);
                    intent.putExtra("add_id", "1");
                    mContext.startActivity(intent);
                    ((Activity) mContext).finish(); // Finish the current activity
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return addressModel.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        TextView ADD_NAME,ADD_MOBILE,ADD_ADDRESS;
        AppCompatButton CHANGE_BTN;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            ADD_NAME = itemView.findViewById(R.id.item_con_add_name);
            ADD_MOBILE = itemView.findViewById(R.id.item_con_add_mobile);
            ADD_ADDRESS = itemView.findViewById(R.id.item_con_add_address);

            CHANGE_BTN = itemView.findViewById(R.id.item_add_change);
        }
    }
}
