package com.sundar.i_macbankers.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sundar.i_macbankers.Models.NotificationModel;
import com.sundar.i_macbankers.R;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class NotificationAdapter extends BaseAdapter {
    Context context;
    ArrayList<NotificationModel> notificationModels = new ArrayList<>();
    LayoutInflater layoutInflater;

    public NotificationAdapter(Context context, ArrayList<NotificationModel> notificationModels) {
        this.context = context;
        this.notificationModels = notificationModels;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return notificationModels.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.item_drop_down,parent,false);

        TextView Notification;
        Notification = view.findViewById(R.id.drop_down);

        Notification.setText("Loan Date               - "+notificationModels.get(position).getLoan_date()+"\nLoan No                  - "+notificationModels.get(position).getLoan_no()+
                "\nCustomer Name    - "+notificationModels.get(position).getCustomer_name());

        return view;
    }
}
