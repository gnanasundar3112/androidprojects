package com.sundar.i_macbankers.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sundar.i_macbankers.Models.Report_receipt_model;
import com.sundar.i_macbankers.R;

import java.util.ArrayList;

public class Report_receipt_adapter extends BaseAdapter {
    Context context;
    ArrayList<Report_receipt_model> arrayList;
    LayoutInflater layoutInflater;

    public Report_receipt_adapter(Context context, ArrayList<Report_receipt_model> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.item_report_receipt,parent,false);

        TextView DATE,AMOUNT;
        DATE = view.findViewById(R.id.item_rec_date);
        AMOUNT = view.findViewById(R.id.item_int_amt);

        DATE.setText(arrayList.get(position).getDate());
        AMOUNT.setText(""+arrayList.get(position).getAmount());
        return view;
    }
}
