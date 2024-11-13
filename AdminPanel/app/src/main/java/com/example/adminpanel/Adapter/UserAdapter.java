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
import com.example.adminpanel.DeliveryUser;
import com.example.adminpanel.Model.Cate_Model;
import com.example.adminpanel.Model.UserModel;
import com.example.adminpanel.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ImageViewHolder>{
    private Context mContext;
    private List<UserModel> userModel = new ArrayList<>();

    public UserAdapter(Context mContext, List<UserModel> userModel) {
        this.mContext = mContext;
        this.userModel = userModel;
    }

    @NonNull
    @Override
    public UserAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_deli_user,parent,false);
        return new UserAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ImageViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final UserModel userModels = userModel.get(position);

        holder.USER_NAME.setText(userModels.getUser_Name());
        holder.USER_PHONE.setText(userModels.getUser_mobile());
        holder.USER_PASSWORD.setText(userModels.getUser_password());
        holder.USER_AADHAR.setText(userModels.getUser_aadhar());
        holder.USER_ADDRESS.setText(userModels.getUser_address());
        holder.USER_ACTIVE.setText(userModels.getActive());

        Glide.with(mContext).load(userModels.getUser_image()).placeholder(R.drawable.prograss_animination).into(holder.USER_IMAGE);
        Glide.with(mContext).load(userModels.getAdhar_image()).placeholder(R.drawable.prograss_animination).into(holder.AADHAR_IMAGE);
        Glide.with(mContext).load(userModels.getVeh_image()).placeholder(R.drawable.prograss_animination).into(holder.VEHICLE_IMAGE);

        holder.USER_EDIT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext, R.style.LogoutDialogThe);
                View view1 = LayoutInflater.from(mContext).inflate(R.layout.user_dialog,
                        (LinearLayout) holder.itemView.findViewById(R.id.user_dialog));

                builder.setView(view1);
                ((TextView) view1.findViewById(R.id.user_title)).setText("Delivery User Detail");
                ((MaterialButton) view1.findViewById(R.id.user_cancel_btn)).setText("Cancel");
                ((MaterialButton) view1.findViewById(R.id.user_insert_btn)).setText("Update");

                ((TextInputEditText) view1.findViewById(R.id.user_name)).setText(userModels.getUser_Name());
                ((TextInputEditText) view1.findViewById(R.id.user_phone)).setText(userModels.getUser_mobile());
                ((TextInputEditText) view1.findViewById(R.id.user_password)).setText(userModels.getUser_password());
                ((TextInputEditText) view1.findViewById(R.id.user_aadhar_no)).setText(userModels.getUser_aadhar());
                ((TextInputEditText) view1.findViewById(R.id.user_address)).setText(userModels.getUser_address());

                Glide.with(mContext).load(userModels.getUser_image()).placeholder(R.drawable.prograss_animination).into(((ImageView) view1.findViewById(R.id.user_image)));
                Glide.with(mContext).load(userModels.getAdhar_image()).placeholder(R.drawable.prograss_animination).into(((ImageView) view1.findViewById(R.id.adhar_image)));
                Glide.with(mContext).load(userModels.getVeh_image()).placeholder(R.drawable.prograss_animination).into(((ImageView) view1.findViewById(R.id.bick_image)));

                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mContext, R.array.Active, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_size);
                ((Spinner) view1.findViewById(R.id.user_active)).setAdapter(adapter);

                final android.app.AlertDialog alertDialog = builder.create();


                view1.findViewById(R.id.user_cancel_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();

                    }
                });
                view1.findViewById(R.id.user_insert_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String user_name = ((TextInputEditText) view1.findViewById(R.id.user_name)).getText().toString();
                        String user_phone = ((TextInputEditText) view1.findViewById(R.id.user_phone)).getText().toString();
                        String user_password = ((TextInputEditText) view1.findViewById(R.id.user_password)).getText().toString();
                        String user_aadhar = ((TextInputEditText) view1.findViewById(R.id.user_aadhar_no)).getText().toString();
                        String user_address = ((TextInputEditText) view1.findViewById(R.id.user_address)).getText().toString();
                        String active = ((Spinner) view1.findViewById(R.id.user_active)).getSelectedItem().toString();


                        final ProgressDialog progressDialog1 = new ProgressDialog(mContext);
                        progressDialog1.setMessage("Please wait...");

                        progressDialog1.show();

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://caustic-rinses.000webhostapp.com/adminpanel/updatefolder/user_update.php",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        progressDialog1.dismiss();
                                        if (response.equals("Update Successfully")) {
                                            Toast.makeText(mContext,response,Toast.LENGTH_SHORT).show();

                                        } else {
                                            Toast.makeText(mContext,response,Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
                                progressDialog1.dismiss();
                            }
                        } )
                        {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String,String> params = new HashMap<>();
                                params.put("user_name",user_name);
                                params.put("user_phone",user_phone);
                                params.put("user_password",user_password);
                                params.put("aadhar_no",user_aadhar);
                                params.put("user_address",user_address);
                                // params.put("user_image",UserImageString(user_bitmap));
                                // params.put("aadhar_image",AAdharImageString(aadhar_bitmap));
                                // params.put("bike_image",BikeImageString(bike_bitmap));
                                params.put("active",active);
                                params.put("user_id",userModels.getUser_Id());
                                return params;
                            }
                        };

                        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
                        requestQueue.add(stringRequest);
                    }
                });

                if (alertDialog.getWindow() != null) {
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                alertDialog.show();
            }
        });
        holder.USER_DELETE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialogTheme);
                View view1 = LayoutInflater.from(mContext).inflate(R.layout.warning_dialog,
                        (ConstraintLayout)holder.itemView.findViewById(R.id.warning_dialog));

                builder.setView(view1);
                ((TextView) view1.findViewById(R.id.dialog_title)).setText("DELETE");
                ((TextView) view1.findViewById(R.id.dialog_message)).setText("Confirm delete to user");
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


                        StringRequest request = new StringRequest(Request.Method.POST, "https://caustic-rinses.000webhostapp.com/adminpanel/deletephpfolder/users_delete.php",
                                new Response.Listener<String>() {

                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject object = new JSONObject(response);
                                            String check = object.getString("user_register");
                                            if (check.equals("delete")){
                                                UserDelete(position);
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

                                params.put("user_id",userModels.getUser_Id());

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
        return userModel.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageView USER_IMAGE,AADHAR_IMAGE,VEHICLE_IMAGE;
        TextView  USER_NAME,USER_PHONE,USER_PASSWORD,USER_AADHAR,USER_ADDRESS,USER_ACTIVE;
        ImageButton USER_EDIT,USER_DELETE;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            USER_IMAGE = itemView.findViewById(R.id.user_image);
            AADHAR_IMAGE = itemView.findViewById(R.id.aadhar_image);
            VEHICLE_IMAGE = itemView.findViewById(R.id.vehicle_image);

            USER_NAME = itemView.findViewById(R.id.user_name);
            USER_PHONE = itemView.findViewById(R.id.user_phone);
            USER_PASSWORD = itemView.findViewById(R.id.user_password);
            USER_AADHAR = itemView.findViewById(R.id.user_aadhar);
            USER_ADDRESS = itemView.findViewById(R.id.user_address);
            USER_ACTIVE = itemView.findViewById(R.id.user_active);

            USER_EDIT = itemView.findViewById(R.id.user_edit);
            USER_DELETE = itemView.findViewById(R.id.user_delete);
        }
    }
    // click delete commend start
    public void UserDelete(int item){
        userModel.remove(item);
        notifyItemRemoved(item);
    }
    // click delete commend end
}
