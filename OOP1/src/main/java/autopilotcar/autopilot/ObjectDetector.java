package autopilotcar.autopilot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Mensimulasikan sensor pendeteksi objek di sekitar kendaraan.
public class ObjectDetector implements Sensor {
    private static final ObjectType[] DETECTABLE_TYPES = {
            ObjectType.VEHICLE,
            ObjectType.PEDESTRIAN,
            ObjectType.OBSTACLE,
            ObjectType.TRAFFIC_SIGN
    };

    private final Random random;
    private float detectionRange;
    private float sensitivity;
    private List<DetectedObject> detectedObjects;
    private boolean ready;

    public ObjectDetector(float detectionRange, float sensitivity) {
        this.detectionRange = detectionRange;
        this.sensitivity = sensitivity;
        this.detectedObjects = new ArrayList<>();
        this.random = new Random();
        this.ready = false;
    }

    public float getDetectionRange() {
        return detectionRange;
    }

    public void setDetectionRange(float detectionRange) {
        this.detectionRange = detectionRange;
    }

    public float getSensitivity() {
        return sensitivity;
    }

    public void setSensitivity(float sensitivity) {
        this.sensitivity = sensitivity;
    }

    public List<DetectedObject> getDetectedObjects() {
        return new ArrayList<>(detectedObjects);
    }

    public void scan() {
        detectedObjects.clear();
        int totalObjects = random.nextInt(4);

        for (int i = 0; i < totalObjects; i++) {
            float distance = 1.0f + random.nextFloat() * detectionRange;
            float relativeSpeed = -20.0f + random.nextFloat() * 40.0f;
            ObjectType objectType = DETECTABLE_TYPES[random.nextInt(DETECTABLE_TYPES.length)];
            boolean threat = distance <= (detectionRange * sensitivity * 0.5f);

            detectedObjects.add(new DetectedObject(
                    "OBJ-" + (i + 1),
                    distance,
                    relativeSpeed,
                    threat,
                    objectType
            ));
        }
    }

    public DetectedObject getNearestObject() {
        if (detectedObjects.isEmpty()) {
            return null;
        }

        DetectedObject nearestObject = detectedObjects.get(0);
        for (DetectedObject detectedObject : detectedObjects) {
            if (detectedObject.getDistance() < nearestObject.getDistance()) {
                nearestObject = detectedObject;
            }
        }
        return nearestObject;
    }

    public boolean isObstacleAhead() {
        DetectedObject nearestObject = getNearestObject();
        return nearestObject != null && nearestObject.getDistance() < detectionRange;
    }

    public float getDistance() {
        DetectedObject nearestObject = getNearestObject();
        return nearestObject != null ? nearestObject.getDistance() : -1.0f;
    }

    @Override
    public void initialize() {
        ready = true;
        detectedObjects.clear();
    }

    @Override
    public Object readData() {
        if (!ready) {
            initialize();
        }
        scan();
        return getDetectedObjects();
    }

    @Override
    public boolean isReady() {
        return ready;
    }
}
