package com.sundar.i_macbankers.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import com.sundar.i_macbankers.Models.ReceiptModel;
import com.sundar.i_macbankers.R;
import com.sundar.i_macbankers.Receipt;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ReceiptAdapter extends RecyclerView.Adapter<ReceiptAdapter.ImageViewHolder> {
    private Context context;
    private List<ReceiptModel> receiptModels = new ArrayList<>();
    AlertDialog alertDialog;
    Calendar CALENDAR;

    private TextInputEditText RECEIPT_DATE,INTEREST_AMT;
    private MaterialButton SAVE,CANCEL;

    String userName,delete_url,update_url;

    public ReceiptAdapter(Context context, List<ReceiptModel> receiptModels) {
        this.context = context;
        this.receiptModels = receiptModels;

        delete_url = Links.receipt_delete;
        update_url = Links.receipt_update;
    }

    @NonNull
    @Override
    public ReceiptAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_receipt,parent,false);
        return new ReceiptAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiptAdapter.ImageViewHolder holder, int position) {
        final ReceiptModel receiptModel = receiptModels.get(position);
        holder.DATE.setText(receiptModel.getDate());
        holder.INTEREST_AMT.setText(""+receiptModel.getInterest_amt());

        SharedPreferences sharedPreferences = ((Receipt) context).getSharedPreferences();
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
                            Delete(receiptModel.getCust_id(), receiptModel.getLoan_no(), receiptModel.getSerial());
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
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                CALENDAR.set(Calendar.YEAR,year);
                CALENDAR.set(Calendar.MONTH,month);
                CALENDAR.set(Calendar.DAY_OF_MONTH,day);
                getFromDate();
            }
        };
        holder.EDIT.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingInflatedId")
            @Override
            public void onClick(View v) {
                if (userName.equals("admin")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                    View view = LayoutInflater.from(context).inflate(R.layout.receipt_dialog,
                            (LinearLayout) holder.itemView.findViewById(R.id.receipt_dialog));
                    builder.setView(view);

                    alertDialog = builder.create();
                    CALENDAR = Calendar.getInstance();

                    ((TextView) view.findViewById(R.id.dialog_title)).setText("Update Receipt Detail");

                    RECEIPT_DATE = view.findViewById(R.id.dia_receipt_date);
                    INTEREST_AMT = view.findViewById(R.id.dia_receipt_interest_amt);

                    SAVE = view.findViewById(R.id.dialog_receipt_save);
                    CANCEL = view.findViewById(R.id.dialog_receipt_cancel);

                    SAVE.setText("Update");

                    String originalDateString = receiptModel.getDate();
                    SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                    try {
                        Date date = originalFormat.parse(originalDateString);
                        SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                        String updateDate = newFormat.format(date);
                        RECEIPT_DATE.setText(updateDate);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    INTEREST_AMT.setText("" + receiptModel.getInterest_amt());

                    RECEIPT_DATE.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new DatePickerDialog(context, date, CALENDAR.get(Calendar.YEAR),
                                    CALENDAR.get(Calendar.MONTH),
                                    CALENDAR.get(Calendar.DAY_OF_MONTH)).show();
                        }
                    });

                    SAVE.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            update(receiptModel.getCust_id(), receiptModel.getLoan_no(), receiptModel.getSerial());
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
        return receiptModels.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        private TextView DATE,INTEREST_AMT;
        private ImageButton EDIT,DELETE;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            DATE = itemView.findViewById(R.id.item_receipt_date);
            INTEREST_AMT = itemView.findViewById(R.id.item_interest_amt);
            EDIT = itemView.findViewById(R.id.receipt_edit);
            DELETE = itemView.findViewById(R.id.receipt_delete);
        }
    }

    public void Delete(String cust_id,String loan_no,String serial){

        StringRequest request = new StringRequest(Request.Method.POST, delete_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equalsIgnoreCase("deleted Successfully")){
                            Toast.makeText(context, "Item deleted successfully", Toast.LENGTH_SHORT).show();
                            ((Receipt) context).fetch();
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
                params.put("cust_id", cust_id);
                params.put("loan_no", loan_no);
                params.put("serial", serial);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }
    public void update(String cust_id,String loan_no,String serial) {

        String Receipt_date = RECEIPT_DATE.getText().toString().trim();
        String Interest_amt = INTEREST_AMT.getText().toString();

        StringRequest request = new StringRequest(Request.Method.POST, update_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equalsIgnoreCase("Updated Successfully")){
                            Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show();
                            ((Receipt) context).fetch();
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
                params.put("cust_id", cust_id);
                params.put("loan_no", loan_no);
                params.put("serial", serial);
                params.put("recep_date", Receipt_date);
                params.put("inter_amt", Interest_amt);
                params.put("userName", userName);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }
    private void getFromDate() {
        String DateFormat = "dd-MM-yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormat, Locale.US);
        RECEIPT_DATE.setText(dateFormat.format(CALENDAR.getTime()));
    }
}
