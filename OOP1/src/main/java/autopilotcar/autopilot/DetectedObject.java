package autopilotcar.autopilot;

// Merepresentasikan objek yang terdeteksi oleh sensor autopilot.
public class DetectedObject {
    private String objectId;
    private float distance;
    private float relativeSpeed;
    private boolean threat;
    private ObjectType objectType;

    public DetectedObject(String objectId, float distance, float relativeSpeed, boolean threat, ObjectType objectType) {
        this.objectId = objectId;
        this.distance = distance;
        this.relativeSpeed = relativeSpeed;
        this.threat = threat;
        this.objectType = objectType;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public float getRelativeSpeed() {
        return relativeSpeed;
    }

    public void setRelativeSpeed(float relativeSpeed) {
        this.relativeSpeed = relativeSpeed;
    }

    public boolean isThreat() {
        return threat;
    }

    public void setThreat(boolean threat) {
        this.threat = threat;
    }

    public ObjectType getObjectType() {
        return objectType;
    }

    public void setObjectType(ObjectType objectType) {
        this.objectType = objectType;
    }

    @Override
    public String toString() {
        return "DetectedObject{" +
                "objectId='" + objectId + '\'' +
                ", distance=" + distance +
                ", relativeSpeed=" + relativeSpeed +
                ", threat=" + threat +
                ", objectType=" + objectType +
                '}';
    }
}
