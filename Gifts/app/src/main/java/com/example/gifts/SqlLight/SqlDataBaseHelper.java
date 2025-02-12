package com.example.gifts.SqlLight;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class SqlDataBaseHelper extends SQLiteOpenHelper {
    final static String DATABASE_NAME = "function.db";

    public SqlDataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE accounts (`fun_id` varchar(8) NOT NULL DEFAULT '',`fun_name` varchar(100) NOT NULL DEFAULT '',`party_name` varchar(100) NOT NULL DEFAULT ''," +
                "  `area_name` varchar(50) NOT NULL DEFAULT '', `in_amt` decimal(11,2) DEFAULT 0.00, `remarks` varchar(300) NOT NULL DEFAULT '', `out_amt` decimal(11,2) DEFAULT 0.00," +
                "  `other_amt` decimal(11,2) NOT NULL DEFAULT 0.00, `crea_user` varchar(30),`crea_date` date NOT NULL DEFAULT '0000-00-00',`crea_stat` varchar(3) ," +
                "  `crea_time` varchar(8) NOT NULL DEFAULT '00:00:00', `modi_user` varchar(30) , `modi_date` date NOT NULL DEFAULT '0000-00-00',`modi_stat` varchar(3) ," +
                "  `modi_time` varchar(8) NOT NULL DEFAULT '00:00:00')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS accounts");
        onCreate(sqLiteDatabase);
    }


    public static void exportDatabase(Context context) {
        try {
            File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Function");
            if (!folder.exists()) {
                folder.mkdirs();
            }

            File file = new File(folder, "function.db");
            file.createNewFile();

            FileInputStream fis = new FileInputStream(context.getDatabasePath(SqlDataBaseHelper.DATABASE_NAME));
            FileOutputStream fos = new FileOutputStream(file);
            FileChannel src = fis.getChannel();
            FileChannel dst = fos.getChannel();
            dst.transferFrom(src, 0, src.size());

            fis.close();
            src.close();
            fos.close();
            dst.close();

            Toast.makeText(context, "Data Backup Successfully " , Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void importDatabase(Context context) {
        try {
            File importDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Function");
            File file = new File(importDir, "function.db");

            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                FileOutputStream fos = new FileOutputStream(context.getDatabasePath(SqlDataBaseHelper.DATABASE_NAME));
                FileChannel src = fis.getChannel();
                FileChannel dst = fos.getChannel();
                dst.transferFrom(src, 0, src.size());

                fis.close();
                src.close();
                fos.close();
                dst.close();

                Toast.makeText(context, "Data Restore Successfully" , Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "Database file not found.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
