package com.example.adminpanel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Profile extends AppCompatActivity {

    Button LOGOUT;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        LOGOUT = findViewById(R.id.log_out);

        LOGOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Profile.this, R.style.AlertDialogTheme);
                View view1 = LayoutInflater.from(Profile.this).inflate(R.layout.warning_dialog,
                        (ConstraintLayout)findViewById(R.id.warning_dialog));

                builder.setView(view1);
                ((TextView) view1.findViewById(R.id.dialog_title)).setText("Logout");
                ((TextView) view1.findViewById(R.id.dialog_message)).setText("Are you sure you want to logout");
                ((Button) view1.findViewById(R.id.dialog_btn)).setText("NO");
                ((Button) view1.findViewById(R.id.dialog_btn2)).setText("YES");
                //((ImageView) view1.findViewById(R.id.dialog_image)).setImageResource(R.drawable.onion1);


                final android.app.AlertDialog alertDialog = builder.create();

                view1.findViewById(R.id.dialog_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

                view1.findViewById(R.id.dialog_btn2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences sharedPreferences = getSharedPreferences(Login.PREFS_NAME,0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putBoolean("LoggedIn",false);
                        editor.clear();
                        editor.commit();

                        startActivity(new Intent(getApplicationContext(), Login.class));
                        overridePendingTransition(0,0);
                        Toast.makeText(Profile.this, "Logout", Toast.LENGTH_SHORT).show();
                        finish();

                        alertDialog.dismiss();
                    }
                });
                if (alertDialog.getWindow() !=null){
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                alertDialog.show();
            }
        });
    }
}