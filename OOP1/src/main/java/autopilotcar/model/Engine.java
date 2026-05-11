package autopilotcar.model;

// Merepresentasikan mesin kendaraan untuk simulasi dasar.
public class Engine {
    private float capacity;
    private int numCylinders;

    public Engine(float capacity, int numCylinders) {
        this.capacity = capacity;
        this.numCylinders = numCylinders;
    }

    public float getCapacity() {
        return capacity;
    }

    public void setCapacity(float capacity) {
        this.capacity = capacity;
    }

    public int getNumCylinders() {
        return numCylinders;
    }

    public void setNumCylinders(int numCylinders) {
        this.numCylinders = numCylinders;
    }

    public void start() {
        System.out.println("Engine started with capacity: " + capacity + "L");
    }

    public void brake() {
        System.out.println("Engine braking activated.");
    }

    public void accelerate() {
        System.out.println("Engine accelerating with " + numCylinders + " cylinders.");
    }

    @Override
    public String toString() {
        return "Engine{" +
                "capacity=" + capacity +
                ", numCylinders=" + numCylinders +
                '}';
    }
}
