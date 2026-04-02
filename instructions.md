Design and implement a complete Android application (Java, XML-based UI, no Jetpack Compose) that functions as a smartphone-based wearable hydration monitoring system. The app must run fully on-device using built-in sensors and must be robust, user-friendly, and visually modern. The system should estimate hydration needs using motion sensor data, cumulative activity load, time-based decay, and user hydration input, and generate actionable alerts with suggested intake amounts and deadlines.

---

APPLICATION OBJECTIVE:

Create a real-time wearable-style hydration assistant that:

1. Continuously reads accelerometer data
2. Converts motion into meaningful activity metrics
3. Tracks cumulative physical exertion over time
4. Maintains a hydration model based on user input and decay
5. Classifies hydration risk into levels
6. Provides clear recommendations (amount + deadline)
7. Maintains stability across lifecycle events (rotation, pause/resume)

---

TECHNICAL REQUIREMENTS:

* Platform: Android (Java)
* UI: XML layouts (modern Material-inspired design)
* Sensors: Accelerometer
* Architecture: Modular classes (separation of concerns)
* No external APIs or cloud dependency
* Must handle lifecycle events (rotation, backgrounding)

---

CORE PROCESSING PIPELINE:

Sensor Data â†’ Magnitude â†’ Variance â†’ Activity Classification
â†’ Cumulative Load (time-integrated variance)
â†’ Hydration Model (user input + decay)
â†’ Risk Engine
â†’ Alert + Recommendation Output

---

SENSOR HANDLING:

* Use SensorManager with TYPE_ACCELEROMETER

* Sampling rate: SENSOR_DELAY_NORMAL (~10 Hz)

* Compute magnitude:
  magnitude = sqrt(axÂ² + ayÂ² + azÂ²)

* Maintain sliding window (30â€“50 samples)

* Compute variance over this window

---

FEATURE EXTRACTION:

Variance thresholds:

* < 1.2 â†’ Idle
* 1.2 â€“ 2.5 â†’ Walking
* 2.5 â€“ 4.0 â†’ Running
* > 4.0 â†’ Erratic movement

---

ACTIVITY CLASSIFICATION:

* Idle: minimal movement
* Walking: moderate variance
* Running: high variance
* Erratic: irregular spikes

Activity must gate further logic:

* If Idle â†’ do not accumulate load significantly

---

CUMULATIVE LOAD MODEL:

cumulativeLoad += variance Ã— deltaTime Ã— activityWeight

Where:

* deltaTime = seconds since last update
* activityWeight:
  Idle = 0.1
  Walking = 1.0
  Running = 2.5
  Erratic = 1.5

Decay:

* Every minute: cumulativeLoad *= 0.98

Purpose:

* Represents accumulated exertion over time

---

HYDRATION MODEL:

State variables:

* hydrationLevel (ml-equivalent)
* lastDrinkTime (timestamp)
* lastDrinkAmount (ml)

Initialization:
hydrationLevel = lastDrinkAmount

---

HYDRATION DECAY:

hydrationLevel -= (baseDecay + activityDecay) Ã— deltaTime

Where:

* baseDecay = 0.5 ml/min
* activityDecay:
  Idle = 0
  Walking = 1.0 ml/min
  Running = 3.0 ml/min
  Erratic = 2.0 ml/min

Clamp:
hydrationLevel â‰¥ 0

---

USER HYDRATION INPUT:

UI must allow:

* Enter amount (ml)
* Button: â€œLog Water Intakeâ€

On input:

* lastDrinkTime = current time
* hydrationLevel += inputAmount
* Cap hydrationLevel at 1000 ml

Display:

* Last drink time (formatted)
* Total hydration level

---

RISK ENGINE:

Combine:

* cumulativeLoad
* hydrationLevel
* time since last intake

Thresholds:

NORMAL:

* cumulativeLoad < 50 AND hydrationLevel > 300

MODERATE:

* cumulativeLoad 50â€“120 OR hydrationLevel 150â€“300

HIGH:

* cumulativeLoad 120â€“200 OR hydrationLevel 50â€“150

EMERGENCY:

* cumulativeLoad > 200 OR hydrationLevel < 50

---

HYDRATION RECOMMENDATIONS:

MODERATE:

* Suggest: 150â€“250 ml
* Deadline: 20 minutes

HIGH:

* Suggest: 250â€“400 ml
* Deadline: 10 minutes

EMERGENCY:

* Suggest: 400â€“600 ml
* Deadline: 5 minutes (immediate)

NORMAL:

* No suggestion

---

DEADLINE LOGIC:

deadline = currentTime + urgencyWindow

Where:

* Moderate: +20 min
* High: +10 min
* Emergency: +5 min

Display countdown if possible

---

UI DESIGN REQUIREMENTS:

* Must be visually modern, bright, and engaging
* Use vibrant colors:
  Green â†’ Normal
  Yellow â†’ Moderate
  Orange â†’ High
  Red â†’ Emergency

Main screen must display:

* Activity (Idle / Walking / Running)
* Variance
* Cumulative Load
* Hydration Level (ml)
* Last Drink Time
* Risk Level
* Suggested Intake (ml)
* Deadline

Controls:

* Start button
* Stop button
* Hydration input (ml)
* Log intake button

---

UI BEHAVIOR:

* Start button:
  Enables tracking
* Stop button:
  Must NOT function unless Start was pressed
  Should be disabled initially
* After Stop:
  Sensors must unregister

---

LIFECYCLE & DURABILITY:

App must handle:

* Screen rotation (portrait â†” landscape)
* Background â†’ foreground transitions

State must persist:

* cumulativeLoad
* hydrationLevel
* lastDrinkTime
* lastDrinkAmount
* activity state

Use:

* onSaveInstanceState OR ViewModel (preferred)

No data reset on rotation

---

PERFORMANCE CONSTRAINTS:

* Efficient sensor usage
* No excessive CPU usage
* Avoid memory leaks
* Maintain smooth UI updates

---

EXPECTED BEHAVIOR SCENARIOS:

Scenario 1:
User idle
â†’ Activity: Idle
â†’ Risk: Normal

Scenario 2:
Walking 10 minutes
â†’ Moderate load
â†’ Risk: Moderate
â†’ Suggest 200 ml in 20 minutes

Scenario 3:
Running 20 minutes
â†’ High load
â†’ Risk: High
â†’ Suggest 300 ml in 10 minutes

Scenario 4:
Long activity + no hydration
â†’ Risk: Emergency
â†’ Suggest 500 ml immediately

Scenario 5:
User logs water
â†’ hydrationLevel increases
â†’ risk reduces accordingly

---

ERROR HANDLING:

* Handle missing sensors gracefully
* Prevent division by zero in variance
* Ensure no negative hydration values
* Prevent UI crashes during rapid updates

---

FINAL SYSTEM DESCRIPTION:

The application acts as a wearable hydration assistant that continuously monitors movement intensity using accelerometer data, accumulates exertion over time, models hydration depletion dynamically, and provides context-aware hydration alerts with actionable recommendations and deadlines, all while maintaining a robust and visually engaging user experience.

---