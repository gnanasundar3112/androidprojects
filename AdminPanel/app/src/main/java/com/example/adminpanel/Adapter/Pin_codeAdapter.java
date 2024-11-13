package com.example.adminpanel.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import com.example.adminpanel.Model.Pin_codeModel;
import com.example.adminpanel.Model.Prod_Model;
import com.example.adminpanel.Pin_codeMaster;
import com.example.adminpanel.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pin_codeAdapter extends RecyclerView.Adapter<Pin_codeAdapter.ImageViewHolder>{
    private Context mContext;
    private List<Pin_codeModel> pin_codeModel = new ArrayList<>();

    public Pin_codeAdapter(Context mContext, List<Pin_codeModel> pin_codeModel) {
        this.mContext = mContext;
        this.pin_codeModel = pin_codeModel;
    }

    @NonNull
    @Override
    public Pin_codeAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_pincode,parent,false);
        return new Pin_codeAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Pin_codeAdapter.ImageViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final Pin_codeModel pin_codeModels = pin_codeModel.get(position);

        holder.PINCODE_NAME.setText(pin_codeModels.getPin_code_name());
        holder.PINCODE_ACTIVE.setText(pin_codeModels.getPin_code_active());


        holder.PINCODE_EDIT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext, R.style.LogoutDialogThe);
                View view1 = LayoutInflater.from(mContext).inflate(R.layout.pincode_dialog,
                        (LinearLayout)holder.itemView.findViewById(R.id.pincode_dialog));

                builder.setView(view1);
                ((TextView) view1.findViewById(R.id.user_title)).setText("Pincode Master");
                ((MaterialButton) view1.findViewById(R.id.cate_cancel)).setText("Cancel");
                ((MaterialButton) view1.findViewById(R.id.cate_insert)).setText("Update");

                ((TextInputEditText) view1.findViewById(R.id.pin_name)).setText(pin_codeModels.getPin_code_name());

                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mContext, R.array.Active, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_size);
                ((Spinner)view1.findViewById(R.id.pin_active)).setAdapter(adapter);

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
                        String pincode_error = ((TextInputEditText) view1.findViewById(R.id.pin_name)).getText().toString();

                        if (pincode_error.length() < 6) {
                            ((TextInputEditText) view1.findViewById(R.id.pin_name)).setError("Pincode length is short");
                        }else {
                            final ProgressDialog progressDialog1 = new ProgressDialog(mContext);
                            progressDialog1.setMessage("Please wait...");

                            progressDialog1.show();

                            String pincode = ((TextInputEditText) view1.findViewById(R.id.pin_name)).getText().toString();
                            String active = ((Spinner) view1.findViewById(R.id.pin_active)).getSelectedItem().toString().trim();


                            StringRequest request = new StringRequest(Request.Method.POST, "https://caustic-rinses.000webhostapp.com/adminpanel/updatefolder/pincode_update.php",
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
                                    params.put("pincode", pincode);
                                    params.put("active", active);
                                    return params;
                                }
                            };
                            RequestQueue requestQueue = Volley.newRequestQueue(mContext);
                            requestQueue.add(request);
                        }
                    }
                });

                if (alertDialog.getWindow() != null) {
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                alertDialog.show();
            }
        });


        holder.PINCODE_DELETE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialogTheme);
                View view1 = LayoutInflater.from(mContext).inflate(R.layout.warning_dialog,
                        (ConstraintLayout)holder.itemView.findViewById(R.id.warning_dialog));

                builder.setView(view1);
                ((TextView) view1.findViewById(R.id.dialog_title)).setText("DELETE");
                ((TextView) view1.findViewById(R.id.dialog_message)).setText("Confirm delete to pincode");
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


                        StringRequest request = new StringRequest(Request.Method.POST, "https://caustic-rinses.000webhostapp.com/adminpanel/deletephpfolder/pincode_delete.php",
                                new Response.Listener<String>() {

                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject object = new JSONObject(response);
                                            String check = object.getString("pincode_master");
                                            if (check.equals("delete")){
                                                ProdDelete(position);
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

                                params.put("pincode",pin_codeModels.getPin_code_name());

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
        return pin_codeModel.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        TextView PINCODE_NAME,PINCODE_ACTIVE;
        ImageButton PINCODE_EDIT,PINCODE_DELETE;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            PINCODE_NAME = itemView.findViewById(R.id.pincode_name);
            PINCODE_ACTIVE  = itemView.findViewById(R.id.pincode_active);

            PINCODE_EDIT = itemView.findViewById(R.id.pincode_edit);
            PINCODE_DELETE = itemView.findViewById(R.id.pincode_delete);
        }
    }
    // click delete commend start
    public void ProdDelete(int item){
        pin_codeModel.remove(item);
        notifyItemRemoved(item);
    }
    // click delete commend end
}
