package com.sundar.i_macbankers.internet;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;

import com.sundar.i_macbankers.CateMaster;
import com.sundar.i_macbankers.GradeMaster;
import com.sundar.i_macbankers.Loan;
import com.sundar.i_macbankers.MainActivity;
import com.sundar.i_macbankers.ProductMaster;
import com.sundar.i_macbankers.R;

public class NetworkChangeListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!common.isConnectedToInternet(context)) {            // internet is not connected
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View layout_dialog = LayoutInflater.from(context).inflate(R.layout.check_internet_connection, null);
            builder.setView(layout_dialog);

            AppCompatButton btnRetry = layout_dialog.findViewById(R.id.retry);

            //show dialog
            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.setCancelable(false);

            dialog.getWindow().setGravity(Gravity.CENTER);

            btnRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    onReceive(context, intent);
                    if (context instanceof CateMaster) {
                        ((CateMaster) context).fetch();
                    }else if (context instanceof ProductMaster) {
                        ((ProductMaster) context).fetch();
                    }else if (context instanceof GradeMaster) {
                        ((GradeMaster) context).fetch();
                    }else if (context instanceof Loan) {
                        ((Loan) context).loanNo();
                    }
                }
            });
        }
    }
}
