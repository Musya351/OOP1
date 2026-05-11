package autopilotcar.model;

// Merepresentasikan bodi kendaraan dan karakteristik fisiknya.
public class Body {
    private int numberOfDoors;
    private String color;
    private String frameType;

    public Body(int numberOfDoors, String color, String frameType) {
        this.numberOfDoors = numberOfDoors;
        this.color = color;
        this.frameType = frameType;
    }

    public int getNumberOfDoors() {
        return numberOfDoors;
    }

    public void setNumberOfDoors(int numberOfDoors) {
        this.numberOfDoors = numberOfDoors;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFrameType() {
        return frameType;
    }

    public void setFrameType(String frameType) {
        this.frameType = frameType;
    }

    @Override
    public String toString() {
        return "Body{" +
                "numberOfDoors=" + numberOfDoors +
                ", color='" + color + '\'' +
                ", frameType='" + frameType + '\'' +
                '}';
    }
}
