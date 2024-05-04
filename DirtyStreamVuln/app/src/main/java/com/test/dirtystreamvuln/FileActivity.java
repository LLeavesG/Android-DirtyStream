package com.test.dirtystreamvuln;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        handleIntent();
    }

    @SuppressLint("Range")
    public String getFileName(Context context, Uri uri) {
        String fileName = null;
        int size = 0;

        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    fileName = cursor.getString(cursor.getColumnIndex("_display_name"));
                    size = cursor.getInt(cursor.getColumnIndex("_size"));
                }
            }
        }
        Log.d("FileActivity", "File name: " + fileName + " Size: " + size);
        return fileName;
    }

    public static boolean checkForbidenPath(String path) {
        return path.startsWith("/data/user") || path.startsWith("/data/data/com.test.dirtystreamvuln");
    }

    public static void copyTo(InputStream in, OutputStream out){
        byte[] buffer = new byte[1024];
        int read;
        try {
            while((read = in.read(buffer)) != -1){
                out.write(buffer, 0, read);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void copyFile(Uri uri, String filename) {
        Log.d("FileActivity", "Copying file: " + filename);
        InputStream inputStream = null;

        File cache = new File(getFilesDir(), filename);

        try{
            inputStream = getApplicationContext().getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(cache);
            copyTo(inputStream, outputStream);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleIntent() {
        Log.d("FileActivity", "Handling intent");
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        Uri data = intent.getData();
        String filename = getFileName(this, data);
        String path = data.getPath();
        Log.d("FileActivity", "Path: " + path);

        if (checkForbidenPath(path)) {
            Log.d("FileActivity", "Forbidden path detected: " + path);
            return;
        }

        if (filename != null) {
            copyFile(data, filename);
        }

    }
}