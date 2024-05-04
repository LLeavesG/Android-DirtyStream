package com.test.dirtystreamvuln;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newTextFile("file.txt", "Hello World\n");
        newSharedPrefFile();

//        try {
//            InputStream inputStream = getContentResolver().openInputStream(Uri.parse("content://com.test.vulnapp.fileprovider/root/data/data/com.test.dirtystreamvuln/shared_prefs/shared_pref.xml"));
//            File file = new File( "/sdcard/shared_prefs.xml");
//            FileWriter writer = new FileWriter(file);
//            byte[] buffer = new byte[1024];
//            int read;
//            while ((read = inputStream.read(buffer)) != -1) {
//                writer.write(new String(buffer, 0, read));
//            }
//            writer.close();
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }

    public void newTextFile(String filename, String content) {
        File file = new File(getFilesDir(), filename);
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void newSharedPrefFile() {
        // create shared preference file
        SharedPreferences sharedPref = getSharedPreferences("shared_pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("key", "value");
        editor.commit();

    }
}