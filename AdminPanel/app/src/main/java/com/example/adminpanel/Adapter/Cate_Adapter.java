package com.example.adminpanel.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.bumptech.glide.Glide;
import com.example.adminpanel.CateMaster;
import com.example.adminpanel.Model.Cate_Model;
import com.example.adminpanel.Model.Prod_Size_Model;
import com.example.adminpanel.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cate_Adapter extends RecyclerView.Adapter<Cate_Adapter.ImageViewHolder>{

    private Context mContext;
    private List<Cate_Model> cateModel = new ArrayList<>();

    // filter from search bar start
    public void setFilteredList(List<Cate_Model>filteredList){
        this.cateModel=filteredList;
        notifyDataSetChanged();
    }
    // filter from search bar End

    public Cate_Adapter(Context mContext, List<Cate_Model> cateModel) {
        this.mContext = mContext;
        this.cateModel = cateModel;
    }



    @NonNull
    @Override
    public Cate_Adapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_category,parent,false);
        return new Cate_Adapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Cate_Adapter.ImageViewHolder holder, @SuppressLint("RecyclerView") int position) {

        final Cate_Model cate_models = cateModel.get(position);

        holder.CATE_NAME.setText(cate_models.getCate_name());
        holder.TAM_NAME.setText(cate_models.getTamil_name());
        holder.SHORT_NAME.setText(cate_models.getShort_name());
        holder.ACTIVE.setText(cate_models.getActive());

        Glide.with(mContext).load(cate_models.getCate_image()).placeholder(R.drawable.prograss_animination).into(holder.CATE_IMAGE);

        holder.CATE_EDIT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext, R.style.LogoutDialogThe);
                View view1 = LayoutInflater.from(mContext).inflate(R.layout.cate_dialog,
                        (LinearLayout)holder.itemView.findViewById(R.id.cate_dialog));

                builder.setView(view1);
                ((TextView) view1.findViewById(R.id.user_title)).setText("Category Master");
                ((MaterialButton) view1.findViewById(R.id.cate_cancel)).setText("Cancel");
                ((MaterialButton) view1.findViewById(R.id.cate_insert)).setText("Update");

                ((TextInputEditText) view1.findViewById(R.id.cate_name)).setText(cate_models.getCate_name());
                ((TextInputEditText) view1.findViewById(R.id.tamil_name)).setText(cate_models.getTamil_name());
                ((TextInputEditText) view1.findViewById(R.id.short_name)).setText(cate_models.getShort_name());
                Glide.with(mContext).load(cate_models.getCate_image()).placeholder(R.drawable.prograss_animination).into(((ImageView) view1.findViewById(R.id.cate_image)));

                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mContext, R.array.Active, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_size);
                ((Spinner) view1.findViewById(R.id.cate_active)).setAdapter(adapter);

                final android.app.AlertDialog alertDialog = builder.create();

                view1.findViewById(R.id.cate_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();

                    }
                });
                view1.findViewById(R.id.cate_insert).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        final ProgressDialog progressDialog1 = new ProgressDialog(mContext);
                        progressDialog1.setMessage("Please wait...");

                        progressDialog1.show();

                        String cate_name = ((TextInputEditText) view1.findViewById(R.id.cate_name)).getText().toString();
                        String tamil_name = ((TextInputEditText) view1.findViewById(R.id.tamil_name)).getText().toString();
                        String short_name = ((TextInputEditText) view1.findViewById(R.id.short_name)).getText().toString();
                        String active = ((Spinner) view1.findViewById(R.id.cate_active)).getSelectedItem().toString();


                        StringRequest request = new StringRequest(Request.Method.POST, "https://caustic-rinses.000webhostapp.com/adminpanel/updatefolder/category_update.php",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        progressDialog1.dismiss();
                                        if (response.equals("Update Successfully")) {
                                            Toast.makeText(mContext, response, Toast.LENGTH_SHORT).show();

                                        } else {
                                            Toast.makeText(mContext, response, Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog1.dismiss();
                                Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        ) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {

                                Map<String, String> params = new HashMap<String, String>();
                                params.put("cate_name", cate_name);
                                params.put("tamil_name", tamil_name);
                                params.put("short_name", short_name);
                                params.put("active", active);
                                params.put("cate_id",cate_models.getCate_id());
                                // params.put("image",encodeImage);

                                return params;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
                        requestQueue.add(request);

                    }
                });

                if (alertDialog.getWindow() != null) {
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                alertDialog.show();
            }
        });
        holder.CATE_DELETE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialogTheme);
                View view1 = LayoutInflater.from(mContext).inflate(R.layout.warning_dialog,
                        (ConstraintLayout)holder.itemView.findViewById(R.id.warning_dialog));

                builder.setView(view1);
                ((TextView) view1.findViewById(R.id.dialog_title)).setText("DELETE");
                ((TextView) view1.findViewById(R.id.dialog_message)).setText("Confirm delete to category");
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


                        StringRequest request = new StringRequest(Request.Method.POST, "https://caustic-rinses.000webhostapp.com/adminpanel/deletephpfolder/category_delete.php",
                                new Response.Listener<String>() {

                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject object = new JSONObject(response);
                                            String check = object.getString("catemaster");
                                            if (check.equals("delete")){
                                                Delete(position);
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

                                params.put("cate_id",cate_models.getCate_id());

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
        return cateModel.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageView CATE_IMAGE;
        TextView CATE_NAME,TAM_NAME,SHORT_NAME,ACTIVE;
        ImageButton CATE_EDIT,CATE_DELETE;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            CATE_IMAGE = itemView.findViewById(R.id.cate_image);
            CATE_NAME = itemView.findViewById(R.id.cate_name);
            TAM_NAME = itemView.findViewById(R.id.tamil_name);
            SHORT_NAME = itemView.findViewById(R.id.short_name);
            ACTIVE = itemView.findViewById(R.id.Active);
            CATE_EDIT = itemView.findViewById(R.id.cate_edit);
            CATE_DELETE = itemView.findViewById(R.id.cate_delete);
        }
    }
    // click delete commend start
    public void Delete(int item){
        cateModel.remove(item);
        notifyItemRemoved(item);
    }
    // click delete commend end
}
