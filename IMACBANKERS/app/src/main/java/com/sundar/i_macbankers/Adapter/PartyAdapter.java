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
import android.widget.AutoCompleteTextView;
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
import com.sundar.i_macbankers.Models.PartyModel;
import com.sundar.i_macbankers.PartyMaster;
import com.sundar.i_macbankers.ProductMaster;
import com.sundar.i_macbankers.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PartyAdapter extends RecyclerView.Adapter<PartyAdapter.ViewHolder> {
    Context context;
    private List<PartyModel> partyModel = new ArrayList<>();
    AlertDialog alertDialog,editDialog;
    private TextInputEditText AC_NAME,ADDRESS,PLACE,AADHAR_NO,MOBILE;
    AutoCompleteTextView STATE;
    private Spinner ACTIVE;
    ArrayList<String> active_list = new ArrayList<>();
    ArrayAdapter<String> active_adapter;
    private MaterialButton SAVE,CANCEL;
    String userName,update_url,delete_url;

    public PartyAdapter(Context context, List<PartyModel> partyModel) {
        this.context = context;
        this.partyModel = partyModel;
        update_url = Links.party_update;
        delete_url = Links.party_delete;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_party,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final PartyModel PartyModels = partyModel.get(position);
        holder.AC_ID.setText(PartyModels.getAC_ID());
        holder.AC_NAME.setText(PartyModels.getAC_NAME());
        holder.ADDRESS.setText(PartyModels.getADDRESS());
        holder.PLACE.setText(PartyModels.getPLACE());
        holder.STATE.setText(PartyModels.getSTATE());
        holder.MOBILE.setText(PartyModels.getMOBILE());
        holder.AADHAR_NO.setText(PartyModels.getAADHAR_NO());
        holder.ACTIVE.setText(PartyModels.getACTIVE());

        SharedPreferences sharedPreferences = ((PartyMaster) context).getSharedPreferences();
        String user_name = sharedPreferences.getString("user_name", "");
        userName = user_name;

        holder.DELETE_BTN.setOnClickListener(new View.OnClickListener() {
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
                            Delete(PartyModels.getAC_ID());
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
        holder.EDIT_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userName.equals("admin")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                    View view = LayoutInflater.from(context).inflate(R.layout.partydialog,
                            (LinearLayout) holder.itemView.findViewById(R.id.party_dialog));
                    builder.setView(view);

                    alertDialog = builder.create();

                    ((TextView) view.findViewById(R.id.dialog_title)).setText("Update Party Detail");
                    ((MaterialButton) view.findViewById(R.id.dialog_save)).setText("Update");

                    AC_NAME = view.findViewById(R.id.dia_ac_name);
                    ADDRESS = view.findViewById(R.id.dia_address);
                    PLACE = view.findViewById(R.id.dia_place);
                    STATE = view.findViewById(R.id.dia_state);
                    MOBILE = view.findViewById(R.id.dia_mobile);
                    AADHAR_NO = view.findViewById(R.id.dia_aadhar_no);
                    ACTIVE = view.findViewById(R.id.dia_active);

                    AC_NAME.setText(PartyModels.getAC_NAME());
                    ADDRESS.setText(PartyModels.getADDRESS());
                    PLACE.setText(PartyModels.getPLACE());
                    STATE.setText(PartyModels.getSTATE());
                    AADHAR_NO.setText(PartyModels.getAADHAR_NO());
                    MOBILE.setText(PartyModels.getMOBILE());


                    // Assuming this code is inside an activity or a context
                    String[] statesArray = context.getResources().getStringArray(R.array.states_array);
                    for (int i = 0; i < statesArray.length; i++) {
                        statesArray[i] = statesArray[i].toUpperCase();
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, statesArray);
                    STATE.setAdapter(adapter);
                    STATE.setOnItemClickListener((parent, view1, position, id) -> {
                        String selectedItem = (String) parent.getItemAtPosition(position);
                        Toast.makeText(context, "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
                    });

                    active_adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, context.getResources().getStringArray(R.array.active));
                    active_adapter.setDropDownViewResource(R.layout.item_drop_down);
                    ACTIVE.setAdapter(active_adapter);

                    // Set the selected status based on the value in payedModel
                    String statusValue = PartyModels.getACTIVE();
                    int position = active_adapter.getPosition(statusValue);
                    ACTIVE.setSelection(position);


                    SAVE = view.findViewById(R.id.dialog_save);
                    CANCEL = view.findViewById(R.id.dialog_cancel);

                    SAVE.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            update(PartyModels.getAC_ID());
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
        return partyModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView AC_ID,AC_NAME,SHORT_NAME,ADDRESS,PLACE,STATE,MOBILE,AADHAR_NO,ACTIVE;
        ImageButton EDIT_BTN,DELETE_BTN;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            AC_ID = itemView.findViewById(R.id.ac_id);
            AC_NAME = itemView.findViewById(R.id.ac_party);
            ADDRESS = itemView.findViewById(R.id.address);
            PLACE = itemView.findViewById(R.id.place);
            STATE = itemView.findViewById(R.id.state_name);
            MOBILE = itemView.findViewById(R.id.phone);
            AADHAR_NO = itemView.findViewById(R.id.adhar_no);
            ACTIVE = itemView.findViewById(R.id.active);
            EDIT_BTN = itemView.findViewById(R.id.party_edit);
            DELETE_BTN = itemView.findViewById(R.id.party_delete);
        }
    }

    public void Delete(String ac_id){

        StringRequest request = new StringRequest(Request.Method.POST, delete_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equalsIgnoreCase("deleted Successfully")){
                            Toast.makeText(context, "Item deleted successfully", Toast.LENGTH_SHORT).show();
                            ((PartyMaster) context).fetch();
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
                params.put("ac_id", ac_id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);

    }

    public void update(String ac_id) {

        String Ac_name = AC_NAME.getText().toString().trim();
        String Address = ADDRESS.getText().toString().trim();
        String Place = PLACE.getText().toString().trim();
        String State = STATE.getText().toString().trim();
        String Aadhar_no = AADHAR_NO.getText().toString().trim();
        String Mobile = MOBILE.getText().toString().trim();
        String Active = ACTIVE.getSelectedItem().toString();

        if (Active.equals("ENABLE")) {
            Active = "1";
        }else {
            Active = "2";
        }

        String finalActive = Active;

        StringRequest request = new StringRequest(Request.Method.POST, update_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                        AC_NAME.setText(response);
                        if (response.equalsIgnoreCase("Updated Successfully")){
                            Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show();
                            ((PartyMaster) context).fetch();
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
                params.put("ac_id",ac_id);
                params.put("ac_name",Ac_name.toUpperCase());
                params.put("address",Address.toUpperCase());
                params.put("place",Place.toUpperCase());
                params.put("state",State.toUpperCase());
                params.put("mobile",Mobile);
                params.put("aadhar_no",Aadhar_no);
                params.put("active",finalActive);
                params.put("crea_user",userName);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

}
