package com.example.jake.heartbeat04;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FileNameActivity extends WearableActivity {

    EditText fileNameEditText;
    public static final String EXTRA_FILE_NAME = "android.intent.extra.FILE_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_name);
        setAmbientEnabled();

        fileNameEditText = (EditText) findViewById(R.id.file_name_edit_text);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
        String defaultFileName = simpleDateFormat.format(new Date()) + ".csv";
        fileNameEditText.setText(defaultFileName);
    }

    public void recordDataButtonClick(View view) {
        String fileName = fileNameEditText.getText().toString();

        Intent intent = new Intent(FileNameActivity.this, RecordDataActivity.class);
        intent.putExtra(EXTRA_FILE_NAME, fileName);
        startActivity(intent);
    }
}
