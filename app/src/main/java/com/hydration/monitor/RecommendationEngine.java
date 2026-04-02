package com.hydration.monitor;

public class RecommendationEngine {
    public Recommendation generateRecommendation(RiskLevel riskLevel) {
        switch (riskLevel) {
            case MODERATE:
                return new Recommendation(200, 20); // 150-250ml, 20 min
                
            case HIGH:
                return new Recommendation(325, 10); // 250-400ml, 10 min
                
            case EMERGENCY:
                return new Recommendation(500, 5); // 400-600ml, 5 min
                
            case NORMAL:
            default:
                return null;
        }
    }
}
