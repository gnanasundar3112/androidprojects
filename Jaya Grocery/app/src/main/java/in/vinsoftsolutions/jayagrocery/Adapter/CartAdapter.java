package in.vinsoftsolutions.jayagrocery.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import in.vinsoftsolutions.jayagrocery.Address;
import in.vinsoftsolutions.jayagrocery.CartActivity;
import in.vinsoftsolutions.jayagrocery.Models.CartModel;
import in.vinsoftsolutions.jayagrocery.R;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ImageViewHolder>{

    private Context mContext;
    private List<CartModel> products = new ArrayList<>();

    public CartAdapter(Context mContext, List<CartModel> products) {
        this.mContext = mContext;
        this.products = products;
    }

    @NonNull
    @Override
    public CartAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_cart_prod,parent,false);
        return new CartAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ImageViewHolder holder, int position) {
        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.slide_in_left);
        holder.itemView.startAnimation(animation);

        final CartModel cartModel = products.get(position);

        holder.CART_PROD_NAM.setText(cartModel.getCart_eng());
        holder.CART_PROD_TAM.setText(cartModel.getCart_tam());
        holder.CART_PROD_PRICE.setText("₹ "+cartModel.getCart_price());
        holder.CART_PROD_GRM.setText(""+cartModel.getCart_gram());
        holder.CART_PROD_QTY.setText(""+cartModel.getCart_qty());
        holder.CART_PROD_TOTAL.setText("₹ "+cartModel.getCart_totalPrice());
        holder.CART_PROD_STOCK.setText(cartModel.getCart_out_stock());

        Glide.with(mContext).load(cartModel.getCart_image()).placeholder(R.drawable.prograss_animination).into(holder.CART_PROD_IMG);

        String outStock = String.valueOf("2");
        String OUT_STOCK = String.valueOf("Out of Stock");

        if(outStock.equalsIgnoreCase(cartModel.getCart_out_stock())){
            holder.CART_PROD_STOCK.setText(OUT_STOCK);
            ((CartActivity) mContext).updateOutOfStock();
        }else {
            holder.CART_PROD_STOCK.setText("");
        }

        holder.CART_PROD_DELETE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialogTheme);
                View view1 = LayoutInflater.from(mContext).inflate(R.layout.delete_dialog,
                        (ConstraintLayout)holder.itemView.findViewById(R.id.warning_dialog));

                builder.setView(view1);
                ((TextView) view1.findViewById(R.id.dialog_title)).setText("DELETE");
                ((TextView) view1.findViewById(R.id.dialog_message)).setText("Are you sure you want to remove this item?");
                ((Button) view1.findViewById(R.id.dialog_btn)).setText("NO");
                ((Button) view1.findViewById(R.id.dialog_btn2)).setText("YES");
                ((ImageView) view1.findViewById(R.id.dialog_image)).setImageResource(R.drawable.baseline_logout);

                final AlertDialog alertDialog = builder.create();

                view1.findViewById(R.id.dialog_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view){
                        alertDialog.dismiss();
                    }
                });


                view1.findViewById(R.id.dialog_btn2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        StringRequest request = new StringRequest(Request.Method.POST, "https://vinsoftsolutions.in/jaya-android/cart_prod_delete.php",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject object = new JSONObject(response);
                                            String check = object.getString("state");
                                            if (check.equals("delete")) {
                                                Delete(position);
                                                Toast.makeText(mContext, "DELETE", Toast.LENGTH_SHORT).show();

                                                // Recalculate the cart total and update it in CartActivity
                                                double newCartTotal = calculateNewCartTotal();
                                                ((CartActivity) mContext).updateCartTotal(newCartTotal);

                                                ((CartActivity) mContext).updateEnableOutOfStock();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    public void Delete(int item) {
                                        products.remove(item);
                                        notifyItemRemoved(item);
                                    }

                                    // this calculate for live // Calculate the new cart total by iterating through CART_PRODUCTS
                                    private double calculateNewCartTotal() {
                                        double total = 0;
                                        for (CartModel item : products) {
                                            total += item.getCart_totalPrice();
                                        }
                                        return total;
                                    }

                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                                    Toast.makeText(mContext, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }) {
                            @Nullable
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();

                                params.put("prod_id", cartModel.getCart_id());
                                params.put("cust_id", cartModel.getCart_cust_id());

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
        return products.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        private ImageView CART_PROD_IMG,CART_PROD_DELETE;
        private MaterialTextView CART_PROD_NAM,CART_PROD_TAM,CART_PROD_PRICE,CART_PROD_GRM,CART_PROD_QTY,CART_PROD_TOTAL,CART_PROD_STOCK;
        private ConstraintLayout CART_WARNING_DIALOG,CART_PROD_LINEAR;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            CART_PROD_NAM = itemView.findViewById(R.id.cart_item_eng);
            CART_PROD_TAM = itemView.findViewById(R.id.cart_item_tam);
            CART_PROD_PRICE = itemView.findViewById(R.id.cart_item_price);
            CART_PROD_GRM = itemView.findViewById(R.id.cart_item_gram);
            CART_PROD_QTY = itemView.findViewById(R.id.cart_item_qty);
            CART_PROD_TOTAL = itemView.findViewById(R.id.cart_item_total);
            CART_PROD_STOCK = itemView.findViewById(R.id.out_stock);
            CART_PROD_IMG = itemView.findViewById(R.id.cart_item_img);
            CART_PROD_DELETE = itemView.findViewById(R.id.cart_item_delete);
            CART_PROD_LINEAR = itemView.findViewById(R.id.cart_linear);
            //CART_WARNING_DIALOG =itemView.findViewById(R.id.warning_dialog);
        }
    }
}
