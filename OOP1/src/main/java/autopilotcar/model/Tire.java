package autopilotcar.model;

// Merepresentasikan ban pada kendaraan.
public class Tire {
    private float width;
    private float airPressure;
    private float treadDepth;

    public Tire(float width, float airPressure, float treadDepth) {
        this.width = width;
        this.airPressure = airPressure;
        this.treadDepth = treadDepth;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getAirPressure() {
        return airPressure;
    }

    public void setAirPressure(float airPressure) {
        this.airPressure = airPressure;
    }

    public float getTreadDepth() {
        return treadDepth;
    }

    public void setTreadDepth(float treadDepth) {
        this.treadDepth = treadDepth;
    }

    @Override
    public String toString() {
        return "Tire{" +
                "width=" + width +
                ", airPressure=" + airPressure +
                ", treadDepth=" + treadDepth +
                '}';
    }
}
