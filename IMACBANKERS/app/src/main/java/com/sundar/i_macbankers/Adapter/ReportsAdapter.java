package com.sundar.i_macbankers.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sundar.i_macbankers.GradeRate;
import com.sundar.i_macbankers.Links;
import com.sundar.i_macbankers.Models.CateModel;
import com.sundar.i_macbankers.Models.GradeRateModel;
import com.sundar.i_macbankers.Models.ReceiptModel;
import com.sundar.i_macbankers.Models.Report_receipt_model;
import com.sundar.i_macbankers.Models.ReportsModel;
import com.sundar.i_macbankers.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.ImageViewHolder> {

    AlertDialog alertDialog;
    String report_receipt_url;

    private ArrayList<Report_receipt_model> report_receipt_models = new ArrayList<>();
    private ListView listView;
    private TextView RECEIPT_TOT;
    private double Rep_tot;

    private Context context;
    private List<ReportsModel> reportsModels = new ArrayList<>();

    public ReportsAdapter(Context context, List<ReportsModel> reportsModels) {
        this.context = context;
        this.reportsModels = reportsModels;

        report_receipt_url = Links.receipt_report;
    }

    @NonNull
    @Override
    public ReportsAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(context).inflate(R.layout.item_reports,parent,false);
        return new ReportsAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportsAdapter.ImageViewHolder holder, int position) {
        final ReportsModel reportsModel = reportsModels.get(position);

        holder.LOAN_DATE.setText(reportsModel.getLoan_date());
        holder.LOAN_NO.setText(reportsModel.getLoan_no());
        holder.PARTY_NAME.setText(reportsModel.getParty_name());
        holder.GRAMS.setText(reportsModel.getGrams());
        holder.LOAN_AMT.setText(""+reportsModel.getLoan_amt());
        holder.INT_PER.setText(reportsModel.getInt_per());
        holder.INT_AMT.setText(""+reportsModel.getInt_amt());
        holder.TOL_INT.setText(""+reportsModel.getTol_amt());
        holder.TOT_PAID.setText(""+reportsModel.getTol_paid());
        holder.BAL_INT.setText(""+reportsModel.getBal_int());
        holder.STATUS.setText(reportsModel.getStatus());
        holder.CL_DATE.setText(reportsModel.getCl_date());

        String STATUS = String.valueOf("open");
        if (STATUS.equalsIgnoreCase(reportsModel.getStatus())) {
            holder.REPORT_ROW.setBackgroundColor(Color.parseColor("#CAF7B8"));
        } else {
            holder.REPORT_ROW.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        holder.VIEW.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingInflatedId")
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                View view = LayoutInflater.from(context).inflate(R.layout.report_det,
                        (LinearLayout)holder.itemView.findViewById(R.id.report_rec_item));
                builder.setView(view);

                alertDialog = builder.create();

                spinnerValues(reportsModel.getLoan_no());

                listView = view.findViewById(R.id.receipt_list);
                RECEIPT_TOT = view.findViewById(R.id.rep_tot);

                if (alertDialog.getWindow() != null) {
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                alertDialog.show();
            }
        });
    }

    private void spinnerValues(String loan_no) {

        StringRequest request = new StringRequest(Request.Method.POST, report_receipt_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        report_receipt_models.clear();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            Rep_tot = 0;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                String recep_date = object.getString("recep_date");
                                double inter_amt = object.getDouble("inter_amt");

                                Rep_tot += inter_amt;

                                SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                String updateDate = null;

                                try {
                                    Date date = originalFormat.parse(recep_date);
                                    SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                    updateDate = newFormat.format(date);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                Report_receipt_model reportReceiptModel = new Report_receipt_model();
                                reportReceiptModel.setDate(updateDate);
                                reportReceiptModel.setAmount(inter_amt);
                                report_receipt_models.add(reportReceiptModel);

                                Report_receipt_adapter listAdapter = new Report_receipt_adapter(context,report_receipt_models);
                                listView.setAdapter(listAdapter);
                            }

                            RECEIPT_TOT.setText(String.valueOf(Rep_tot));

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
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("loan_no", loan_no);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);

    }

    @Override
    public int getItemCount() {
        return reportsModels.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        private TextView LOAN_DATE,LOAN_NO,PARTY_NAME,GRAMS,LOAN_AMT,INT_PER,INT_AMT,TOL_INT,TOT_PAID,BAL_INT,STATUS,CL_DATE;
        private ImageView VIEW;
        private TableRow REPORT_ROW;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            LOAN_DATE = itemView.findViewById(R.id.item_report_loan_date);
            LOAN_NO = itemView.findViewById(R.id.item_report_loan_no);
            PARTY_NAME = itemView.findViewById(R.id.item_report_party_name);
            GRAMS = itemView.findViewById(R.id.item_report_grm);
            LOAN_AMT = itemView.findViewById(R.id.item_report_loan_amt);
            INT_PER = itemView.findViewById(R.id.item_report_int_per);
            INT_AMT = itemView.findViewById(R.id.item_report_int_amt);
            TOL_INT = itemView.findViewById(R.id.item_report_tol_int);
            TOT_PAID = itemView.findViewById(R.id.item_report_tol_amt);
            BAL_INT = itemView.findViewById(R.id.item_report_bal_amt);
            STATUS = itemView.findViewById(R.id.item_report_status);
            CL_DATE  = itemView.findViewById(R.id.item_report_cl_date);
            VIEW  = itemView.findViewById(R.id.report_view);
            REPORT_ROW = itemView.findViewById(R.id.report_row);
        }
    }
}
