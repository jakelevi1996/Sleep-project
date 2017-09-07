package com.example.jake.heartbeat04;

import android.app.NotificationManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RecordDataActivity extends WearableActivity implements SensorEventListener {
    static final String TAG = RecordDataActivity.class.getName();

    String fileName, accString="", hrString;
    SimpleDateFormat timeSimple = new SimpleDateFormat("hh:mm", Locale.UK);
    SimpleDateFormat timeDetailed = new SimpleDateFormat("hh:mm:ss.SSS", Locale.UK);
    int batteryPercentage;

    // TODO: implement "recordingHeartRate" and "recordingAccelerometer" as settings
    boolean recordingHeartRate = true, recordingAccelerometer = true;

    private SensorManager sensorManager;
    private Sensor heartRateSensor, accelerometerSensor;
    BatteryManager batteryManager;

    TextView hrTextView, accTextView, batteryTextView, timeTextView;

    GoogleApiClient googleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_data);
        setAmbientEnabled();

        hrTextView = (TextView) findViewById(R.id.hr_text_view);
        accTextView = (TextView) findViewById(R.id.acc_text_view);
        batteryTextView = (TextView) findViewById(R.id.battery_text_view);
        timeTextView = (TextView) findViewById(R.id.time_text_view);

        initSensors();
        initGoogleApiClient();

        fileName = getIntent().getStringExtra(FileNameActivity.EXTRA_FILE_NAME);

    }

    @Override
    protected void onResume() { // should reactivate sensors here ????
        googleApiClient.connect();
        super.onResume();
    }

    @Override
    protected void onPause() {
        toggleSensor(heartRateSensor, false);
        toggleSensor(accelerometerSensor, false);
        googleApiClient.disconnect();
        Log.d(TAG, "disconnected");
        super.onPause();
    }

    void toggleSensor(Sensor sensor, boolean switchOn) {
        if (switchOn) sensorManager.registerListener(RecordDataActivity.this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        else sensorManager.unregisterListener(RecordDataActivity.this, sensor);
    }

    void initSensors() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (recordingHeartRate) {
            heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
            toggleSensor(heartRateSensor, true);
            hrTextView.setText("HR: ...");
        } else hrTextView.setText("HR off");

        if (recordingAccelerometer) {
            accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
            toggleSensor(accelerometerSensor, true);
            accTextView.setText("Acc: ...");
        } else accTextView.setText("Acc off");

        batteryManager = (BatteryManager) getSystemService(BATTERY_SERVICE);
        updateTimeBatteryTextViews();
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_HEART_RATE) {
            hrString = String.valueOf(event.values[0]);

            String msg = "HR: " + hrString + " bpm";
            hrTextView.setText(msg);

            updateCSV();
            updateTimeBatteryTextViews();
        }
        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            float x = event.values[0], y = event.values[1], z = event.values[2];
            double acc = Math.sqrt(x*x + y*y + z*z) / 9.81;
            String oldAccString = accString;
            accString = String.format(Locale.UK, "%.2f", acc);

            if (!accString.equals(oldAccString)) { // Accelerometer value has changed by .01g or more
                accTextView.setText("Acc: " + accString + " g");

                updateCSV();
                updateTimeBatteryTextViews();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d(TAG, "Sensor " + sensor.getType() + " changed accuracy to " + accuracy);
    }

    void updateTimeBatteryTextViews() {
        Date now = new Date();
        String time = timeSimple.format(now);
        timeTextView.setText(time);

        batteryPercentage = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        batteryTextView.setText("Bat: " + batteryPercentage + "%");
    }

    void initGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {
                        Log.d(TAG, "onConnected: " + connectionHint);
                        // Now you can use the Data Layer API
                    }
                    @Override
                    public void onConnectionSuspended(int cause) {
                        Log.d(TAG, "onConnectionSuspended: " + cause);
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult result) {
                        Log.d(TAG, "onConnectionFailed: " + result);
                    }
                })
                // Request access only to the Wearable API
                .addApi(Wearable.API)
                .build();
    }


    private void sendMessage(final String path, final String text) {
        new Thread( new Runnable() {
            @Override
            public void run() {
                try {
                    NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(googleApiClient).await();
                    Log.d(TAG, "n_nodes = " + nodes.getNodes().size());
                    for (Node node : nodes.getNodes()) {
                        Wearable.MessageApi.sendMessage(googleApiClient, node.getId(), path,
                                text.getBytes()).await();
                        Log.d(TAG, "message sent");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "sending failed");
                }
            }
        }).start();
    }

    void updateCSV() {
        Date now = new Date();
        String time = timeDetailed.format(now);
        String message = time + ", " + hrString + ", " + accString + ", " + batteryPercentage + "\n";

        sendMessage(fileName, message);
    }
}
