package in.vinsoftsolutions.jayagrocery.internet;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;

import in.vinsoftsolutions.jayagrocery.MainActivity;
import in.vinsoftsolutions.jayagrocery.ProductDetail;
import in.vinsoftsolutions.jayagrocery.R;

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
                    if (context instanceof MainActivity) {
                        ((MainActivity) context).fetchProfile();
                        ((MainActivity) context).fetchCategory();
                        ((MainActivity) context).fetchProducts();
                        ((MainActivity) context).cartCount();
                    }else if (context instanceof ProductDetail) {
                        ((ProductDetail) context).cartCount();
                        ((ProductDetail) context).fetchProducts();
                    }
                }
            });
        }
    }
}
