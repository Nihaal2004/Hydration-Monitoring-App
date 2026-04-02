package com.hydration.monitor;

public class Recommendation {
    public int suggestedIntakeMl;
    public long deadlineTimestamp;
    public int deadlineMinutes;
    
    public Recommendation(int suggestedIntakeMl, int deadlineMinutes) {
        this.suggestedIntakeMl = suggestedIntakeMl;
        this.deadlineMinutes = deadlineMinutes;
        this.deadlineTimestamp = System.currentTimeMillis() + (deadlineMinutes * 60 * 1000L);
    }
}
