package com.sundar.i_macbankers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.net.HttpURLConnection;
import java.net.URL;

public class Settings extends AppCompatActivity {
    private TextView APPBAR_TITLE;
    private ImageView BACK_PRESS,ADD_BTN;
    private Button BACKUP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ADD_BTN = findViewById(R.id.add_btn);
        ADD_BTN.setImageResource(android.R.color.transparent);

        APPBAR_TITLE = findViewById(R.id.appbarTitle);
        APPBAR_TITLE.setText("Settings");
        //back press activity
        BACK_PRESS = findViewById(R.id.backPress);
        BACK_PRESS.setOnClickListener(view -> Settings.super.onBackPressed());

        BACKUP = findViewById(R.id.backup);

        BACKUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Links.backup;
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
    }
}