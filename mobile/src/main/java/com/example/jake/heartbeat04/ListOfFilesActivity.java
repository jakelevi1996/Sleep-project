package com.example.jake.heartbeat04;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListOfFilesActivity extends AppCompatActivity {

    public static final String EXTRA_FILENAME = "filename";
    ListView savedFilesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_files);

        savedFilesList = (ListView) findViewById(R.id.saved_files_list);

    }


    @Override
    protected void onResume() {
        super.onResume();

        // create list adapter to display saved files
        ArrayAdapter<String> listAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                fileList());
        savedFilesList.setAdapter(listAdapter);

        // create onClickListener to launch ViewFileActivity
        AdapterView.OnItemClickListener itemClickListener =
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> listView,
                                            View v,
                                            int position,
                                            long id) {

                        String filename = fileList()[position];
                        Intent intent = new Intent(ListOfFilesActivity.this, ViewFileActivity.class);
                        intent.putExtra(EXTRA_FILENAME, filename);
                        startActivity(intent);
                    }
                };

        savedFilesList.setOnItemClickListener(itemClickListener);
    }
}
