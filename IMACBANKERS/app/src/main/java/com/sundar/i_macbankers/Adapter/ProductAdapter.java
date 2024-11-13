package com.sundar.i_macbankers.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.sundar.i_macbankers.CateMaster;
import com.sundar.i_macbankers.Links;
import com.sundar.i_macbankers.Models.ProductModel;
import com.sundar.i_macbankers.ProductMaster;
import com.sundar.i_macbankers.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private Context context;
    private List<ProductModel> productModels = new ArrayList<>();
    AlertDialog alertDialog;
    private TextInputEditText PRODUCT_NAME;
    private Spinner ACTIVE;
    private ArrayAdapter<String> active_adapter;
    private MaterialButton SAVE,CANCEL;
    int DIALOG_BG,DIALOG_TEXT_COLOR;
    String userName,delete_url,update_url;

    public ProductAdapter(Context context, List<ProductModel> productModels) {
        this.context = context;
        this.productModels = productModels;

        delete_url = Links.product_delete;
        update_url = Links.product_update;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_prod,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ProductModel productModel = productModels.get(position);

        holder.PRODUCT_NAME.setText(productModel.getProd_name());
        holder.ACTIVE.setText(productModel.getActive());

        SharedPreferences sharedPreferences = ((ProductMaster) context).getSharedPreferences();
        String user_name = sharedPreferences.getString("user_name", "");
        userName = user_name;

        holder.DELETE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userName.equals("admin")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                    View view = LayoutInflater.from(context).inflate(R.layout.warning_dialog,
                            (ConstraintLayout) holder.itemView.findViewById(R.id.warning_dialog));

                    builder.setView(view);
                    alertDialog = builder.create();
                    ((TextView) view.findViewById(R.id.dialog_title)).setText("DELETE");
                    ((TextView) view.findViewById(R.id.dialog_message)).setText("Are you sure you want to Delete");
                    ((Button) view.findViewById(R.id.dialog_cancel)).setText("NO");
                    ((Button) view.findViewById(R.id.dialog_submit)).setText("YES");


                    view.findViewById(R.id.dialog_cancel).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }
                    });

                    view.findViewById(R.id.dialog_submit).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Delete(productModel.getProd_id());
                        }
                    });

                    if (alertDialog.getWindow() != null) {
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                    }
                    alertDialog.show();
                }else {
                    Toast.makeText(context, "Admin only delete", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.EDIT.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingInflatedId")
            @Override
            public void onClick(View v) {
                if (userName.equals("admin")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                    View view = LayoutInflater.from(context).inflate(R.layout.proddialog,
                            (LinearLayout) holder.itemView.findViewById(R.id.prod_dialog));
                    builder.setView(view);

                    alertDialog = builder.create();

                    ((TextView) view.findViewById(R.id.dialog_title)).setText("Update Product");
                    ((MaterialButton) view.findViewById(R.id.dialog_prod_save)).setText("Update");

                    PRODUCT_NAME = view.findViewById(R.id.dia_prod_name);
                    ACTIVE = view.findViewById(R.id.dia_prod_active);

                    PRODUCT_NAME.setText(productModel.getProd_name());

                    active_adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, context.getResources().getStringArray(R.array.active));
                    active_adapter.setDropDownViewResource(R.layout.item_drop_down);
                    ACTIVE.setAdapter(active_adapter);

                    // Set the selected status based on the value in payedModel
                    String statusValue = productModel.getActive();
                    int position = active_adapter.getPosition(statusValue);
                    ACTIVE.setSelection(position);

                    SAVE = view.findViewById(R.id.dialog_prod_save);
                    CANCEL = view.findViewById(R.id.dialog_prod_cancel);

                    SAVE.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            update(productModel.getProd_id());
                        }
                    });

                    CANCEL.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View V) {
                            alertDialog.dismiss();
                        }
                    });

                    if (alertDialog.getWindow() != null) {
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                    }
                    alertDialog.show();
                }else {
                    Toast.makeText(context, "Admin only Edit", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView PRODUCT_NAME,ACTIVE;
        private ImageButton EDIT,DELETE;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            PRODUCT_NAME = itemView.findViewById(R.id.prod_name);
            ACTIVE = itemView.findViewById(R.id.prod_active);
            EDIT = itemView.findViewById(R.id.prod_edit);
            DELETE = itemView.findViewById(R.id.prod_delete);
        }
    }

    public void Delete(String Prod_id){
        StringRequest request = new StringRequest(Request.Method.POST, delete_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equalsIgnoreCase("deleted Successfully")){
                            Toast.makeText(context, "Item deleted successfully", Toast.LENGTH_SHORT).show();
                            ((ProductMaster) context).fetch();
                            alertDialog.dismiss();
                        } else {
                            Toast.makeText(context, "Item deleted Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(context, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("prod_id", Prod_id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public void update(String Prod_id) {

        String prod_name = PRODUCT_NAME.getText().toString().trim();
        String active = ACTIVE.getSelectedItem().toString();

        if (active.equals("ENABLE")) {
            active = "1";
        }else {
            active = "2";
        }

        String finalActive = active;

        StringRequest request = new StringRequest(Request.Method.POST, update_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equalsIgnoreCase("Updated Successfully")){
                            Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show();
                            ((ProductMaster) context).fetch();
                            alertDialog.dismiss();
                        } else {
                            Toast.makeText(context, "Updated Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(context, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("prod_id", Prod_id);
                params.put("prod_name", prod_name.toUpperCase());
                params.put("active", finalActive);
                params.put("userName", userName);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

}
