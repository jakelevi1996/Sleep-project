package com.example.jake.heartbeat04;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    // Constants for list positions
    final static int GETTING_STARTED    = 0;
    final static int VIEW_DATA          = 1;
    final static int VIEW_GRAPHS        = 2;
    final static int SETTINGS           = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService(new Intent(this, MessageListenerService.class));


        ListView mainMenuListView = (ListView) findViewById(R.id.main_menu_list);
        //Create an OnItemClickListener
        AdapterView.OnItemClickListener itemClickListener =
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> listView,
                                            View v,
                                            int position,
                                            long id) {
                        switch (position) {
                            case GETTING_STARTED:
                                startActivity(new Intent(MainActivity.this, FutureReleaseActivity.class));
                                break;
                            case VIEW_DATA:
                                startActivity(new Intent(MainActivity.this, ListOfFilesActivity.class));
                                break;
                            case VIEW_GRAPHS:
                                startActivity(new Intent(MainActivity.this, FutureReleaseActivity.class));
                                break;
                            case SETTINGS:
                                startActivity(new Intent(MainActivity.this, FutureReleaseActivity.class));
                                break;
                            case 4:
                                startService(new Intent(MainActivity.this, MessageListenerService.class));
                                break;
                            case 5:
                                stopService(new Intent(MainActivity.this, MessageListenerService.class));
                                break;
                        }

                    }
                };
        //Add the listener to the list view
        mainMenuListView.setOnItemClickListener(itemClickListener);
    }
}
