package com.example.jake.heartbeat04;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends WearableActivity {

    // Constants for list positions
    final static int GETTING_STARTED    = 0;
    final static int RECORD_DATA        = 1;
    final static int VIEW_SENSORS       = 2;
    final static int SETTINGS           = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();

        ListView mainMenuListView = (ListView) findViewById(R.id.main_menu_list_view);
        setListViewAdapter(mainMenuListView);

    }

    void setListViewAdapter(ListView listView) {

        //Create an OnItemClickListener
        AdapterView.OnItemClickListener itemClickListener =
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> listView,
                                            View v,
                                            int position,
                                            long id) {
                        Intent intent;
                        switch (position) {
                            case GETTING_STARTED:
                                break;
                            case RECORD_DATA:
                                intent = new Intent(MainActivity.this, FileNameActivity.class);
                                startActivity(intent);
                                break;
                            case VIEW_SENSORS:
                                intent = new Intent(MainActivity.this, ViewSensorsActivity.class);
                                startActivity(intent);
                                break;
                            case SETTINGS:
                                break;
                        }

                    }
                };
        //Add the listener to the list view
        listView.setOnItemClickListener(itemClickListener);

    }
}
