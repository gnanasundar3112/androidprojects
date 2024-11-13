package com.sundar.i_macbankers.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import com.sundar.i_macbankers.MainActivity;
import com.sundar.i_macbankers.Models.CateModel;
import com.sundar.i_macbankers.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CateAdapter extends RecyclerView.Adapter<CateAdapter.ViewHolder> {
    Context context;
    private List<CateModel> cateModels = new ArrayList<>();
    AlertDialog alertDialog;

    private TextInputEditText CATE_NAME;
    private Spinner ACTIVE;
    private ArrayAdapter<String> active_adapter;
    private MaterialButton SAVE,CANCEL;
    String userName,delete_url, update_url;
    public CateAdapter(Context context, List<CateModel> cateModels) {
        this.context = context;
        this.cateModels = cateModels;

        delete_url = Links.cate_delete;
        update_url = Links.cate_update;
    }


    @NonNull
    @Override
    public CateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cate,parent,false);
        return new CateAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CateAdapter.ViewHolder holder, int position) {

        final CateModel cateModel = cateModels.get(position);
        holder.CATE_NAME.setText(cateModel.getCate_name());
        holder.ACTIVE.setText(cateModel.getActive());

        SharedPreferences sharedPreferences = ((CateMaster) context).getSharedPreferences();
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
                            Delete(cateModel.getCate_id());
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
                    View view = LayoutInflater.from(context).inflate(R.layout.catedialog,
                            (LinearLayout)holder.itemView.findViewById(R.id.cate_dialog));
                    builder.setView(view);

                    alertDialog = builder.create();

                    ((TextView) view.findViewById(R.id.dialog_title)).setText("Update Category");
                    ((MaterialButton) view.findViewById(R.id.dialog_cate_save)).setText("Update");

                    CATE_NAME =view.findViewById(R.id.dia_cate_name);
                    ACTIVE = view.findViewById(R.id.dia_cate_active);

                    SAVE = view.findViewById(R.id.dialog_cate_save);
                    CANCEL = view.findViewById(R.id.dialog_cate_cancel);

                    CATE_NAME.setText(cateModel.getCate_name());

                    active_adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, context.getResources().getStringArray(R.array.active));
                    active_adapter.setDropDownViewResource(R.layout.item_drop_down);
                    ACTIVE.setAdapter(active_adapter);

                    // Set the selected status based on the value in payedModel
                    String statusValue = cateModel.getActive();
                    int position = active_adapter.getPosition(statusValue);
                    ACTIVE.setSelection(position);

                    SAVE.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            update(cateModel.getCate_id());
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
        return cateModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView CATE_NAME,ACTIVE;
        private ImageButton EDIT,DELETE;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            CATE_NAME = itemView.findViewById(R.id.cate_name);
            ACTIVE = itemView.findViewById(R.id.cate_active);
            EDIT = itemView.findViewById(R.id.cate_edit);
            DELETE = itemView.findViewById(R.id.cate_delete);
        }
    }

    public void Delete(String cate_id){

        StringRequest request = new StringRequest(Request.Method.POST, delete_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equalsIgnoreCase("deleted Successfully")){
                            Toast.makeText(context, "Item deleted successfully", Toast.LENGTH_SHORT).show();
                            ((CateMaster) context).fetch();
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
                params.put("cate_id", cate_id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }


    public void update(String cate_id) {
        String cate_name = CATE_NAME.getText().toString().trim();
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
                            ((CateMaster) context).fetch();
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
                params.put("cate_name", cate_name.toUpperCase());
                params.put("active", finalActive);
                params.put("cate_id", cate_id);
                params.put("userName", userName);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }
}