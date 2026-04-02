package com.hydration.monitor;

public class RiskEngine {
    public RiskLevel assessRisk(double cumulativeLoad, double hydrationLevel) {
        // EMERGENCY: load > 200 OR hydration < 50
        if (cumulativeLoad > 200 || hydrationLevel < 50) {
            return RiskLevel.EMERGENCY;
        }
        
        // HIGH: load 120-200 OR hydration 50-150
        if ((cumulativeLoad >= 120 && cumulativeLoad <= 200) || 
            (hydrationLevel >= 50 && hydrationLevel < 150)) {
            return RiskLevel.HIGH;
        }
        
        // MODERATE: load 50-120 OR hydration 150-300
        if ((cumulativeLoad >= 50 && cumulativeLoad < 120) || 
            (hydrationLevel >= 150 && hydrationLevel < 300)) {
            return RiskLevel.MODERATE;
        }
        
        // NORMAL: load < 50 AND hydration > 300
        return RiskLevel.NORMAL;
    }
}
