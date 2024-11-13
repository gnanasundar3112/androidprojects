package in.vinsoftsolutions.jayagrocery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import in.vinsoftsolutions.jayagrocery.R;

import in.vinsoftsolutions.jayagrocery.internet.NetworkChangeListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Refer_Friends extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private FloatingActionButton BACK_PRESS;
    TextView APPBAR_TITLE;
    private MaterialButton REFER_FRIENDS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_friends);

        BACK_PRESS = findViewById(R.id.fab_backPress);
        APPBAR_TITLE = findViewById(R.id.txt_appbarTitle);
        REFER_FRIENDS = findViewById(R.id.refer_friend);

        APPBAR_TITLE.setText("Refer Friend");
        //back press activity
        BACK_PRESS.setOnClickListener(view -> Refer_Friends.super.onBackPressed());

        REFER_FRIENDS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                share.putExtra(Intent.EXTRA_SUBJECT,"hi this is my app");
//                share.putExtra(Intent.EXTRA_TEXT,"http://play.google.com/store/apps/details?id="+ getApplicationContext().getPackageName());
                share.putExtra(Intent.EXTRA_TEXT,"https://vinsoftsolutions.in/apkJaya/appLink.html");
                startActivity(Intent.createChooser(share,"Share To Friends"));
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