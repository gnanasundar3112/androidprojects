package in.jgssolution.study;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import in.jgssolution.study.Api.RequestUrl;
import in.jgssolution.study.Internet.NetworkChangeListener;

public class SuccessActivity extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private TextView FINAL_SCORE,STUDENT_NAME,REGISTER_NUMBER,RESULT,COMPLETE_DATE;
    private AppCompatButton DOWNLOAD_BTN, EXIT_BTN;
    private String[] PERMISSION = {READ_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE};
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private String ROLL_NO;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        FINAL_SCORE = findViewById(R.id.finalScore);
        STUDENT_NAME = findViewById(R.id.stud_name);
        REGISTER_NUMBER = findViewById(R.id.register_number);
        RESULT = findViewById(R.id.result);
        COMPLETE_DATE = findViewById(R.id.complete_date);
        DOWNLOAD_BTN = findViewById(R.id.downloadBtn);
        EXIT_BTN = findViewById(R.id.exitBtn);

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String rollNo = sharedPreferences.getString("rollNo", "");
        ROLL_NO = rollNo;

        EXIT_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout();
                Intent intent = new Intent(SuccessActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        DOWNLOAD_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    createPdf();
                } else {
                    requestPermission();
                }
            }
        });

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (Environment.isExternalStorageManager()) {
                        Toast.makeText(SuccessActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SuccessActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Logout();
                Intent intent = new Intent(SuccessActivity.this,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        },20000);
    }

    private void ExamResult(){
        if (ROLL_NO.isEmpty()){
            ROLL_NO = "0";
        }
        String url = RequestUrl.EXAM_RESULT + "?rollno=" + ROLL_NO;

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Parse JSON response
                            JSONObject object = new JSONObject(response);

                            String studentname = object.optString("studentname");
                            String score = object.optString("score");
                            String result = object.optString("result");
                            String attendquestion = object.optString("attendquestion");
                            String yearofpass = object.optString("yearofpass");
                            String noofquestion = object.optString("noofquestion");
                            String schedulednoofquestion = object.optString("schedulednoofquestion");

                            STUDENT_NAME.setText(studentname.toUpperCase());
                            FINAL_SCORE.setText(score +" / " + schedulednoofquestion);
                            REGISTER_NUMBER.setText(ROLL_NO);

                            if ("Pass".equalsIgnoreCase(result)) {
                                RESULT.setTextColor(Color.GREEN);
                                RESULT.setText(result.toUpperCase());
                            } else if ("Fail".equalsIgnoreCase(result)) {
                                RESULT.setTextColor(Color.RED);
                                RESULT.setText(result.toUpperCase());
                            } else {
                                RESULT.setTextColor(Color.BLACK);
                                RESULT.setText(result);
                            }
////                           COMPLETE_DATE.setText(getIntent().getStringExtra("complete_date"));


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SuccessActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            int statusCode = error.networkResponse.statusCode;
                            String responseData = new String(error.networkResponse.data);
                            try {
                                JSONObject errorResponse = new JSONObject(responseData);

                                if (statusCode == 404) {
                                    String status = errorResponse.optString("status");
                                    String message = errorResponse.optString("message", "Data not available");

                                    if ("NOT_FOUND".equals(status)) {
                                        Toast.makeText(SuccessActivity.this, message, Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                } else if (statusCode == 500) {
                                    Toast.makeText(SuccessActivity.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                                } else {
                                    String message = errorResponse.optString("message", "Unexpected error occurred.");
                                    Toast.makeText(SuccessActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(SuccessActivity.this, "Error parsing response: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SuccessActivity.this, "Network error: " + (error.getMessage() != null ? error.getMessage() : "Unknown error"), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        // Add request to queue
        RequestQueue requestQueue = Volley.newRequestQueue(SuccessActivity.this);
        requestQueue.add(request);
    }

    private void Logout(){
        String url = RequestUrl.LOG_OUT + "?rollno=" + ROLL_NO;

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(SuccessActivity.this, response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle network error
                        if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                            Toast.makeText(SuccessActivity.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SuccessActivity.this, "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        // Add request to queue
        RequestQueue requestQueue = Volley.newRequestQueue(SuccessActivity.this);
        requestQueue.add(request);
    }

    @Override
    public void onBackPressed() {
        // Create an AlertDialog to confirm exit
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Confirm Exit");
        adb.setMessage("Are you sure you want to exit this exam?");
        adb.setCancelable(false);
        adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(SuccessActivity.this,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        adb.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = adb.create();
        alertDialog.show();
    }

    private void createPdf() {
        // File name and path
        String fileName = "certificate.pdf";
        File filePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);

        // Create a new PDF document
        PdfDocument pdfDocument = new PdfDocument();

        // Set page size and create a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create(); // A4 size: 595x842 pixels
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        // Set up the canvas and paint for drawing
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();

        // Draw border box
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        paint.setColor(Color.BLACK);
        canvas.drawRect(40, 40, canvas.getWidth() - 40, canvas.getHeight() - 40, paint);

        // Draw title
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(24);
        paint.setColor(Color.BLACK);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Certificate of Achievement", canvas.getWidth() / 2, 100, paint);

        // Draw subtitle
        paint.setTextSize(18);

        // Draw regular text
        paint.setTypeface(Typeface.DEFAULT_BOLD); // Regular font
        canvas.drawText("Presented to Spis India", canvas.getWidth() / 2, 150, paint);
        canvas.drawText("For successfully completing Online Assessment Test", canvas.getWidth() / 2, 180, paint);

        // Calculate canvas width and define positioning
        int startXLabel = 60; // Left margin for labels
        int colonX = startXLabel + 200; // Position for the colon ":"
        int startXDetails = colonX + 20; // Position for details after the colon
        int startY = 250; // Starting vertical position
        int lineSpacing = 50; // Space between lines

        // Draw data with proper spacing and alignment
        paint.setTextAlign(Paint.Align.LEFT); // Ensure left alignment

        // Name row
        canvas.drawText("Name", startXLabel, startY, paint);
        canvas.drawText(":", colonX, startY, paint);
        canvas.drawText(STUDENT_NAME.getText().toString(), startXDetails, startY, paint);

        // Register Number row
        canvas.drawText("Register Number", startXLabel, startY + lineSpacing, paint);
        canvas.drawText(":", colonX, startY + lineSpacing, paint);
        canvas.drawText(REGISTER_NUMBER.getText().toString(), startXDetails, startY + lineSpacing, paint);

        // Final Score row
        canvas.drawText("Final Score", startXLabel, startY + 2 * lineSpacing, paint);
        canvas.drawText(":", colonX, startY + 2 * lineSpacing, paint);
        canvas.drawText(FINAL_SCORE.getText().toString(), startXDetails, startY + 2 * lineSpacing, paint);

        // Result row
        canvas.drawText("Result", startXLabel, startY + 3 * lineSpacing, paint);
        canvas.drawText(":", colonX, startY + 3 * lineSpacing, paint);
        canvas.drawText(RESULT.getText().toString(), startXDetails, startY + 3 * lineSpacing, paint);

        // Date of Completion row
        canvas.drawText("Date of Completion", startXLabel, startY + 4 * lineSpacing, paint);
        canvas.drawText(":", colonX, startY + 4 * lineSpacing, paint);
        canvas.drawText(COMPLETE_DATE.getText().toString(), startXDetails, startY + 4 * lineSpacing, paint);

        // Finish the page
        pdfDocument.finishPage(page);

        // Write the document to a file
        try {
            pdfDocument.writeTo(new FileOutputStream(filePath));
            Toast.makeText(this, "Downloaded Successfully to Downloads folder", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error creating PDF: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            pdfDocument.close();
        }
    }


    // THIS PERMISSION FOR ANDROID 11 AND ABOVE AND BELOW STORAGE PERMISSION
    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int readCheck = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
            int writeCheck = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
            return readCheck == PackageManager.PERMISSION_GRANTED && writeCheck == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                activityResultLauncher.launch(intent);
            } catch (Exception e) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                activityResultLauncher.launch(intent);
            }
        } else {
            ActivityCompat.requestPermissions(SuccessActivity.this, PERMISSION, 30);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 30) {
            if (grantResults.length > 0) {
                boolean readPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean writePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (readPermission && writePermission) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        ExamResult();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkChangeListener);
    }
}