package in.vinsoftsolutions.jayagrocery.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import in.vinsoftsolutions.jayagrocery.Address;
import in.vinsoftsolutions.jayagrocery.MainActivity;
import in.vinsoftsolutions.jayagrocery.Models.AddressModel;
import in.vinsoftsolutions.jayagrocery.Profile;
import in.vinsoftsolutions.jayagrocery.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ImageViewHolder> implements PopupMenu.OnMenuItemClickListener{

    private Context mContext;
    private List<AddressModel> addressModel = new ArrayList<>();
    public AddressAdapter(Context mContext, List<AddressModel> addressModel) {
        this.mContext = mContext;
        this.addressModel = addressModel;
    }


    @Override
    public AddressAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_address,parent,false);
        return new AddressAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressAdapter.ImageViewHolder holder, int position) {

        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.slide_in_left);
        holder.itemView.startAnimation(animation);

        final AddressModel addressModels = addressModel.get(position);

        holder.add_name.setText(addressModels.getAdd_name());
        holder.add_mobile.setText(addressModels.getAdd_mobile());
        holder.add_address.setText(addressModels.getAdd_address()+", "+addressModels.getAdd_city()+", "+addressModels.getAdd_state()+" - "+addressModels.getAdd_pinCode());
        holder.add_select.setText(addressModels.getAdd_select());

        String PRIMARY = String.valueOf("1");

        if (PRIMARY.equalsIgnoreCase(addressModels.getAdd_select())){
            holder.add_select.setText("Primary");
            holder.add_select.setTextColor(Color.parseColor("#E4BA3D"));
        }else {
            holder.add_select.setText("");
        }

        holder.add_del_btn.setTag(position);
        holder.add_del_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = (int) v.getTag();
                showPopupMenu(v, adapterPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return addressModel.size();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.delete) {
            // Extracting the intent from the menu item
            Intent intent = item.getIntent();
            if (intent != null) {
                int adapterPosition = intent.getIntExtra("deletePosition", -1);
                if (adapterPosition != -1) {
                    showDeleteConfirmationDialog(adapterPosition);
                }
            }
        } else if (id == R.id.select) {
            // Extracting the intent from the menu item
            Intent intent = item.getIntent();
            if (intent != null) {
                int adapterPosition = intent.getIntExtra("selectPosition", -1);
                if (adapterPosition != -1) {
                    AddressSelect(adapterPosition);
                }
            }
        }
        return true;
    }


    private void showPopupMenu(View v, final int position) {
        PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.edit_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.getMenu().getItem(0).setIntent(new Intent().putExtra("deletePosition", position));
        popupMenu.getMenu().getItem(1).setIntent(new Intent().putExtra("selectPosition", position));
        popupMenu.show();
    }
    private void showDeleteConfirmationDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialogTheme);
        View view1 = LayoutInflater.from(mContext).inflate(R.layout.delete_dialog, null);
        builder.setView(view1);

        ((TextView) view1.findViewById(R.id.dialog_title)).setText("DELETE");
        ((TextView) view1.findViewById(R.id.dialog_message)).setText("Confirm delete the address");
        ((Button) view1.findViewById(R.id.dialog_btn)).setText("NO");
        ((Button) view1.findViewById(R.id.dialog_btn2)).setText("YES");
        ((ImageView) view1.findViewById(R.id.dialog_image)).setImageResource(R.drawable.baseline_logout);

        final AlertDialog alertDialog = builder.create();

        view1.findViewById(R.id.dialog_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        view1.findViewById(R.id.dialog_btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ADD_ACTIVE = String.valueOf("2");
                StringRequest request = new StringRequest(Request.Method.POST, "https://vinsoftsolutions.in/jaya-android/address_delete.php",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.equalsIgnoreCase("Deleted Successfully")) {
                                    Delete(position);
                                    ((Address) mContext).updateAddress();
                                    Toast.makeText(mContext, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(mContext, response, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();

                        AddressModel addressModels = addressModel.get(position);

                        params.put("cust_id",addressModels.getCust_id());
                        params.put("add_id",addressModels.getAdd_id());
                        params.put("active",ADD_ACTIVE);

                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(mContext);
                requestQueue.add(request);
                alertDialog.dismiss();
            }
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
    // click delete commend start
    public void Delete(int position){
        addressModel.remove(position);
        notifyItemRemoved(position);
    }
    // click delete commend end

    private void AddressSelect(final int position) {
        final AddressModel addressModels = addressModel.get(position);

        String ADD_SELECT = String.valueOf("1");

        StringRequest request = new StringRequest(Request.Method.POST, "https://vinsoftsolutions.in/jaya-android/address_selected.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equalsIgnoreCase("Selected")) {
                            ((Address) mContext).updateAddress();
                            Toast.makeText(mContext, "Selected", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, response, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(mContext, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                // Corrected variable name to addressModel
                params.put("cust_id", addressModels.getCust_id());
                params.put("add_id", addressModels.getAdd_id());
                params.put("add_select", ADD_SELECT);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(request);
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder {
        TextView add_name,add_mobile,add_address,add_select;
        ImageView add_del_btn;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            add_name = itemView.findViewById(R.id.item_add_name);
            add_mobile = itemView.findViewById(R.id.item_add_mobile);
            add_address = itemView.findViewById(R.id.item_add_address);
            add_select = itemView.findViewById(R.id.item_add_select);

            add_del_btn = itemView.findViewById(R.id.item_add_delete_btn);
        }
    }
}