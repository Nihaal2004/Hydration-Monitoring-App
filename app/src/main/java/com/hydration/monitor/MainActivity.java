package com.hydration.monitor;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android:widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SensorHandler.SensorDataListener {
    
    private SensorHandler sensorHandler;
    private ActivityClassifier activityClassifier;
    private LoadTracker loadTracker;
    private HydrationModel hydrationModel;
    private RiskEngine riskEngine;
    private RecommendationEngine recommendationEngine;
    
    private TextView tvActivity, tvVariance, tvLoad, tvHydration;
    private TextView tvLastDrink, tvRisk, tvSuggestion, tvDeadline;
    private Button btnStart, btnStop, btnLogIntake;
    private EditText etWaterAmount;
    private LinearLayout cardRisk;
    
    private Handler updateHandler;
    private Runnable updateRunnable;
    
    private boolean isTracking = false;
    private double currentVariance = 0.0;
    private ActivityState currentActivity = ActivityState.IDLE;
    
    private static final String KEY_LOAD = "cumulative_load";
    private static final String KEY_HYDRATION = "hydration_level";
    private static final String KEY_LAST_DRINK_TIME = "last_drink_time";
    private static final String KEY_LAST_DRINK_AMOUNT = "last_drink_amount";
    private static final String KEY_IS_TRACKING = "is_tracking";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initializeComponents();
        initializeUI();
        restoreState(savedInstanceState);
        setupUpdateHandler();
    }
    
    private void initializeComponents() {
        sensorHandler = new SensorHandler(this, this);
        activityClassifier = new ActivityClassifier();
        loadTracker = new LoadTracker();
        hydrationModel = new HydrationModel();
        riskEngine = new RiskEngine();
        recommendationEngine = new RecommendationEngine();
    }
    
    private void initializeUI() {
        tvActivity = findViewById(R.id.tvActivity);
        tvVariance = findViewById(R.id.tvVariance);
        tvLoad = findViewById(R.id.tvLoad);
        tvHydration = findViewById(R.id.tvHydration);
        tvLastDrink = findViewById(R.id.tvLastDrink);
        tvRisk = findViewById(R.id.tvRisk);
        tvSuggestion = findViewById(R.id.tvSuggestion);
        tvDeadline = findViewById(R.id.tvDeadline);
        
        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);
        btnLogIntake = findViewById(R.id.btnLogIntake);
        etWaterAmount = findViewById(R.id.etWaterAmount);
        cardRisk = findViewById(R.id.cardRisk);
        
        btnStart.setOnClickListener(v -> startTracking());
        btnStop.setOnClickListener(v -> stopTracking());
        btnLogIntake.setOnClickListener(v -> logWaterIntake());
        
        if (!sensorHandler.isAccelerometerAvailable()) {
            Toast.makeText(this, R.string.sensor_unavailable, Toast.LENGTH_LONG).show();
            btnStart.setEnabled(false);
        }
    }
    
    private void setupUpdateHandler() {
        updateHandler = new Handler();
        updateRunnable = new Runnable() {
            @Override
            public void run() {
                if (isTracking) {
                    updateModels();
                    updateUI();
                    updateHandler.postDelayed(this, 1000);
                }
            }
        };
    }
    
    private void startTracking() {
        if (!isTracking) {
            isTracking = true;
            sensorHandler.start();
            btnStart.setEnabled(false);
            btnStop.setEnabled(true);
            updateHandler.post(updateRunnable);
            Toast.makeText(this, "Monitoring started", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void stopTracking() {
        if (isTracking) {
            isTracking = false;
            sensorHandler.stop();
            btnStart.setEnabled(true);
            btnStop.setEnabled(false);
            updateHandler.removeCallbacks(updateRunnable);
            Toast.makeText(this, "Monitoring stopped", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void logWaterIntake() {
        String amountStr = etWaterAmount.getText().toString().trim();
        if (amountStr.isEmpty()) {
            Toast.makeText(this, "Please enter water amount", Toast.LENGTH_SHORT).show();
            return;
        }
        
        try {
            int amount = Integer.parseInt(amountStr);
            if (amount <= 0) {
                Toast.makeText(this, "Amount must be positive", Toast.LENGTH_SHORT).show();
                return;
            }
            
            hydrationModel.logWaterIntake(amount);
            etWaterAmount.setText("");
            updateUI();
            Toast.makeText(this, "Logged " + amount + " ml", Toast.LENGTH_SHORT).show();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    public void onVarianceCalculated(double variance) {
        currentVariance = variance;
        currentActivity = activityClassifier.classify(variance);
    }
    
    private void updateModels() {
        double activityWeight = activityClassifier.getActivityWeight(currentActivity);
        loadTracker.update(currentVariance, currentActivity, activityWeight);
        hydrationModel.update(currentActivity);
    }
    
    private void updateUI() {
        tvActivity.setText(currentActivity.toString());
        tvVariance.setText(String.format(Locale.US, "%.2f", currentVariance));
        tvLoad.setText(String.format(Locale.US, "%.2f", loadTracker.getCumulativeLoad()));
        tvHydration.setText(String.format(Locale.US, "%.0f ml", hydrationModel.getHydrationLevel()));
        
        long lastDrinkTime = hydrationModel.getLastDrinkTime();
        if (lastDrinkTime > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.US);
            tvLastDrink.setText(sdf.format(new Date(lastDrinkTime)));
        } else {
            tvLastDrink.setText(R.string.never);
        }
        
        RiskLevel riskLevel = riskEngine.assessRisk(
            loadTracker.getCumulativeLoad(), 
            hydrationModel.getHydrationLevel()
        );
        
        updateRiskDisplay(riskLevel);
        
        Recommendation recommendation = recommendationEngine.generateRecommendation(riskLevel);
        if (recommendation != null) {
            tvSuggestion.setText(String.format(Locale.US, "%d ml", recommendation.suggestedIntakeMl));
            
            long timeRemaining = recommendation.deadlineTimestamp - System.currentTimeMillis();
            long minutesRemaining = timeRemaining / 60000;
            if (minutesRemaining > 0) {
                tvDeadline.setText(String.format(Locale.US, "%d minutes", minutesRemaining));
            } else {
                tvDeadline.setText("NOW!");
            }
        } else {
            tvSuggestion.setText(R.string.no_suggestion);
            tvDeadline.setText(R.string.no_suggestion);
        }
    }
    
    private void updateRiskDisplay(RiskLevel riskLevel) {
        tvRisk.setText(riskLevel.toString());
        
        int color;
        switch (riskLevel) {
            case NORMAL:
                color = getResources().getColor(R.color.risk_normal, null);
                break;
            case MODERATE:
                color = getResources().getColor(R.color.risk_moderate, null);
                break;
            case HIGH:
                color = getResources().getColor(R.color.risk_high, null);
                break;
            case EMERGENCY:
                color = getResources().getColor(R.color.risk_emergency, null);
                break;
            default:
                color = getResources().getColor(R.color.risk_normal, null);
        }
        
        tvRisk.setTextColor(color);
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putDouble(KEY_LOAD, loadTracker.getCumulativeLoad());
        outState.putDouble(KEY_HYDRATION, hydrationModel.getHydrationLevel());
        outState.putLong(KEY_LAST_DRINK_TIME, hydrationModel.getLastDrinkTime());
        outState.putInt(KEY_LAST_DRINK_AMOUNT, hydrationModel.getLastDrinkAmount());
        outState.putBoolean(KEY_IS_TRACKING, isTracking);
    }
    
    private void restoreState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            loadTracker.setCumulativeLoad(savedInstanceState.getDouble(KEY_LOAD, 0.0));
            hydrationModel.setHydrationLevel(savedInstanceState.getDouble(KEY_HYDRATION, 0.0));
            hydrationModel.setLastDrinkTime(savedInstanceState.getLong(KEY_LAST_DRINK_TIME, 0));
            hydrationModel.setLastDrinkAmount(savedInstanceState.getInt(KEY_LAST_DRINK_AMOUNT, 0));
            
            boolean wasTracking = savedInstanceState.getBoolean(KEY_IS_TRACKING, false);
            if (wasTracking) {
                startTracking();
            }
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        if (isTracking) {
            updateHandler.removeCallbacks(updateRunnable);
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        if (isTracking) {
            updateHandler.post(updateRunnable);
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isTracking) {
            sensorHandler.stop();
        }
        updateHandler.removeCallbacks(updateRunnable);
    }
}
