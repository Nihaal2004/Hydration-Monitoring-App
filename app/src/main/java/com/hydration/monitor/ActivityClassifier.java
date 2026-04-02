package com.hydration.monitor;

public class ActivityClassifier {
    private static final double IDLE_THRESHOLD = 1.2;
    private static final double WALKING_THRESHOLD = 2.5;
    private static final double RUNNING_THRESHOLD = 4.0;
    
    public ActivityState classify(double variance) {
        if (variance < IDLE_THRESHOLD) {
            return ActivityState.IDLE;
        } else if (variance < WALKING_THRESHOLD) {
            return ActivityState.WALKING;
        } else if (variance < RUNNING_THRESHOLD) {
            return ActivityState.RUNNING;
        } else {
            return ActivityState.ERRATIC;
        }
    }
    
    public double getActivityWeight(ActivityState state) {
        switch (state) {
            case IDLE:
                return 0.1;
            case WALKING:
                return 1.0;
            case RUNNING:
                return 2.5;
            case ERRATIC:
                return 1.5;
            default:
                return 0.1;
        }
    }
}
