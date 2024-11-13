package in.vinsoftsolutions.aspn.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import in.vinsoftsolutions.aspn.Models.ReceiptModel;
import in.vinsoftsolutions.aspn.R;
import in.vinsoftsolutions.aspn.Receipt;

public class ReceiptAdapter extends RecyclerView.Adapter<ReceiptAdapter.ViewHolder> {
    Context context;
    private Dialog dialog;
    private List<ReceiptModel> receiptModels = new ArrayList<>();

    public ReceiptAdapter(Context context, List<ReceiptModel> receiptModels) {
        this.context = context;
        this.receiptModels = receiptModels;
    }

    @NonNull
    @Override
    public ReceiptAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_receipt,parent,false);
        return new ReceiptAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiptAdapter.ViewHolder holder, int position) {
        final ReceiptModel receiptModel = receiptModels.get(position);
        holder.BILL_NO.setText(receiptModel.getBill_no());
        holder.BILL_DATE.setText(receiptModel.getBill_date());
        holder.DAYS.setText(receiptModel.getDays());
        holder.AMOUNT.setText(receiptModel.getAmount());
        holder.RPT_AMOUNT.setText(receiptModel.getRpt_amount());

        holder.TABLEROW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new Dialog(context);
                dialog.setContentView(R.layout.receipt_edit_dia);

                // Set dialog width to match parent
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.copyFrom(dialog.getWindow().getAttributes());
                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                layoutParams.height = 1000;
                dialog.getWindow().setAttributes(layoutParams);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();


                TextView BILL_NO = dialog.findViewById(R.id.dialog_bill_no);
                TextView BILL_DATE  = dialog.findViewById(R.id.dialog_bill_date);
                TextView BILL_AMOUNT = dialog.findViewById(R.id.dialog_bill_amount);
                EditText AMOUNT = dialog.findViewById(R.id.dialog_amount);
                ImageView CLOSE = dialog.findViewById(R.id.dialog_close);
                MaterialButton UPDATE = dialog.findViewById(R.id.dialog_update_btn);

                BILL_NO.setText("Bill no : "+receiptModel.getBill_no());
                BILL_DATE.setText("Bill Date : "+receiptModel.getBill_date());
                BILL_AMOUNT.setText("Bill Amount : "+"₹ "+receiptModel.getAmount());

                CLOSE.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                UPDATE.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String amount = AMOUNT.getText().toString().trim();
                        Toast.makeText(context, amount, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return receiptModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView BILL_NO,BILL_DATE,DAYS,AMOUNT,RPT_AMOUNT;
        private TableRow TABLEROW;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            BILL_NO = itemView.findViewById(R.id.bill_no);
            BILL_DATE = itemView.findViewById(R.id.bill_date);
            DAYS = itemView.findViewById(R.id.days);
            AMOUNT = itemView.findViewById(R.id.amount);
            RPT_AMOUNT = itemView.findViewById(R.id.rpt_amt);
            TABLEROW = itemView.findViewById(R.id.rpt_row);
        }
    }
}
