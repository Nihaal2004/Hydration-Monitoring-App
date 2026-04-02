# Hydration App Development Tasks

## Phase 1: Project Setup & Core Structure
- [x] Create project structure
- [x] Set up Android project with Java
- [x] Configure build.gradle with necessary dependencies
- [x] Create base activity (MainActivity)

## Phase 2: Sensor Management
- [x] Create SensorHandler class
- [x] Implement accelerometer registration/unregistration
- [x] Implement magnitude calculation (sqrt(ax² + ay² + az²))
- [x] Create sliding window buffer (30-50 samples)
- [x] Implement variance calculation over window

## Phase 3: Activity Classification
- [x] Create ActivityClassifier class
- [x] Implement variance threshold logic:
  - Idle: < 1.2
  - Walking: 1.2 - 2.5
  - Running: 2.5 - 4.0
  - Erratic: > 4.0
- [x] Return activity state enum

## Phase 4: Cumulative Load Model
- [x] Create LoadTracker class
- [x] Implement cumulative load calculation
  - Formula: cumulativeLoad += variance × deltaTime × activityWeight
  - Activity weights: Idle=0.1, Walking=1.0, Running=2.5, Erratic=1.5
- [x] Implement decay mechanism (0.98 per minute)
- [x] Track time delta between updates

## Phase 5: Hydration Model
- [x] Create HydrationModel class
- [x] Implement state variables:
  - hydrationLevel (ml)
  - lastDrinkTime (timestamp)
  - lastDrinkAmount (ml)
- [x] Implement hydration decay formula
  - Base decay: 0.5 ml/min
  - Activity decay: Idle=0, Walking=1.0, Running=3.0, Erratic=2.0
- [x] Implement user input handling
- [x] Cap hydration level at 1000 ml
- [x] Clamp hydration level >= 0

## Phase 6: Risk Engine
- [x] Create RiskEngine class
- [x] Implement risk classification logic:
  - NORMAL: load < 50 AND hydration > 300
  - MODERATE: load 50-120 OR hydration 150-300
  - HIGH: load 120-200 OR hydration 50-150
  - EMERGENCY: load > 200 OR hydration < 50
- [x] Return risk level enum

## Phase 7: Recommendation System
- [x] Create RecommendationEngine class
- [x] Implement suggestion logic:
  - MODERATE: 150-250 ml, 20 min deadline
  - HIGH: 250-400 ml, 10 min deadline
  - EMERGENCY: 400-600 ml, 5 min deadline
  - NORMAL: No suggestion
- [x] Calculate deadline timestamps
- [x] Format countdown display

## Phase 8: UI Layout (XML)
- [x] Create activity_main.xml layout
- [x] Add display TextViews:
  - Activity status
  - Variance value
  - Cumulative load
  - Hydration level
  - Last drink time
  - Risk level
  - Suggested intake
  - Deadline
- [x] Add controls:
  - Start button
  - Stop button (initially disabled)
  - Water intake EditText
  - Log intake button
- [x] Apply color scheme (Green/Yellow/Orange/Red)
- [x] Implement modern Material-inspired design

## Phase 9: MainActivity Implementation
- [x] Connect UI elements to code
- [x] Implement Start button logic
  - Enable sensor tracking
  - Enable Stop button
  - Disable Start button
- [x] Implement Stop button logic
  - Disable sensor tracking
  - Unregister sensors
  - Enable Start button
  - Disable Stop button
- [x] Implement water intake logging
  - Update hydration level
  - Store timestamp
  - Refresh UI
- [x] Implement periodic UI updates

## Phase 10: Lifecycle Management
- [x] Implement state persistence using ViewModel or onSaveInstanceState
- [x] Save state variables:
  - cumulativeLoad
  - hydrationLevel
  - lastDrinkTime
  - lastDrinkAmount
  - activity state
- [x] Handle screen rotation
- [x] Handle background/foreground transitions
- [x] Prevent data reset on configuration changes

## Phase 11: Integration & Testing
- [x] Integrate all components in MainActivity
- [ ] Test Scenario 1: User idle → Normal risk
- [ ] Test Scenario 2: Walking 10 min → Moderate risk
- [ ] Test Scenario 3: Running 20 min → High risk
- [ ] Test Scenario 4: Long activity + no hydration → Emergency
- [ ] Test Scenario 5: User logs water → Risk reduction
- [ ] Test screen rotation persistence
- [ ] Test app pause/resume

## Phase 12: Error Handling & Polish
- [x] Handle missing sensors gracefully
- [x] Prevent division by zero in variance
- [x] Ensure no negative hydration values
- [x] Prevent UI crashes during rapid updates
- [x] Optimize performance (CPU, memory)
- [ ] Add smooth UI animations/transitions
- [ ] Final UI polish and testing

## Phase 13: Documentation
- [x] Update README.md with:
  - App description
  - Features
  - Installation instructions
  - Usage guide
  - Technical details
- [x] Add code comments where necessary
- [x] Document formulas and thresholds

## Status Summary
✅ **Completed**: Core implementation finished
- All 9 Java classes created and implemented
- Complete UI layout with Material Design
- Full lifecycle management
- State persistence
- Error handling

🔄 **Next Steps**: 
- Physical device testing
- UI polish and animations
- Build and deploy APK
