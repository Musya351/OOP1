package autopilotcar.model;

// Merepresentasikan sistem rem kendaraan.
public class Brake {
    private String type;
    private float brakeForce;

    public Brake(String type, float brakeForce) {
        this.type = type;
        this.brakeForce = brakeForce;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getBrakeForce() {
        return brakeForce;
    }

    public void setBrakeForce(float brakeForce) {
        this.brakeForce = brakeForce;
    }

    public void apply() {
        System.out.println("Brake applied with force: " + brakeForce);
    }

    public void emergencyBrake() {
        System.out.println("Emergency brake activated using " + type + " brake.");
    }

    @Override
    public String toString() {
        return "Brake{" +
                "type='" + type + '\'' +
                ", brakeForce=" + brakeForce +
                '}';
    }
}
