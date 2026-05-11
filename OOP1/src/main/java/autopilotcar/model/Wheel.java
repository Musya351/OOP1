package autopilotcar.model;

// Merepresentasikan roda yang memiliki ban sebagai bagian komponennya.
public class Wheel {
    private float diameter;
    private String position;
    private float rpm;
    private Tire tire;

    public Wheel(float diameter, String position, float rpm, Tire tire) {
        this.diameter = diameter;
        this.position = position;
        this.rpm = rpm;
        this.tire = tire;
    }

    public float getDiameter() {
        return diameter;
    }

    public void setDiameter(float diameter) {
        this.diameter = diameter;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public float getRpm() {
        return rpm;
    }

    public void setRpm(float rpm) {
        this.rpm = rpm;
    }

    public Tire getTire() {
        return tire;
    }

    public void setTire(Tire tire) {
        this.tire = tire;
    }

    @Override
    public String toString() {
        return "Wheel{" +
                "diameter=" + diameter +
                ", position='" + position + '\'' +
                ", rpm=" + rpm +
                ", tire=" + tire +
                '}';
    }
}
