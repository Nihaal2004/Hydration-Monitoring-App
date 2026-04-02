package com.hydration.monitor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import java.util.ArrayList;
import java.util.List;

public class SensorHandler implements SensorEventListener {
    private static final int WINDOW_SIZE = 40;
    
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private List<Float> magnitudeWindow;
    private SensorDataListener listener;
    
    public interface SensorDataListener {
        void onVarianceCalculated(double variance);
    }
    
    public SensorHandler(Context context, SensorDataListener listener) {
        this.listener = listener;
        this.magnitudeWindow = new ArrayList<>();
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
    }
    
    public boolean isAccelerometerAvailable() {
        return accelerometer != null;
    }
    
    public void start() {
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
    
    public void stop() {
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
        magnitudeWindow.clear();
    }
    
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            
            float magnitude = (float) Math.sqrt(x * x + y * y + z * z);
            
            magnitudeWindow.add(magnitude);
            if (magnitudeWindow.size() > WINDOW_SIZE) {
                magnitudeWindow.remove(0);
            }
            
            if (magnitudeWindow.size() >= WINDOW_SIZE) {
                double variance = calculateVariance();
                if (listener != null) {
                    listener.onVarianceCalculated(variance);
                }
            }
        }
    }
    
    private double calculateVariance() {
        if (magnitudeWindow.isEmpty()) {
            return 0.0;
        }
        
        double sum = 0.0;
        for (float val : magnitudeWindow) {
            sum += val;
        }
        double mean = sum / magnitudeWindow.size();
        
        double variance = 0.0;
        for (float val : magnitudeWindow) {
            variance += Math.pow(val - mean, 2);
        }
        
        return variance / magnitudeWindow.size();
    }
    
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not needed for this implementation
    }
}
