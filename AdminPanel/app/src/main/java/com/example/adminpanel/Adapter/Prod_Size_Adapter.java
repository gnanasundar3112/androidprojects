package com.example.adminpanel.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.adminpanel.Model.Cate_Model;
import com.example.adminpanel.Model.Prod_Size_Model;
import com.example.adminpanel.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Prod_Size_Adapter extends RecyclerView.Adapter<Prod_Size_Adapter.ImageViewHolder> {
    private Context mContext;
    private List<Prod_Size_Model> prod_size_model = new ArrayList<>();

    public Prod_Size_Adapter(Context mContext, List<Prod_Size_Model> prod_size_model) {
        this.mContext = mContext;
        this.prod_size_model = prod_size_model;
    }
    // filter from search bar start
    public void setFilteredList(List<Prod_Size_Model>filteredList){
        this.prod_size_model=filteredList;
        notifyDataSetChanged();
    }
    // filter from search bar End

    @NonNull
    @Override
    public Prod_Size_Adapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_productsize,parent,false);
        return new Prod_Size_Adapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Prod_Size_Adapter.ImageViewHolder holder, int position) {
        final Prod_Size_Model prod_size_models = prod_size_model.get(position);


        holder.EFF_DATE.setText(prod_size_models.getEff_date());
        holder.PROD_NAME.setText(prod_size_models.getSize_prod_name());
        holder.SIZE_NAME.setText(prod_size_models.getProd_size());

        holder.SIZE_DELETE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialogTheme);
                View view1 = LayoutInflater.from(mContext).inflate(R.layout.warning_dialog,
                        (ConstraintLayout)holder.itemView.findViewById(R.id.warning_dialog));

                builder.setView(view1);
                ((TextView) view1.findViewById(R.id.dialog_title)).setText("DELETE");
                ((TextView) view1.findViewById(R.id.dialog_message)).setText("Confirm delete to product size");
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


                        StringRequest request = new StringRequest(Request.Method.POST, "https://caustic-rinses.000webhostapp.com/adminpanel/deletephpfolder/sizemaster_delete.php",
                                new Response.Listener<String>() {

                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject object = new JSONObject(response);
                                            String check = object.getString("productsize");
                                            if (check.equals("delete")){
                                                ProductSizeDelete(position);
                                                Toast.makeText(mContext, "Delete Successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
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

                                params.put("eff_date",prod_size_models.getEff_date());
                                params.put("prod_id",prod_size_models.getSize_prod_id());
                                params.put("size_id",prod_size_models.getProd_size_id());

                                return params;
                            }
                        };

                        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
                        requestQueue.add(request);
                        alertDialog.dismiss();
                    }

                });

                if (alertDialog.getWindow() !=null){
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                alertDialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return prod_size_model.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        TextView EFF_DATE,PROD_NAME,SIZE_NAME;
        ImageButton SIZE_DELETE;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            EFF_DATE = itemView.findViewById(R.id.size_eff_date);
            PROD_NAME = itemView.findViewById(R.id.size_prod_name);
            SIZE_NAME = itemView.findViewById(R.id.size_name);
            SIZE_DELETE = itemView.findViewById(R.id.size_delete);
        }
    }
    // click delete commend start
    public void ProductSizeDelete(int item){
        prod_size_model.remove(item);
        notifyItemRemoved(item);
    }
    // click delete commend end
}
