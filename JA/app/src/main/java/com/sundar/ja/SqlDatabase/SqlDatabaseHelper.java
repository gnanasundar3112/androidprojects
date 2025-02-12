package com.sundar.ja.SqlDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class SqlDatabaseHelper extends SQLiteOpenHelper {

    final static String DATABASE_NAME = "jawater.db";

    public SqlDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS  accountmaster (ac_id INTEGER PRIMARY KEY AUTOINCREMENT, ac_name TEXT NOT NULL DEFAULT '',address TEXT DEFAULT ''," +
                "phone TEXT DEFAULT '',active INTEGER DEFAULT 1,crea_date DATE DEFAULT '0000-00-00',crea_time TEXT DEFAULT '00:00:00',modi_date DATE DEFAULT '0000-00-00'," +
                "modi_time TEXT DEFAULT '00:00:00')");

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS  dailyactive (day_id TEXT PRIMARY KEY,date DATE DEFAULT '0000-00-00',ac_id INTEGER,qty INTEGER, payment INTEGER DEFAULT 1," +
                "amount DECIMEL DEFAULT '0.00',received DECIMEL DEFAULT '0.00',crea_date DATE DEFAULT '0000-00-00',crea_time TEXT DEFAULT '00:00:00'," +
                "modi_date DATE DEFAULT '0000-00-00',modi_time TEXT DEFAULT '00:00:00')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS accountmaster");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS dailyactive");
        onCreate(sqLiteDatabase);
    }

    public static void exportDatabase(Context context) {
        try {
            File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "JAWATER");
            if (!folder.exists()) {
                folder.mkdirs();
            }

            File file = new File(folder, "jawater.db");
            file.createNewFile();

            FileInputStream fis = new FileInputStream(context.getDatabasePath(SqlDatabaseHelper.DATABASE_NAME));
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
            File importDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "JAWATER");
            File file = new File(importDir, "jawater.db");

            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                FileOutputStream fos = new FileOutputStream(context.getDatabasePath(SqlDatabaseHelper.DATABASE_NAME));
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
