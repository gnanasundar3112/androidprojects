package com.example.gifts.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gifts.IncomOutActivity;
import com.example.gifts.Models.IncomeModel;
import com.example.gifts.R;
import com.example.gifts.SqlLight.SqlDataBaseHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IncomeAdapter extends RecyclerView.Adapter<IncomeAdapter.ImageViewHolder>{

    public TextInputEditText function,party_name,village,income,remarks,payable,other_amt;
    private Context mContext;
    private List<IncomeModel> IncomeModel = new ArrayList<>();
    public IncomeAdapter(Context mContext, List<IncomeModel> IncomeModel) {
        this.mContext = mContext;
        this.IncomeModel = IncomeModel;
    }
    public void setFilteredList(List<IncomeModel>filteredList){
        this.IncomeModel=filteredList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public IncomeAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_detail,parent,false);
        return new IncomeAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IncomeAdapter.ImageViewHolder holder, int position) {
        final IncomeModel IncomeModels = IncomeModel.get(position);

        holder.fun_id.setText(IncomeModels.getFun_id().substring(IncomeModels.getFun_id().length() - 4));
        holder.fun_name.setText(IncomeModels.getFun_name());
        holder.party_name.setText(IncomeModels.getParty_name());
        holder.area_name.setText(IncomeModels.getArea_name());
        holder.in_amt.setText("₹ "+IncomeModels.getIn_amt());
        holder.remarks.setText(IncomeModels.getRemarks());
        holder.out_amt.setText("₹ "+IncomeModels.getOut_amt());
        holder.other_amt.setText("₹ "+IncomeModels.getOther_amt());

        holder.edit_btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingInflatedId")
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext, R.style.AlertDialogTheme);
                View view1 = LayoutInflater.from(mContext).inflate(R.layout.incomadddialog,
                        (LinearLayout)holder.itemView.findViewById(R.id.income_dialog));

                builder.setView(view1);
                ((TextView) view1.findViewById(R.id.dialog_title)).setText("Edit Party Detail");
                ((MaterialButton) view1.findViewById(R.id.dialog_save)).setText("Update");

                ((TextView) view1.findViewById(R.id.dia_fun_id)).setText(IncomeModels.getFun_id());
                ((TextInputEditText) view1.findViewById(R.id.dia_fun_name)).setText(IncomeModels.getFun_name());
                ((TextInputEditText) view1.findViewById(R.id.dia_ac_name)).setText(IncomeModels.getParty_name());
                ((TextInputEditText) view1.findViewById(R.id.dia_area_name)).setText(IncomeModels.getArea_name());
                ((TextInputEditText) view1.findViewById(R.id.dia_in_amt)).setText(""+IncomeModels.getIn_amt());
                ((TextInputEditText) view1.findViewById(R.id.dia_remarks)).setText(IncomeModels.getRemarks());
                ((TextInputEditText) view1.findViewById(R.id.dia_out_amt)).setText(""+IncomeModels.getOut_amt());
                ((TextInputEditText) view1.findViewById(R.id.dia_other_amt)).setText(""+IncomeModels.getOther_amt());

                final android.app.AlertDialog alertDialog = builder.create();


                view1.findViewById(R.id.dialog_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view ) {
                        SqlDataBaseHelper dbHelper = new SqlDataBaseHelper(mContext);
                        SQLiteDatabase db = dbHelper.getWritableDatabase();

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        String date = dateFormat.format(new Date());
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                        String time = timeFormat.format(new Date());

                        String FUN_ID = ((TextView) view1.findViewById(R.id.dia_fun_id)).getText().toString();
                        String FUNCTION = ((TextInputEditText) view1.findViewById(R.id.dia_fun_name)).getText().toString();
                        String PARTY_NAME = ((TextInputEditText) view1.findViewById(R.id.dia_ac_name)).getText().toString();
                        String VILLAGE = ((TextInputEditText) view1.findViewById(R.id.dia_area_name)).getText().toString();
                        String INCOME = ((TextInputEditText) view1.findViewById(R.id.dia_in_amt)).getText().toString();
                        String REMARKS = ((TextInputEditText) view1.findViewById(R.id.dia_remarks)).getText().toString();
                        String PAYABLE = ((TextInputEditText) view1.findViewById(R.id.dia_out_amt)).getText().toString();
                        String OTHER_AMT = ((TextInputEditText) view1.findViewById(R.id.dia_other_amt)).getText().toString();


                        if (INCOME.isEmpty()) {
                            INCOME = "0.00";
                        }
                        if (PAYABLE.isEmpty()) {
                            PAYABLE = "0.00";
                        }
                        if (OTHER_AMT.isEmpty()) {
                            OTHER_AMT = "0.00";
                        }
                        String finalINCOME = INCOME;
                        String finalPAYABLE = PAYABLE;
                        String finalOTHER_AMT = OTHER_AMT;

                        ContentValues values = new ContentValues();
                        values.put("fun_name", FUNCTION);
                        values.put("party_name", PARTY_NAME);
                        values.put("area_name", VILLAGE);
                        values.put("in_amt", finalINCOME);
                        values.put("remarks", REMARKS);
                        values.put("out_amt", finalPAYABLE);
                        values.put("other_amt", finalOTHER_AMT);
                        values.put("modi_date", date);
                        values.put("modi_time", time);

                        long tableRow = db.update("accounts", values, "fun_id = ?", new String[] {FUN_ID});

                        if(tableRow != -1) {
                            Toast.makeText(mContext, "Updated Successfully", Toast.LENGTH_SHORT).show();
                            ((IncomOutActivity) mContext).fetch();
                            alertDialog.dismiss();
                        }else {
                            Toast.makeText(mContext, "Updated Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                view1.findViewById(R.id.dialog_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();

                    }
                });

                if (alertDialog.getWindow() != null) {
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                alertDialog.show();
            }
        });


        holder.del_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialogTheme);
                View view1 = LayoutInflater.from(mContext).inflate(R.layout.warning_dialog,
                        (ConstraintLayout)holder.itemView.findViewById(R.id.warning_dialog));

                builder.setView(view1);
                ((TextView) view1.findViewById(R.id.dialog_title)).setText("DELETE");
                ((TextView) view1.findViewById(R.id.dialog_message)).setText("Confirm delete to Part Name");
                ((Button) view1.findViewById(R.id.dialog_btn)).setText("NO");
                ((Button) view1.findViewById(R.id.dialog_btn2)).setText("YES");
                ((ImageView) view1.findViewById(R.id.dialog_image)).setImageResource(R.drawable.baseline_delete);

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
                        SqlDataBaseHelper dbHelper = new SqlDataBaseHelper(mContext);
                        SQLiteDatabase db = dbHelper.getWritableDatabase();

                        long deletedRows = db.delete("accounts", "fun_id = ?", new String[] {IncomeModels.getFun_id()});

                        db.close();

                        if (deletedRows > 0) {
                            Toast.makeText(mContext, "Item deleted successfully", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                            ((IncomOutActivity) mContext).fetch();
                        } else {
                            Toast.makeText(mContext, "Failed to delete item", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        }

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
        return IncomeModel.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        TextView fun_id,fun_name,party_name,area_name,in_amt,remarks,out_amt,other_amt;
        ImageView edit_btn,del_btn;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            fun_id = itemView.findViewById(R.id.fun_id);
            fun_name = itemView.findViewById(R.id.fun_Name);
            party_name = itemView.findViewById(R.id.party_name);
            area_name = itemView.findViewById(R.id.area_name);
            in_amt = itemView.findViewById(R.id.in_amount);
            remarks = itemView.findViewById(R.id.remarks);
            out_amt = itemView.findViewById(R.id.out_amount);
            other_amt = itemView.findViewById(R.id.other_amount);
            edit_btn = itemView.findViewById(R.id.edit);
            del_btn = itemView.findViewById(R.id.delete);
        }
    }
}
