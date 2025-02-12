package com.sundar.ja.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.sundar.ja.DailyActivity;
import com.sundar.ja.Models.DailyModel;
import com.sundar.ja.Models.PartyModel;
import com.sundar.ja.PartyMaster;
import com.sundar.ja.R;
import com.sundar.ja.SqlDatabase.SqlDatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.ImageViewHolder> {
    Context context;
    private List<DailyModel> dailyModel = new ArrayList<>();
    AlertDialog alertDialog;
    private TextInputEditText QTY,AMOUNT,RECEIVED;
    private Spinner PAYMENT;
    private MaterialButton SAVE,CANCEL;
    ArrayAdapter<String> payment_adapter;
    private TextView DATE;
    String AC_ID;
    
    public DailyAdapter(Context context, List<DailyModel> dailyModel) {
        this.context = context;
        this.dailyModel = dailyModel;
    }

    @NonNull
    @Override
    public DailyAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_daily_detail,parent,false);
        return new DailyAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyAdapter.ImageViewHolder holder, int position) {
        final DailyModel dailyModels = dailyModel.get(position);

        holder.DATE.setText(dailyModels.getDate());
        holder.CAN_QTY.setText(""+dailyModels.getQty());
        holder.AMOUNT.setText(""+dailyModels.getAmount());
        holder.RECEIVED.setText(""+dailyModels.getReceived());

        holder.DAY_ROW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle(dailyModels.getParty_name());
                dialog.setMessage("Date  \t\t\t\t\t\t\t\t\t\t\t: \t"+dailyModels.getDate() +
                        "\nPayment Type \t\t: \t" + dailyModels.getPayment() +
                        "\nAmount \t\t\t\t\t\t\t\t: \t₹ "+dailyModels.getAmount() +
                        "\nReceived \t\t\t\t\t\t\t: \t₹ "+dailyModels.getReceived());
                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
        });


        holder.DAY_ROW.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("Activity");
                dialog.setMessage("Choose an action for this activity:");

                dialog.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                        View editview = LayoutInflater.from(context).inflate(R.layout.dialog_daily,
                                (LinearLayout) holder.itemView.findViewById(R.id.daily_dialog));
                        builder.setView(editview);

                        alertDialog = builder.create();

                        ((TextView) editview.findViewById(R.id.dialog_title)).setText("Update Detail");
                        ((MaterialButton) editview.findViewById(R.id.dialog_save)).setText("Update");

                        DATE = editview.findViewById(R.id.date);
                        QTY = editview.findViewById(R.id.dia_can_qty);
                        PAYMENT = editview.findViewById(R.id.dia_payment);
                        RECEIVED = editview.findViewById(R.id.dia_received);
                        AMOUNT = editview.findViewById(R.id.dia_amount);

                        SAVE = editview.findViewById(R.id.dialog_save);
                        CANCEL = editview.findViewById(R.id.dialog_cancel);

                        DATE.setText(dailyModels.getDate());
                        QTY.setText(""+dailyModels.getQty());
                        AMOUNT.setText(""+dailyModels.getAmount());
                        RECEIVED.setText(""+dailyModels.getReceived());

                        payment_adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, context.getResources().getStringArray(R.array.payment));
                        payment_adapter.setDropDownViewResource(R.layout.item_drop_down);
                        PAYMENT.setAdapter(payment_adapter);

                        // Set the selected status based on the value in payedModel
                        String statusValue = dailyModels.getPayment();
                        int position = payment_adapter.getPosition(statusValue);
                        PAYMENT.setSelection(position);

                        SAVE.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                update(dailyModels.getDay_id());
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
                    }
                });

                dialog.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("DELETE");
                        builder.setMessage("Are you sure you want to Delete");

                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Delete(dailyModels.getDay_id());
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                        // Access and modify buttons after showing the dialog
                        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(
                                ContextCompat.getColor(context, R.color.red)
                        );
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = dialog.create();
                alertDialog.show();

                // Access and modify buttons after showing the dialog
                alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(
                        ContextCompat.getColor(context, R.color.red) // Ensure you have a red color in resources
                );

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return dailyModel.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        TableRow DAY_ROW;
        TextView DATE,CAN_QTY,AMOUNT,RECEIVED;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            DAY_ROW = itemView.findViewById(R.id.day_row);
            DATE = itemView.findViewById(R.id.party_date);
            CAN_QTY = itemView.findViewById(R.id.qty);
            AMOUNT = itemView.findViewById(R.id.amount);
            RECEIVED = itemView.findViewById(R.id.received);
        }
    }

    public void Delete(String id){
        SqlDatabaseHelper dbHelper = new SqlDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String day_id = id;
        String[] ID = {day_id};

        int deletedRows = db.delete("dailyactive", "day_id = ?", ID);

        db.close();

        if (deletedRows > 0) {
            Toast.makeText(context, "Item deleted successfully", Toast.LENGTH_SHORT).show();
            ((DailyActivity) context).fetch();
        } else {
            Toast.makeText(context, "Failed to delete item", Toast.LENGTH_SHORT).show();
            ((DailyActivity) context).fetch();
        }
    }

    public void update(String id) {
        SqlDatabaseHelper dbHelper = new SqlDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String qty = QTY.getText().toString().trim();
        String Payment = PAYMENT.getSelectedItem().toString();
        String Amount = AMOUNT.getText().toString().toString();
        String Received = RECEIVED.getText().toString().trim();

        String day_id = id;
        String[] ID = {day_id};

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String Date = dateFormat.format(new Date());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String time = timeFormat.format(new Date());

        if (Payment.equals("CASH")) {
            Payment = "1";
        }else if (Payment.equals("GPAY")){
            Payment = "2";
        }else if (Payment.equals("PAYTM")) {
            Payment = "3";
        }else {
            Payment = "4";
        }
        ContentValues values = new ContentValues();
        values.put("qty",qty);
        values.put("payment",Payment);
        values.put("received",Received);
        values.put("amount",Amount);
        values.put("modi_date",Date);
        values.put("modi_time",time);

        // Correct table name to "income"
        long tableRow =  db.update("dailyactive", values, "day_id = ?", ID);

        if (tableRow != -1) {
            Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show();
            ((DailyActivity) context). fetch();
            alertDialog.dismiss();
        }else{
            Toast.makeText(context, "Updated Failed", Toast.LENGTH_SHORT).show();
        }
    }
}
