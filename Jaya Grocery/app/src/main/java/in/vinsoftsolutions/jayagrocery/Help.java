package in.vinsoftsolutions.jayagrocery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import in.vinsoftsolutions.jayagrocery.R;

import in.vinsoftsolutions.jayagrocery.internet.NetworkChangeListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Help extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    private FloatingActionButton BACK_PRESS;
    TextView APPBAR_TITLE;
    private MaterialButton CALL_BUTTON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        BACK_PRESS = findViewById(R.id.fab_backPress);
        APPBAR_TITLE = findViewById(R.id.txt_appbarTitle);

        APPBAR_TITLE.setText("Address");
        //back press activity
        BACK_PRESS.setOnClickListener(view -> Help.super.onBackPressed());

        CALL_BUTTON = findViewById(R.id.call_button);

        CALL_BUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:8778423873"));
                startActivity(intent);
            }
        });
    }
    // network offline filter start
    @Override
    protected void onStart() {
        IntentFilter filter =new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
    // network offline filter End
}