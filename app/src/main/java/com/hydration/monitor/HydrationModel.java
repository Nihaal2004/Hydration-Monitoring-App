package com.hydration.monitor;

public class HydrationModel {
    private static final double BASE_DECAY_PER_MIN = 0.5;
    private static final int MAX_HYDRATION_ML = 1000;
    
    private double hydrationLevel = 0.0;
    private long lastDrinkTime = 0;
    private int lastDrinkAmount = 0;
    private long lastUpdateTime;
    
    public HydrationModel() {
        lastUpdateTime = System.currentTimeMillis();
    }
    
    public void update(ActivityState activityState) {
        long currentTime = System.currentTimeMillis();
        double deltaMinutes = (currentTime - lastUpdateTime) / 60000.0;
        
        double activityDecay = getActivityDecay(activityState);
        double totalDecay = (BASE_DECAY_PER_MIN + activityDecay) * deltaMinutes;
        
        hydrationLevel -= totalDecay;
        
        if (hydrationLevel < 0) {
            hydrationLevel = 0;
        }
        
        lastUpdateTime = currentTime;
    }
    
    private double getActivityDecay(ActivityState state) {
        switch (state) {
            case IDLE:
                return 0.0;
            case WALKING:
                return 1.0;
            case RUNNING:
                return 3.0;
            case ERRATIC:
                return 2.0;
            default:
                return 0.0;
        }
    }
    
    public void logWaterIntake(int amountMl) {
        lastDrinkAmount = amountMl;
        lastDrinkTime = System.currentTimeMillis();
        hydrationLevel += amountMl;
        
        if (hydrationLevel > MAX_HYDRATION_ML) {
            hydrationLevel = MAX_HYDRATION_ML;
        }
    }
    
    public double getHydrationLevel() {
        return hydrationLevel;
    }
    
    public long getLastDrinkTime() {
        return lastDrinkTime;
    }
    
    public int getLastDrinkAmount() {
        return lastDrinkAmount;
    }
    
    public void setHydrationLevel(double level) {
        this.hydrationLevel = level;
    }
    
    public void setLastDrinkTime(long time) {
        this.lastDrinkTime = time;
    }
    
    public void setLastDrinkAmount(int amount) {
        this.lastDrinkAmount = amount;
    }
}
