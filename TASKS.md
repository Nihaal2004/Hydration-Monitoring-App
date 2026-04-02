# Hydration App Development Tasks

## Phase 1: Project Setup & Core Structure
- [x] Create project structure
- [ ] Set up Android project with Java
- [ ] Configure build.gradle with necessary dependencies
- [ ] Create base activity (MainActivity)

## Phase 2: Sensor Management
- [ ] Create SensorHandler class
- [ ] Implement accelerometer registration/unregistration
- [ ] Implement magnitude calculation (sqrt(ax² + ay² + az²))
- [ ] Create sliding window buffer (30-50 samples)
- [ ] Implement variance calculation over window

## Phase 3: Activity Classification
- [ ] Create ActivityClassifier class
- [ ] Implement variance threshold logic:
  - Idle: < 1.2
  - Walking: 1.2 - 2.5
  - Running: 2.5 - 4.0
  - Erratic: > 4.0
- [ ] Return activity state enum

## Phase 4: Cumulative Load Model
- [ ] Create LoadTracker class
- [ ] Implement cumulative load calculation
  - Formula: cumulativeLoad += variance × deltaTime × activityWeight
  - Activity weights: Idle=0.1, Walking=1.0, Running=2.5, Erratic=1.5
- [ ] Implement decay mechanism (0.98 per minute)
- [ ] Track time delta between updates

## Phase 5: Hydration Model
- [ ] Create HydrationModel class
- [ ] Implement state variables:
  - hydrationLevel (ml)
  - lastDrinkTime (timestamp)
  - lastDrinkAmount (ml)
- [ ] Implement hydration decay formula
  - Base decay: 0.5 ml/min
  - Activity decay: Idle=0, Walking=1.0, Running=3.0, Erratic=2.0
- [ ] Implement user input handling
- [ ] Cap hydration level at 1000 ml
- [ ] Clamp hydration level >= 0

## Phase 6: Risk Engine
- [ ] Create RiskEngine class
- [ ] Implement risk classification logic:
  - NORMAL: load < 50 AND hydration > 300
  - MODERATE: load 50-120 OR hydration 150-300
  - HIGH: load 120-200 OR hydration 50-150
  - EMERGENCY: load > 200 OR hydration < 50
- [ ] Return risk level enum

## Phase 7: Recommendation System
- [ ] Create RecommendationEngine class
- [ ] Implement suggestion logic:
  - MODERATE: 150-250 ml, 20 min deadline
  - HIGH: 250-400 ml, 10 min deadline
  - EMERGENCY: 400-600 ml, 5 min deadline
  - NORMAL: No suggestion
- [ ] Calculate deadline timestamps
- [ ] Format countdown display

## Phase 8: UI Layout (XML)
- [ ] Create activity_main.xml layout
- [ ] Add display TextViews:
  - Activity status
  - Variance value
  - Cumulative load
  - Hydration level
  - Last drink time
  - Risk level
  - Suggested intake
  - Deadline
- [ ] Add controls:
  - Start button
  - Stop button (initially disabled)
  - Water intake EditText
  - Log intake button
- [ ] Apply color scheme (Green/Yellow/Orange/Red)
- [ ] Implement modern Material-inspired design

## Phase 9: MainActivity Implementation
- [ ] Connect UI elements to code
- [ ] Implement Start button logic
  - Enable sensor tracking
  - Enable Stop button
  - Disable Start button
- [ ] Implement Stop button logic
  - Disable sensor tracking
  - Unregister sensors
  - Enable Start button
  - Disable Stop button
- [ ] Implement water intake logging
  - Update hydration level
  - Store timestamp
  - Refresh UI
- [ ] Implement periodic UI updates

## Phase 10: Lifecycle Management
- [ ] Implement state persistence using ViewModel or onSaveInstanceState
- [ ] Save state variables:
  - cumulativeLoad
  - hydrationLevel
  - lastDrinkTime
  - lastDrinkAmount
  - activity state
- [ ] Handle screen rotation
- [ ] Handle background/foreground transitions
- [ ] Prevent data reset on configuration changes

## Phase 11: Integration & Testing
- [ ] Integrate all components in MainActivity
- [ ] Test Scenario 1: User idle → Normal risk
- [ ] Test Scenario 2: Walking 10 min → Moderate risk
- [ ] Test Scenario 3: Running 20 min → High risk
- [ ] Test Scenario 4: Long activity + no hydration → Emergency
- [ ] Test Scenario 5: User logs water → Risk reduction
- [ ] Test screen rotation persistence
- [ ] Test app pause/resume

## Phase 12: Error Handling & Polish
- [ ] Handle missing sensors gracefully
- [ ] Prevent division by zero in variance
- [ ] Ensure no negative hydration values
- [ ] Prevent UI crashes during rapid updates
- [ ] Optimize performance (CPU, memory)
- [ ] Add smooth UI animations/transitions
- [ ] Final UI polish and testing

## Phase 13: Documentation
- [ ] Update README.md with:
  - App description
  - Features
  - Installation instructions
  - Usage guide
  - Technical details
- [ ] Add code comments where necessary
- [ ] Document formulas and thresholds
