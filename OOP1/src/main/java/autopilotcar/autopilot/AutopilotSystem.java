package autopilotcar.autopilot;

import autopilotcar.model.Brake;
import autopilotcar.model.Engine;
import autopilotcar.model.GearBox;
import autopilotcar.model.GearBoxType;

import java.util.List;
import java.util.Random;

// Mengelola logika utama autopilot berdasarkan sensor dan aktuator kendaraan.
public class AutopilotSystem {
    private static final float DEFAULT_CRUISE_SPEED = 50.0f;
    private static final float EMERGENCY_DECELERATION_STEP = 5.0f;
    private static final float NORMAL_DETECTION_CHANCE = 0.08f;

    private boolean active;
    private float targetSpeed;
    private float minSafeDistance;
    private AutopilotMode mode;
    private final ObjectDetector objectDetector;
    private final SpeedSensor speedSensor;
    private final GearBox gearBox;
    private final Brake brake;
    private final Engine engine;
    private final Random random;

    public AutopilotSystem(float targetSpeed, float minSafeDistance, ObjectDetector objectDetector,
                           SpeedSensor speedSensor, GearBox gearBox, Brake brake, Engine engine) {
        this.active = false;
        this.targetSpeed = targetSpeed;
        this.minSafeDistance = minSafeDistance;
        this.mode = AutopilotMode.MANUAL_OVERRIDE;
        this.objectDetector = objectDetector;
        this.speedSensor = speedSensor;
        this.gearBox = gearBox;
        this.brake = brake;
        this.engine = engine;
        this.random = new Random();
    }

    public boolean isActive() {
        return active;
    }

    public float getTargetSpeed() {
        return targetSpeed;
    }

    public void setTargetSpeed(float targetSpeed) {
        this.targetSpeed = targetSpeed;
    }

    public float getMinSafeDistance() {
        return minSafeDistance;
    }

    public void setMinSafeDistance(float minSafeDistance) {
        this.minSafeDistance = minSafeDistance;
    }

    public AutopilotMode getMode() {
        return mode;
    }

    public float getCurrentSpeed() {
        return speedSensor.getCurrentSpeed();
    }

    public void activate() {
        active = true;
        targetSpeed = DEFAULT_CRUISE_SPEED;
        objectDetector.initialize();
        speedSensor.initialize();
        changeMode(AutopilotMode.CRUISE_CONTROL);
        System.out.println("Autopilot system activated.");
    }

    public void deactivate() {
        active = false;
        changeMode(AutopilotMode.MANUAL_OVERRIDE);
        System.out.println("Autopilot system deactivated.");
    }

    public void update() {
        update(shouldRunNormalDetection(), false);
    }

    public void simulateObjectDetection() {
        update(true, true);
    }

    private void update(boolean scanForObstacles, boolean frequentScan) {
        if (!active) {
            System.out.println("Autopilot is inactive. Update skipped.");
            return;
        }

        float currentSpeed = (Float) speedSensor.readData();
        if (scanForObstacles) {
            if (frequentScan) {
                objectDetector.readFrequentData();
            } else {
                objectDetector.readData();
            }
        }

        if (speedSensor.isOverLimit()) {
            changeMode(AutopilotMode.EMERGENCY_STOP);
            System.out.println("Speed exceeds maximum limit. Triggering emergency response.");
            gradualEmergencyStop();
            deactivate();
            return;
        }

        if (!scanForObstacles && mode == AutopilotMode.COLLISION_AVOIDANCE) {
            changeMode(AutopilotMode.CRUISE_CONTROL);
        }

        if (mode == AutopilotMode.CRUISE_CONTROL) {
            applyCruiseControl(currentSpeed);
        }

        if (scanForObstacles && objectDetector.isObstacleAhead()) {
            DetectedObject nearestObject = objectDetector.getNearestObject();
            if (nearestObject != null && nearestObject.getDistance() <= minSafeDistance) {
                handleObstacle();
                return;
            }
        }

        adjustGear(scanForObstacles);
    }

