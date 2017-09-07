package com.example.jake.heartbeat04;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

public class ViewFileActivity extends AppCompatActivity {
    private static final String TAG = ViewFileActivity.class.getName();
    String filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_file);

        // read the contents of the file into a text view
        filename = (String) getIntent().getExtras().get(ListOfFilesActivity.EXTRA_FILENAME);
        String contents = readFile(filename);

        // set activity title and text
        setTitle(filename);
        TextView fileContentsTextView = (TextView) findViewById(R.id.filename_textview);
        fileContentsTextView.setText(contents);

    }

    String readFile(String filename) {
        String contents = "";

        File path = this.getFilesDir();
        File file = new File(path, filename);
        int length = (int) file.length();
        byte[] bytes = new byte[length];

        try {
            FileInputStream in = new FileInputStream(file);
            int numRead = in.read(bytes);
            if (numRead != length) {
                Log.e(TAG, "Incomplete read of " + file + ". Read chars " + numRead + " of " + length);
            }
            in.close();
            contents = new String(bytes);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return contents;
    }

    public void emailFile(View view) {
        String to[] = {"jakelevi@hotmail.co.uk"};

        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        try {
            emailIntent.setType("vnd.android.cursor.dir/email");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, filename);

            File fileLocation = new File(getFilesDir(), filename);
            Uri contentUri = FileProvider.getUriForFile(this, "com.example.jake.heartbeat04.fileprovider", fileLocation);
            emailIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            // Grant temporary read permission to the content URI
            emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);


            startActivity(Intent.createChooser(emailIntent, "Send email"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteFile(View view) {

        deleteFile(filename);

        String toastText = "\"" + filename + "\" deleted";
        Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, ListOfFilesActivity.class);
        startActivity(intent);
    }
}
