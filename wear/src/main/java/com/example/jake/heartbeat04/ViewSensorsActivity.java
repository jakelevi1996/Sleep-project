package com.example.jake.heartbeat04;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Locale;

public class ViewSensorsActivity extends WearableActivity implements SensorEventListener {
    static final String TAG = ViewSensorsActivity.class.getName();

    private SensorManager sensorManager;
    private Sensor heartRateSensor;
    private Sensor accelerometerSensor;

    private ToggleButton hrButton;
    private ToggleButton accButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_sensors);
        setAmbientEnabled();

        Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();

        hrButton = (ToggleButton) findViewById(R.id.hr_button);
        accButton = (ToggleButton) findViewById(R.id.acc_button);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        toggleSensor(heartRateSensor, true);
        toggleSensor(accelerometerSensor, true);
    }

    void toggleSensor(Sensor sensor, boolean switchOn) {
        if (switchOn) sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        else sensorManager.unregisterListener(this, sensor);
    }

    @Override
    protected void onPause() {
        super.onPause();

        toggleSensor(heartRateSensor, false);
        toggleSensor(accelerometerSensor, false);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (hrButton.isChecked()) toggleSensor(heartRateSensor, true);
        if (accButton.isChecked())toggleSensor(accelerometerSensor, true);
    }

    public void hrButtonClicked(View view) {
        boolean buttonState = ((ToggleButton) view).isChecked();
        toggleSensor(heartRateSensor, buttonState);
    }

    public void accButtonClicked(View view) {
        boolean buttonState = ((ToggleButton) view).isChecked();
        toggleSensor(accelerometerSensor, buttonState);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_HEART_RATE) {
            float heartRate = event.values[0];

            String msg = "HR: " + heartRate + " bpm";
            hrButton.setText(msg);
        }
        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            float x = event.values[0], y = event.values[1], z = event.values[2];
            double acc = Math.sqrt(x*x + y*y + z*z) / 9.81;

            String msg = "Acc: " + String.format(Locale.UK, "%.2f", acc) + " g";
            accButton.setText(msg);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d(TAG, "Sensor " + sensor.getType() + " changed accuracy to " + accuracy);
    }
}
