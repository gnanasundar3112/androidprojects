package com.sundar.ja.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.sundar.ja.DailyActivity;
import com.sundar.ja.Models.PartyModel;
import com.sundar.ja.PartyMaster;
import com.sundar.ja.R;
import com.sundar.ja.SqlDatabase.SqlDatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PartyAdapter extends RecyclerView.Adapter<PartyAdapter.ImageViewHolder> {
    Context context;
    private List<PartyModel> partyModel = new ArrayList<>();
    AlertDialog alertDialog;
    private TextInputEditText AC_NAME,ADDRESS,MOBILE;
    private Spinner ACTIVE;
    ArrayAdapter<String> active_adapter;
    private MaterialButton SAVE,CANCEL;

    public PartyAdapter(Context context, List<PartyModel> partyModel) {
        this.context = context;
        this.partyModel = partyModel;
    }

    public void setFilteredList(List<PartyModel>filteredList){
        this.partyModel=filteredList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public PartyAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_party,parent,false);
        return new PartyAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PartyAdapter.ImageViewHolder holder, int position) {
        final PartyModel partyModels = partyModel.get(position);

        holder.AC_NAME.setText(partyModels.getParty_name());
        holder.ADDRESS.setText(partyModels.getAddress());
        holder.MOBILE.setText(partyModels.getMobile());

        if (partyModels.getAddress().trim().equals("")) {
            holder.ADDRESS.setVisibility(View.GONE);
        }else {
            holder.ADDRESS.setVisibility(View.VISIBLE);
        }
        if (partyModels.getMobile().trim().equals("")) {
            holder.MOBILE.setVisibility(View.GONE);
        }else {
            holder.MOBILE.setVisibility(View.VISIBLE);
        }


        holder.CONSTRAINTLAYOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DailyActivity.class);
                intent.putExtra("party_id", partyModels.getParty_id());
                intent.putExtra("party_name", partyModels.getParty_name());
                context.startActivity(intent);
            }
        });
        holder.CONSTRAINTLAYOUT.setOnLongClickListener(new View.OnLongClickListener() {
            @SuppressLint("MissingInflatedId")
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("Activity");
                dialog.setMessage("Choose an action for this activity:");


                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                        View editview = LayoutInflater.from(context).inflate(R.layout.partydialog,
                                (LinearLayout) holder.itemView.findViewById(R.id.party_dialog));
                        builder.setView(editview);

                        alertDialog = builder.create();

                        ((TextView) editview.findViewById(R.id.dialog_title)).setText("Update Party Detail");
                        ((MaterialButton) editview.findViewById(R.id.dialog_save)).setText("Update");

                        AC_NAME = editview.findViewById(R.id.dia_ac_name);
                        ADDRESS = editview.findViewById(R.id.dia_address);
                        MOBILE = editview.findViewById(R.id.dia_mobile);
                        ACTIVE = editview.findViewById(R.id.dia_active);
                        SAVE = editview.findViewById(R.id.dialog_save);
                        CANCEL = editview.findViewById(R.id.dialog_cancel);

                        AC_NAME.setText(partyModels.getParty_name());
                        ADDRESS.setText(partyModels.getAddress());
                        MOBILE.setText(partyModels.getMobile());

                        active_adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, context.getResources().getStringArray(R.array.active));
                        active_adapter.setDropDownViewResource(R.layout.item_drop_down);
                        ACTIVE.setAdapter(active_adapter);

                        // Set the selected status based on the value in payedModel
                        String statusValue = partyModels.getActive();
                        int position = active_adapter.getPosition(statusValue);
                        ACTIVE.setSelection(position);

                        SAVE.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                update(partyModels.getParty_id());
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
                                Delete(partyModels.getParty_id());
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
        return partyModel.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        TextView AC_NAME,ADDRESS,MOBILE;
        ConstraintLayout CONSTRAINTLAYOUT;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            AC_NAME = itemView.findViewById(R.id.ac_party);
            ADDRESS = itemView.findViewById(R.id.address);
            MOBILE = itemView.findViewById(R.id.phone);
            CONSTRAINTLAYOUT = itemView.findViewById(R.id.constraint);
        }
    }

    public void Delete(String id){
        SqlDatabaseHelper dbHelper = new SqlDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String party_id = id;
        String[] ID = {party_id};

        int deletedRows = db.delete("accountmaster", "ac_id = ?", ID);

        db.close();

        if (deletedRows > 0) {
            Toast.makeText(context, "Item deleted successfully", Toast.LENGTH_SHORT).show();
            ((PartyMaster) context).fetch();
        } else {
            Toast.makeText(context, "Failed to delete item", Toast.LENGTH_SHORT).show();
            ((PartyMaster) context).fetch();
        }
    }

    public void update(String id) {
        SqlDatabaseHelper dbHelper = new SqlDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String Ac_name = AC_NAME.getText().toString().trim();
        String Address = ADDRESS.getText().toString().trim();
        String Mobile = MOBILE.getText().toString().trim();
        String Active = ACTIVE.getSelectedItem().toString();

        String party_id = id;
        String[] ID = {party_id};

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String Date = dateFormat.format(new Date());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String time = timeFormat.format(new Date());

        if (Active.equals("ENABLE")) {
            Active = "1";
        }else {
            Active = "2";
        }

        ContentValues values = new ContentValues();
        values.put("ac_name",Ac_name.toUpperCase());
        values.put("address",Address.toUpperCase());
        values.put("phone",Mobile);
        values.put("active",Active);
        values.put("modi_date",Date);
        values.put("modi_time",time);

        // Correct table name to "income"
        long tableRow =  db.update("accountmaster", values, "ac_id = ?", ID);

        if (tableRow != -1) {
            Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show();
            ((PartyMaster) context). fetch();
            alertDialog.dismiss();
        }else{
            Toast.makeText(context, "Updated Failed", Toast.LENGTH_SHORT).show();
        }
    }
}
