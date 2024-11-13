package com.example.adminpanel.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
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
import com.bumptech.glide.Glide;
import com.example.adminpanel.Model.Cate_Model;
import com.example.adminpanel.Model.Prod_Model;
import com.example.adminpanel.ProductMaster;
import com.example.adminpanel.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Prod_Adapter extends RecyclerView.Adapter<Prod_Adapter.ImageViewHolder> {

    private Context mContext;
    private List<Prod_Model> product = new ArrayList<>();
    Dialog dialog;
    RequestQueue requestQueue;
    ArrayList<String> prod_cate_list = new ArrayList<>();
    ArrayAdapter<String> prod_cate_adapter;
    public Prod_Adapter(Context mContext, List<Prod_Model> products) {
        this.mContext = mContext;
        this.product = products;
    }
    // filter from search bar start
    public void setFilteredList(List<Prod_Model>filteredList){
        this.product=filteredList;
        notifyDataSetChanged();
    }
    // filter from search bar End
    @NonNull
    @Override
    public Prod_Adapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_product,parent,false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Prod_Adapter.ImageViewHolder holder, @SuppressLint("RecyclerView") int position) {

        final Prod_Model prod_models = product.get(position);

        holder.PROD_CATE_NAME.setText(prod_models.getProduct_cate());
        holder.PROD_NAME.setText(prod_models.getProduct_name());
        holder.TAMIL_NAME.setText(prod_models.getProduct_tam_name());
        holder.PROD_SHORT_NAME.setText(prod_models.getProduct_short_name());
        holder.PROD_RATE.setText(prod_models.getProduct_rate());
        holder.PROD_STOCK.setText(prod_models.getProduct_stock());
        holder.PROD_ACTIVE.setText(prod_models.getProduct_active());

        Glide.with(mContext).load(prod_models.getProduct_image()).placeholder(R.drawable.prograss_animination).into(holder.PROD_IMAGE);

        holder.PROD_EDIT.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingInflatedId")
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext, R.style.LogoutDialogThe);
                View view1 = LayoutInflater.from(mContext).inflate(R.layout.product_dialog,
                        (LinearLayout)holder.itemView.findViewById(R.id.prod_dialog));

                builder.setView(view1);
                ((TextView) view1.findViewById(R.id.user_title)).setText("Product Master");
                ((MaterialButton) view1.findViewById(R.id.prod_cancel_btn)).setText("Cancel");
                ((MaterialButton) view1.findViewById(R.id.prod_insert_btn)).setText("Update");

                ((TextInputEditText) view1.findViewById(R.id.prod_name)).setText(prod_models.getProduct_name());
                ((TextInputEditText) view1.findViewById(R.id.tamil_name)).setText(prod_models.getProduct_tam_name());
                ((TextInputEditText) view1.findViewById(R.id.prod_short_name)).setText(prod_models.getProduct_short_name());
                ((TextInputEditText) view1.findViewById(R.id.prod_rate)).setText(prod_models.getProduct_rate());
                ((TextInputEditText) view1.findViewById(R.id.prod_stock)).setText(prod_models.getProduct_stock());
                ((TextView) view1.findViewById(R.id.cate_name_spinner)).setText(prod_models.getProduct_cate());

                Glide.with(mContext).load(prod_models.getProduct_image()).placeholder(R.drawable.prograss_animination).into(((ImageView) view1.findViewById(R.id.prod_image)));

                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mContext, R.array.Active, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_size);
                ((Spinner) view1.findViewById(R.id.prod_active)).setAdapter(adapter);

                final android.app.AlertDialog alertDialog = builder.create();

                view1.findViewById(R.id.cate_name_spinner).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog = new Dialog(mContext);

                        dialog.setContentView(R.layout.searchable_spinner);

                        dialog.getWindow().setLayout(900, 800);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();

                        EditText editText = dialog.findViewById(R.id.search_spinner);
                        ListView listView = dialog.findViewById(R.id.list_spinner);
                        ImageButton cancel = dialog.findViewById(R.id.dialog_cancel);

                        prod_cate_list.clear();
                        requestQueue = Volley.newRequestQueue(mContext);
                        String url1 = "https://caustic-rinses.000webhostapp.com/adminpanel/category_fetch.php";
                        JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.POST, url1, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray jsonArray = response.getJSONArray("catemaster");

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);

                                        String sp_prod_name = object.getString("cate_name");
                                        String sp_prod_id = object.getString("cate_id");

                                        prod_cate_list.add(sp_prod_name);
                                        prod_cate_adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, prod_cate_list);
                                        prod_cate_adapter.setDropDownViewResource(R.layout.spinner_dropdown_size);
                                        listView.setAdapter(prod_cate_adapter);

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        });
                        requestQueue.add(jsonObject);


                        editText.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                prod_cate_adapter.getFilter().filter(charSequence);
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {

                            }
                        });
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                ((TextView) view1.findViewById(R.id.cate_name_spinner)).setText(prod_cate_adapter.getItem(i));
                                dialog.dismiss();
                            }
                        });
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                    }
                });

                view1.findViewById(R.id.prod_cancel_btn).setOnClickListener(new View.OnClickListener() {
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
        holder.PROD_DELETE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialogTheme);
                View view1 = LayoutInflater.from(mContext).inflate(R.layout.warning_dialog,
                        (ConstraintLayout)holder.itemView.findViewById(R.id.warning_dialog));

                builder.setView(view1);
                ((TextView) view1.findViewById(R.id.dialog_title)).setText("DELETE");
                ((TextView) view1.findViewById(R.id.dialog_message)).setText("Confirm delete to product");
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


                        StringRequest request = new StringRequest(Request.Method.POST, "https://caustic-rinses.000webhostapp.com/adminpanel/deletephpfolder/product_delete.php",
                                new Response.Listener<String>() {

                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject object = new JSONObject(response);
                                            String check = object.getString("productmaster");
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

                                params.put("prod_id",prod_models.getProduct_id());

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
        return product.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageView PROD_IMAGE;
        TextView PROD_CATE_NAME,PROD_NAME,TAMIL_NAME,PROD_SHORT_NAME,PROD_RATE,PROD_STOCK,PROD_ACTIVE;
        ImageButton PROD_EDIT,PROD_DELETE;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            PROD_IMAGE = itemView.findViewById(R.id.prod_image);

            PROD_CATE_NAME = itemView.findViewById(R.id.prod_cate_name);
            PROD_NAME = itemView.findViewById(R.id.prod_name);
            TAMIL_NAME = itemView.findViewById(R.id.prod_tamil_name);
            PROD_SHORT_NAME  = itemView.findViewById(R.id.prod_short_name);
            PROD_RATE = itemView.findViewById(R.id.prod_rate);
            PROD_STOCK = itemView.findViewById(R.id.prod_stock);
            PROD_ACTIVE  = itemView.findViewById(R.id.prod_active);

            PROD_EDIT = itemView.findViewById(R.id.prod_edit);
            PROD_DELETE  = itemView.findViewById(R.id.prod_delete);
        }

    }
    // click delete commend start
    public void ProdDelete(int item){
        product.remove(item);
        notifyItemRemoved(item);
    }
    // click delete commend end
}