    public void handleObstacle() {
        DetectedObject nearestObject = objectDetector.getNearestObject();

        if (nearestObject == null) {
            System.out.println("No obstacle detected to handle.");
            return;
        }

        if (!nearestObject.isThreat() || nearestObject.getDistance() > minSafeDistance) {
            System.out.println("Detected object is not considered a threat.");
            return;
        }

        ObjectType objectType = nearestObject.getObjectType();
        switch (objectType) {
            case PEDESTRIAN:
            case UNKNOWN:
                changeMode(AutopilotMode.EMERGENCY_STOP);
                gradualEmergencyStop();
                while (gearBox.getCurrentGear() > 1) {
                    gearBox.shiftDown();
                }
                System.out.println("Emergency stop triggered for object type: " + objectType);
                deactivate();
                break;
            case VEHICLE:
            case OBSTACLE:
                changeMode(AutopilotMode.COLLISION_AVOIDANCE);
                brake.apply();
                engine.brake();
                if (speedSensor.getCurrentSpeed() > 10.0f) {
                    speedSensor.setCurrentSpeed(speedSensor.getCurrentSpeed() - 10.0f);
                } else {
                    speedSensor.setCurrentSpeed(0.0f);
                }
                System.out.println("Collision avoidance maneuver executed for: " + objectType + ". Reducing speed and steering around obstacle.");
                break;
            case TRAFFIC_SIGN:
                changeMode(AutopilotMode.CRUISE_CONTROL);
                float adjustedSpeed = Math.max(0.0f, targetSpeed - 10.0f);
                setCruiseSpeed(adjustedSpeed);
                if (speedSensor.getCurrentSpeed() > adjustedSpeed) {
                    brake.apply();
                    speedSensor.setCurrentSpeed(adjustedSpeed);
                }
                System.out.println("Traffic sign detected. Adjusting target speed to: " + adjustedSpeed);
                break;
            default:
                System.out.println("Unhandled object type: " + objectType);
                break;
        }
    }

    public void adjustGear() {
        adjustGear(true);
    }

    private void adjustGear(boolean checkObstacleBeforeShiftUp) {
        if (!isAutomaticTransmission()) {
            System.out.println("Transmisi manual tidak bisa auto-shift.");
            return;
        }

        int optimalGear = speedSensor.getOptimalGear();
        int currentGear = gearBox.getCurrentGear();

        if (optimalGear == currentGear) {
            System.out.println("Gigi sudah optimal: " + currentGear);
            return;
        }

        while (gearBox.getCurrentGear() < optimalGear) {
            if (checkObstacleBeforeShiftUp && objectDetector.isObstacleAhead()) {
                changeMode(AutopilotMode.COLLISION_AVOIDANCE);
                System.out.println("Shift up dibatalkan karena ada obstacle di depan.");
                return;
            }
            gearBox.shiftUp();
        }

        while (gearBox.getCurrentGear() > optimalGear) {
            gearBox.shiftDown();
            if (speedSensor.getCurrentSpeed() == 0.0f && gearBox.getCurrentGear() > 1) {
                while (gearBox.getCurrentGear() > 1) {
                    gearBox.shiftDown();
                }
                break;
            }
        }

        gearBox.autoShift(speedSensor.getSpeed());
        System.out.println("Gigi sekarang: " + gearBox.getCurrentGear());
    }

    public void setCruiseSpeed(float speed) {
        targetSpeed = speed;
        changeMode(AutopilotMode.CRUISE_CONTROL);
        System.out.println("Cruise speed set to: " + targetSpeed);
    }

    public String getStatusReport() {
        return "AutopilotSystem{" +
                "active=" + active +
                ", targetSpeed=" + targetSpeed +
                ", minSafeDistance=" + minSafeDistance +
                ", mode=" + mode +
                ", currentSpeed=" + speedSensor.getCurrentSpeed() +
                ", currentGear=" + gearBox.getCurrentGear() +
                ", detectorReady=" + objectDetector.isReady() +
                ", speedSensorReady=" + speedSensor.isReady() +
                ", nearestObject=" + objectDetector.getNearestObject() +
                '}';
    }

    private void applyCruiseControl(float currentSpeed) {
        if (speedSensor.maintainSpeed(targetSpeed)) {
            System.out.println("Kecepatan stabil pada: " + currentSpeed);
        } else if (currentSpeed < targetSpeed) {
            engine.accelerate();
            speedSensor.setCurrentSpeed(Math.min(currentSpeed + 5.0f, targetSpeed));
        } else {
            brake.apply();
            speedSensor.setCurrentSpeed(Math.max(currentSpeed - 5.0f, targetSpeed));
        }
    }

    private void gradualEmergencyStop() {
        while (speedSensor.getCurrentSpeed() > 0.0f) {
            brake.emergencyBrake();
            engine.brake();
            speedSensor.setCurrentSpeed(Math.max(0.0f, speedSensor.getCurrentSpeed() - EMERGENCY_DECELERATION_STEP));
            System.out.println("Emergency stop reducing speed to: " + speedSensor.getCurrentSpeed());
            if (speedSensor.getCurrentSpeed() > 0.0f) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException exception) {
                    Thread.currentThread().interrupt();
                    speedSensor.setCurrentSpeed(0.0f);
                    break;
                }
            }
        }
    }

    private boolean isAutomaticTransmission() {
        List<GearBoxType> gearBoxTypes = gearBox.getGearBoxTypes();
        for (GearBoxType gearBoxType : gearBoxTypes) {
            if (gearBoxType.isAutomatic()) {
                return true;
            }
        }
        return false;
    }

    private boolean shouldRunNormalDetection() {
        return random.nextFloat() < NORMAL_DETECTION_CHANCE;
    }

    private void changeMode(AutopilotMode newMode) {
        if (mode != newMode) {
            System.out.println("Autopilot mode changed: " + mode + " -> " + newMode);
            mode = newMode;
        }
    }
}
