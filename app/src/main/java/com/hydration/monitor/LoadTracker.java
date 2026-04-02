package com.hydration.monitor;

public class LoadTracker {
    private static final double DECAY_FACTOR = 0.98;
    private static final long DECAY_INTERVAL_MS = 60000; // 1 minute
    
    private double cumulativeLoad = 0.0;
    private long lastUpdateTime;
    private long lastDecayTime;
    
    public LoadTracker() {
        lastUpdateTime = System.currentTimeMillis();
        lastDecayTime = System.currentTimeMillis();
    }
    
    public void update(double variance, ActivityState activityState, double activityWeight) {
        long currentTime = System.currentTimeMillis();
        double deltaTime = (currentTime - lastUpdateTime) / 1000.0; // seconds
        
        // Apply decay every minute
        long timeSinceDecay = currentTime - lastDecayTime;
        if (timeSinceDecay >= DECAY_INTERVAL_MS) {
            cumulativeLoad *= DECAY_FACTOR;
            lastDecayTime = currentTime;
        }
        
        // Accumulate load
        cumulativeLoad += variance * deltaTime * activityWeight;
        
        lastUpdateTime = currentTime;
    }
    
    public double getCumulativeLoad() {
        return cumulativeLoad;
    }
    
    public void setCumulativeLoad(double load) {
        this.cumulativeLoad = load;
    }
}
