# Hydration Monitoring App

A real-time wearable-style hydration assistant Android application that continuously monitors physical activity using accelerometer data and provides intelligent hydration recommendations.

## Features

### Core Functionality
- **Real-time Activity Monitoring**: Continuously reads accelerometer data to classify user activity (Idle, Walking, Running, Erratic)
- **Intelligent Hydration Tracking**: Models hydration depletion based on activity intensity and time
- **Cumulative Load Tracking**: Accumulates physical exertion over time with decay mechanism
- **Risk Assessment**: Classifies hydration risk into four levels (Normal, Moderate, High, Emergency)
- **Smart Recommendations**: Provides actionable hydration alerts with suggested intake amounts and deadlines
- **User Input**: Manual water intake logging with timestamp tracking

### Technical Features
- **Sensor Processing**: 
  - Accelerometer magnitude calculation: `sqrt(ax² + ay² + az²)`
  - Sliding window variance calculation (40 samples)
  - Activity classification based on variance thresholds

- **Activity Classification**:
  - Idle: variance < 1.2
  - Walking: variance 1.2 - 2.5
  - Running: variance 2.5 - 4.0
  - Erratic: variance > 4.0

- **Hydration Model**:
  - Base decay: 0.5 ml/min
  - Activity-based decay: varies by activity intensity
  - Maximum capacity: 1000 ml
  - Real-time decay calculation

- **Risk Engine**:
  - Normal: load < 50 AND hydration > 300
  - Moderate: load 50-120 OR hydration 150-300
  - High: load 120-200 OR hydration 50-150
  - Emergency: load > 200 OR hydration < 50

- **Recommendations**:
  - Moderate: 150-250 ml within 20 minutes
  - High: 250-400 ml within 10 minutes
  - Emergency: 400-600 ml within 5 minutes (immediate)

### UI Features
- Modern Material-inspired design
- Color-coded risk levels (Green/Yellow/Orange/Red)
- Real-time data updates
- Comprehensive dashboard showing:
  - Current activity state
  - Variance metrics
  - Cumulative load
  - Hydration level
  - Last drink timestamp
  - Risk assessment
  - Suggested intake
  - Countdown deadline

### Lifecycle Management
- State persistence across screen rotations
- Proper handling of background/foreground transitions
- Efficient sensor management
- No data loss during configuration changes

## Architecture

### Modular Design
- **SensorHandler**: Manages accelerometer data collection and variance calculation
- **ActivityClassifier**: Classifies motion into activity states
- **LoadTracker**: Tracks cumulative physical exertion with time-decay
- **HydrationModel**: Models hydration levels with activity-based decay
- **RiskEngine**: Assesses hydration risk based on load and hydration
- **RecommendationEngine**: Generates contextual hydration recommendations
- **MainActivity**: Orchestrates all components and manages UI

## Requirements

- **Platform**: Android 7.0 (API 24) or higher
- **Sensors**: Accelerometer (required)
- **Permissions**: None required (uses only built-in sensors)
- **Language**: Java
- **UI Framework**: XML layouts (Material Design inspired)

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/Nihaal2004/Hydration-Monitoring-App.git
   ```

2. Open the project in Android Studio

3. Build and run on an Android device with accelerometer support

## Usage

1. **Start Monitoring**: Tap the "Start Monitoring" button to begin activity tracking
2. **View Real-time Data**: Monitor your activity level, hydration status, and risk level
3. **Log Water Intake**: Enter the amount of water consumed (in ml) and tap "Log Water Intake"
4. **Follow Recommendations**: Act on hydration suggestions based on your risk level
5. **Stop Monitoring**: Tap "Stop Monitoring" to pause tracking

## Technical Details

### Formulas

**Cumulative Load Calculation**:
```
cumulativeLoad += variance × deltaTime × activityWeight
```
Where activityWeight is:
- Idle: 0.1
- Walking: 1.0
- Running: 2.5
- Erratic: 1.5

**Load Decay**:
```
cumulativeLoad *= 0.98 (every minute)
```

**Hydration Decay**:
```
hydrationLevel -= (baseDecay + activityDecay) × deltaTime
```
Where:
- baseDecay = 0.5 ml/min
- activityDecay varies by activity (0-3.0 ml/min)

### Performance
- Efficient sensor sampling at SENSOR_DELAY_NORMAL (~10 Hz)
- Smooth UI updates at 1 second intervals
- Minimal battery impact
- No memory leaks

## Development

### Project Structure
```
app/
├── src/main/
│   ├── java/com/hydration/monitor/
│   │   ├── MainActivity.java
│   │   ├── SensorHandler.java
│   │   ├── ActivityClassifier.java
│   │   ├── LoadTracker.java
│   │   ├── HydrationModel.java
│   │   ├── RiskEngine.java
│   │   ├── RecommendationEngine.java
│   │   ├── ActivityState.java
│   │   ├── RiskLevel.java
│   │   └── Recommendation.java
│   ├── res/
│   │   ├── layout/activity_main.xml
│   │   ├── values/colors.xml
│   │   ├── values/strings.xml
│   │   └── values/styles.xml
│   └── AndroidManifest.xml
└── build.gradle
```

### Testing Scenarios

1. **Idle User**: Minimal movement → Normal risk
2. **Walking 10 minutes**: Moderate load → Moderate risk → 200ml suggestion in 20 min
3. **Running 20 minutes**: High load → High risk → 325ml suggestion in 10 min
4. **Extended Activity**: Long activity without hydration → Emergency → 500ml immediate
5. **Hydration Response**: Logging water → Risk level reduction

## Error Handling

- Graceful handling of missing sensors
- Prevention of division by zero in calculations
- Hydration level clamping (0-1000 ml)
- UI crash prevention during rapid updates
- Input validation for water intake

## Future Enhancements

- Historical data tracking and charts
- Customizable thresholds
- Notification system for alerts
- Integration with fitness trackers
- Weather-based adjustments
- Multi-user support

## License

This project is open source and available under the MIT License.

## Author

Nihaal2004

## Acknowledgments

Built as a comprehensive Android application demonstrating sensor integration, real-time data processing, and intelligent health monitoring.
