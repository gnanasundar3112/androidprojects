package com.sundar.i_macbankers.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.sundar.i_macbankers.CateMaster;
import com.sundar.i_macbankers.GradeRate;
import com.sundar.i_macbankers.Links;
import com.sundar.i_macbankers.Models.GradeRateModel;
import com.sundar.i_macbankers.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GradeRateAdapter extends RecyclerView.Adapter<GradeRateAdapter.ImageViewHolder> {
    Context context;
    private List<GradeRateModel> gradeRateModels = new ArrayList<>();
    AlertDialog alertDialog;
    String userName,delete_url;

    public GradeRateAdapter(Context context, List<GradeRateModel> gradeRateModels) {
        this.context = context;
        this.gradeRateModels = gradeRateModels;
        delete_url = Links.grade_rate_delete;
    }

    @NonNull
    @Override
    public GradeRateAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_grade_rate,parent,false);
        return new GradeRateAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GradeRateAdapter.ImageViewHolder holder, int position) {
        final GradeRateModel gradeRateModel = gradeRateModels.get(position);
        holder.GRADE_NAME.setText(gradeRateModel.getGrade_name());
        holder.GRADE_RATE.setText(gradeRateModel.getRate());

        SharedPreferences sharedPreferences = ((GradeRate) context).getSharedPreferences();
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
                            Delete(gradeRateModel.getGrade_id());
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
    }

    @Override
    public int getItemCount() {
        return gradeRateModels.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        private TextView GRADE_NAME,GRADE_RATE;
        private ImageButton DELETE;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            GRADE_NAME = itemView.findViewById(R.id.grade_name);
            GRADE_RATE = itemView.findViewById(R.id.grade_rate);
            DELETE = itemView.findViewById(R.id.grade_delete);
        }
    }

    public void Delete(String grade_id){

        StringRequest request = new StringRequest(Request.Method.POST, delete_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equalsIgnoreCase("deleted Successfully")){
                            Toast.makeText(context, "Item deleted successfully", Toast.LENGTH_SHORT).show();
                            ((GradeRate) context).fetch();
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
                params.put("grade_id", grade_id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }
}
