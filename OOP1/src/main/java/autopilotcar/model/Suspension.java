package autopilotcar.model;

// Merepresentasikan suspensi kendaraan yang dapat bergantung pada roda.
public class Suspension {
    private float springRate;
    private float dampingCoeff;
    private String type;

    public Suspension(float springRate, float dampingCoeff, String type) {
        this.springRate = springRate;
        this.dampingCoeff = dampingCoeff;
        this.type = type;
    }

    public float getSpringRate() {
        return springRate;
    }

    public void setSpringRate(float springRate) {
        this.springRate = springRate;
    }

    public float getDampingCoeff() {
        return dampingCoeff;
    }

    public void setDampingCoeff(float dampingCoeff) {
        this.dampingCoeff = dampingCoeff;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String describeWheelDependency(Wheel wheel) {
        return "Suspension " + type + " supports wheel at " + wheel.getPosition();
    }

    @Override
    public String toString() {
        return "Suspension{" +
                "springRate=" + springRate +
                ", dampingCoeff=" + dampingCoeff +
                ", type='" + type + '\'' +
                '}';
    }
}
