package com.sundar.i_macbankers.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.sundar.i_macbankers.CateMaster;
import com.sundar.i_macbankers.Links;
import com.sundar.i_macbankers.Loan;
import com.sundar.i_macbankers.Models.CateModel;
import com.sundar.i_macbankers.Models.LoanModels;
import com.sundar.i_macbankers.R;
import com.sundar.i_macbankers.Receipt;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class LoanAdapter extends RecyclerView.Adapter<LoanAdapter.ImageViewHolder> {
    Context context;
    private List<LoanModels> loanModels = new ArrayList<>();
    AlertDialog alertDialog;
    private TextView SELECT_PROD_NAME,GRADE_NAME;
    private EditText RATE,QTY,WEIGHT,WAST_WEIGHT,NET_WEIGHT,AMOUNT;
    private Dialog DIALOG;
    private String SELECT_PROD_ID="",GRADE_ID="";
    private ArrayAdapter<String> adapter;
    private MaterialButton SAVE,CANCEL;

    private String userName,product_url,grade_url,grade_rate_url,delete_url,update_url;

    public LoanAdapter(Context context, List<LoanModels> loanModels) {
        this.context = context;
        this.loanModels = loanModels;

        product_url = Links.product_fetch;
        grade_url = Links.GradeFetch;
        grade_rate_url = Links.gradeRateFetch;
        delete_url = Links.loan_delete;
        update_url = Links.loan_det_update;

    }

    @NonNull
    @Override
    public LoanAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_loan,parent,false);
        return new LoanAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoanAdapter.ImageViewHolder holder, int position) {
        final LoanModels loanModel = loanModels.get(position);
        holder.PROD_NAME.setText(loanModel.getProd_name());
        holder.GRADE_NAME.setText(loanModel.getGrade_name());
        holder.RATE.setText(""+loanModel.getRate());
        holder.QTY.setText(loanModel.getQty());
        holder.WEIGHT.setText(loanModel.getWeight());
        holder.WST_WT.setText(loanModel.getWst_wt());
        holder.NET_WT.setText(loanModel.getNet_wt());
        holder.AMOUNT.setText(""+loanModel.getAmount());

        SharedPreferences sharedPreferences = ((Loan) context).getSharedPreferences();
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
                            Delete(loanModel.getLoan_no(), loanModel.getSerial());
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
                    View view = LayoutInflater.from(context).inflate(R.layout.loan_dialog,
                            (LinearLayout) holder.itemView.findViewById(R.id.loan_dialog));
                    builder.setView(view);

                    alertDialog = builder.create();

                    ((TextView) view.findViewById(R.id.loan_title)).setText("Update Receipt Detail");

                    SELECT_PROD_NAME = view.findViewById(R.id.loan_dia_prod_name);
                    GRADE_NAME = view.findViewById(R.id.loan_dia_grade_name);
                    RATE = view.findViewById(R.id.dia_rate);
                    QTY = view.findViewById(R.id.dia_qty);
                    WEIGHT = view.findViewById(R.id.dia_weight);
                    WAST_WEIGHT = view.findViewById(R.id.dia_wast_weight);
                    NET_WEIGHT = view.findViewById(R.id.dia_net_weight);
                    AMOUNT = view.findViewById(R.id.dia_amount);

                    SAVE = view.findViewById(R.id.dialog_loan_save);
                    CANCEL = view.findViewById(R.id.dialog_loan_cancel);

                    SAVE.setText("Update");

                    SELECT_PROD_NAME.setText(loanModel.getProd_name());
                    SELECT_PROD_ID = loanModel.getProd_id();

                    GRADE_NAME.setText(loanModel.getGrade_name());
                    GRADE_ID = loanModel.getGrade_id();

                    RATE.setText("" + loanModel.getRate());
                    QTY.setText(loanModel.getQty());
                    WEIGHT.setText(loanModel.getWeight());
                    WAST_WEIGHT.setText(loanModel.getWst_wt());
                    NET_WEIGHT.setText(loanModel.getNet_wt());
                    AMOUNT.setText("" + loanModel.getAmount());

                    SELECT_PROD_NAME.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            productFetch();
                        }
                    });

                    GRADE_NAME.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String product = SELECT_PROD_ID;
                            if (product.isEmpty()) {
                                Toast.makeText(context, "Please Select Product Name", Toast.LENGTH_SHORT).show();
                            } else {
                                GradeFetch(loanModel);
                            }
                        }
                    });

                    WEIGHT.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            // Not needed
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            double Amount = 0;
                            if (!TextUtils.isEmpty(s)) { // Check if input is not empty
                                try {
                                    double weight = Double.parseDouble(s.toString());
                                    NET_WEIGHT.setText(String.valueOf(weight));


                                    double rate = Double.valueOf(RATE.getText().toString());
                                    Amount = weight * rate;
                                    AMOUNT.setText(String.valueOf(Amount));

                                } catch (NumberFormatException e) {
                                    e.printStackTrace(); // Handle the exception
                                }
                            } else {
                                AMOUNT.setText("");
                                NET_WEIGHT.setText(s.toString());
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            // Not needed
                        }
                    });

                    WAST_WEIGHT.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            // Not needed
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            double net_weight = 0, Amount = 0;

                            if (!TextUtils.isEmpty(s)) { // Check if input is not empty
                                try {
                                    double wast_weight = Double.parseDouble(s.toString());
                                    double weight = Double.valueOf(WEIGHT.getText().toString());
                                    net_weight = weight - wast_weight;


                                    double rate = Double.valueOf(RATE.getText().toString());
                                    Amount = net_weight * rate;
                                } catch (NumberFormatException e) {
                                    e.printStackTrace(); // Handle the exception
                                }
                            } else {
                                double rate = Double.valueOf(RATE.getText().toString());
                                double weight = Double.valueOf(WEIGHT.getText().toString());
                                Amount = weight * rate;
                                net_weight = Double.valueOf(WEIGHT.getText().toString());
                            }

                            // Format net_weight to two decimal places
                            String formattedNetWeight = String.valueOf(net_weight);
                            NET_WEIGHT.setText(formattedNetWeight);

                            AMOUNT.setText(String.valueOf(Amount));

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            // Not needed
                        }
                    });


                    SAVE.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            update(loanModel.getLoan_no(), loanModel.getSerial(), loanModel);
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
        return loanModels.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        private TextView PROD_NAME,GRADE_NAME,RATE,QTY,WEIGHT,WST_WT,NET_WT,AMOUNT;
        private ImageButton EDIT,DELETE;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            PROD_NAME = itemView.findViewById(R.id.item_prod_name);
            GRADE_NAME = itemView.findViewById(R.id.item_grade_name);
            RATE = itemView.findViewById(R.id.item_rate);
            QTY = itemView.findViewById(R.id.item_qty);
            WEIGHT = itemView.findViewById(R.id.item_wt);
            WST_WT = itemView.findViewById(R.id.item_wst_wt);
            NET_WT = itemView.findViewById(R.id.item_net_wt);
            AMOUNT = itemView.findViewById(R.id.item_amount);
            EDIT = itemView.findViewById(R.id.loan_edit);
            DELETE = itemView.findViewById(R.id.loan_delete);
        }
    }


    public void productFetch(){
        DIALOG = new Dialog(context);
        DIALOG.setContentView(R.layout.dialog_search_spinner);

        // Set dialog width to match parent
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(DIALOG.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = 1000;
        DIALOG.getWindow().setAttributes(layoutParams);

        DIALOG.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        DIALOG.show();

        TextView Tittle = DIALOG.findViewById(R.id.dialog_spinner_title);
        Tittle.setText("Select Product");
        EditText editText = DIALOG.findViewById(R.id.spinner_search);
        ListView listView = DIALOG.findViewById(R.id.spinner_list);

        HashMap<String, String> PROD_NAME = new HashMap<>();

        StringRequest request = new StringRequest(Request.Method.POST, product_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject object = jsonArray.getJSONObject(i);

                                String prod_id = object.getString("prod_id");
                                String prod_name = object.getString("prod_name");

                                PROD_NAME.put(prod_id, prod_name);
                            }

                            List<String> PRODUCT = new ArrayList<>(PROD_NAME.values());
                            adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, PRODUCT);
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(context, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);



        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("Range")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedProdName = adapter.getItem(position);

                // Find the key corresponding to the selected value
                String selectedProdId = null;

                for (Map.Entry<String, String> entry : PROD_NAME.entrySet()) {
                    if (entry.getValue().equals(selectedProdName)) {
                        selectedProdId = entry.getKey();
                        break;
                    }
                }

                if (selectedProdId != null) {
                    SELECT_PROD_NAME.setText(selectedProdName);
                    SELECT_PROD_ID = selectedProdId;
                }

                DIALOG.dismiss();
            }
        });

    }
    public void GradeFetch(LoanModels loanModels){
        DIALOG = new Dialog(context);
        DIALOG.setContentView(R.layout.dialog_search_spinner);

        // Set dialog width to match parent
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(DIALOG.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = 1000;
        DIALOG.getWindow().setAttributes(layoutParams);

        DIALOG.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        DIALOG.show();

        TextView Tittle = DIALOG.findViewById(R.id.dialog_spinner_title);
        Tittle.setText("Select Grade");
        EditText editText = DIALOG.findViewById(R.id.spinner_search);
        ListView listView = DIALOG.findViewById(R.id.spinner_list);

        HashMap<String, String> GRADENAME = new HashMap<>();

        StringRequest request = new StringRequest(Request.Method.POST, grade_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject object = jsonArray.getJSONObject(i);

                                String grade_id = object.getString("grade_id");
                                String grade_name = object.getString("grade_name");

                                GRADENAME.put(grade_id, grade_name);
                            }

                            List<String> grade = new ArrayList<>(GRADENAME.values());
                            adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, grade);
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(context, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);



        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("Range")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedGradeName = adapter.getItem(position);

                // Find the key corresponding to the selected value
                String selectedGradeId = null;

                for (Map.Entry<String, String> entry : GRADENAME.entrySet()) {
                    if (entry.getValue().equals(selectedGradeName)) {
                        selectedGradeId = entry.getKey();
                        break;
                    }
                }

                if (selectedGradeId != null) {
                    // Use selectedPartyId and selectedPartyName as neede
                    GRADE_NAME.setText(selectedGradeName);
                    GRADE_ID = selectedGradeId;
                }

                gradeRate(loanModels.getLoan_date(),GRADE_ID);

                DIALOG.dismiss();
            }
        });
    }
    public void gradeRate(String loan_date,String GRADE_ID) {
        String Loan_Date = loan_date;

        StringRequest request = new StringRequest(Request.Method.POST, grade_rate_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equalsIgnoreCase("[]")) {
                            RATE.setText("0.00");
                        } else {
                            try {
                                JSONArray jsonArray = new JSONArray(response);

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String rate = object.getString("rate").trim();

                                    RATE.setText(rate);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(context, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(context, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("loan_date", Loan_Date);
                params.put("grade_id",GRADE_ID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }
    public void Delete(String loan_no,String serial){
        StringRequest request = new StringRequest(Request.Method.POST, delete_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equalsIgnoreCase("deleted Successfully")){
                            Toast.makeText(context, "Item deleted successfully", Toast.LENGTH_SHORT).show();
                            ((Loan) context).fetch();
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
                params.put("loan_no", loan_no);
                params.put("serial", serial);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }
    public void update(String loan_no,String serial,LoanModels loanModels) {

        String cust_id = loanModels.getCust_id();
        String prod_id = SELECT_PROD_ID;
        String grade_id = GRADE_ID;
        String rate = RATE.getText().toString();
        String qty = QTY.getText().toString();
        String weight = WEIGHT.getText().toString();
        String waste_weight = WAST_WEIGHT.getText().toString();
        String net_weight = NET_WEIGHT.getText().toString().trim();
        String amount = AMOUNT.getText().toString();

        if (loan_no.isEmpty()) {
            Toast.makeText(context, "Pleas Enter loan no", Toast.LENGTH_SHORT).show();
        }else if (loanModels.getLoan_date().isEmpty()) {
            Toast.makeText(context, "Pleas Enter loan date", Toast.LENGTH_SHORT).show();
        }else if (cust_id.isEmpty()) {
            Toast.makeText(context, "Pleas Enter customer Name", Toast.LENGTH_SHORT).show();
        }else if (prod_id.isEmpty()) {
            Toast.makeText(context, "Pleas Enter product Name", Toast.LENGTH_SHORT).show();
        }else if (grade_id.isEmpty()) {
            Toast.makeText(context, "Pleas Enter grade Name", Toast.LENGTH_SHORT).show();
        }else if (rate.isEmpty()) {
            Toast.makeText(context, "Pleas Enter rate", Toast.LENGTH_SHORT).show();
        }else if (qty.isEmpty()) {
            Toast.makeText(context, "Pleas Enter qty", Toast.LENGTH_SHORT).show();
        }else if (weight.isEmpty()) {
            Toast.makeText(context, "Pleas Enter weight", Toast.LENGTH_SHORT).show();
        }else if (net_weight.isEmpty()) {
            Toast.makeText(context, "Pleas Enter waste weight", Toast.LENGTH_SHORT).show();
        }else if (amount.isEmpty()) {
            Toast.makeText(context, "Pleas Enter amount", Toast.LENGTH_SHORT).show();
        }else {

            StringRequest request = new StringRequest(Request.Method.POST, update_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equalsIgnoreCase("Updated Successfully")) {
                                Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                ((Loan) context).fetch();
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
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("loan_no", loanModels.getLoan_no());
                    params.put("loan_date", loanModels.getLoan_date());
                    params.put("serial",loanModels.getSerial());
                    params.put("cust_id", cust_id);
                    params.put("prod_id",prod_id);
                    params.put("grade_id", grade_id);
                    params.put("rate", rate);
                    params.put("qty", qty);
                    params.put("weight",weight);
                    params.put("waste_weight", waste_weight);
                    params.put("net_weight", net_weight);
                    params.put("amount", amount);
                    params.put("userName",userName);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(request);
        }
    }
}
