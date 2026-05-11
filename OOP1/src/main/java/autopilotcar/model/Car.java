package autopilotcar.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Merepresentasikan mobil sebagai pusat komponen dan simulasi pergerakan.
public class Car {
    private String registrationNum;
    private int year;
    private String licenseNumber;
    private boolean autopilotEnabled;
    private float currentSpeed;
    private Engine engine;
    private GearBox gearBox;
    private Body body;
    private List<Suspension> suspensions;
    private Brake brake;
    private Wheel[] wheels;

    public Car(String registrationNum, int year, String licenseNumber, boolean autopilotEnabled,
               float currentSpeed, Engine engine, GearBox gearBox, Body body,
               List<Suspension> suspensions, Brake brake, Wheel[] wheels) {
        this.registrationNum = registrationNum;
        this.year = year;
        this.licenseNumber = licenseNumber;
        this.autopilotEnabled = autopilotEnabled;
        this.currentSpeed = currentSpeed;
        this.engine = engine;
        this.gearBox = gearBox;
        this.body = body;
        this.suspensions = new ArrayList<>(suspensions);
        this.brake = brake;
        this.wheels = initializeWheels(wheels);
    }

    private Wheel[] initializeWheels(Wheel[] wheels) {
        if (wheels == null || wheels.length != 4) {
            throw new IllegalArgumentException("Car must be initialized with exactly 4 wheels.");
        }
        return Arrays.copyOf(wheels, wheels.length);
    }

    public String getRegistrationNum() {
        return registrationNum;
    }

    public void setRegistrationNum(String registrationNum) {
        this.registrationNum = registrationNum;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public boolean isAutopilotEnabled() {
        return autopilotEnabled;
    }

    public void setAutopilotEnabled(boolean autopilotEnabled) {
        this.autopilotEnabled = autopilotEnabled;
    }

    public float getCurrentSpeed() {
        return currentSpeed;
    }

    public void setCurrentSpeed(float currentSpeed) {
        this.currentSpeed = currentSpeed;
    }

    public Engine getEngine() {
        return engine;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public GearBox getGearBox() {
        return gearBox;
    }

    public void setGearBox(GearBox gearBox) {
        this.gearBox = gearBox;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public List<Suspension> getSuspensions() {
        return new ArrayList<>(suspensions);
    }

    public void setSuspensions(List<Suspension> suspensions) {
        this.suspensions = new ArrayList<>(suspensions);
    }

    public Brake getBrake() {
        return brake;
    }

    public void setBrake(Brake brake) {
        this.brake = brake;
    }

    public Wheel[] getWheels() {
        return Arrays.copyOf(wheels, wheels.length);
    }

    public void setWheels(Wheel[] wheels) {
        this.wheels = initializeWheels(wheels);
    }

    public void moveForward() {
        engine.start();
        engine.accelerate();
        currentSpeed += 10.0f;
        gearBox.autoShift(currentSpeed);
        System.out.println("Car is moving forward at speed: " + currentSpeed);
    }

    public void moveBackward() {
        engine.start();
        currentSpeed = 5.0f;
        gearBox.setCurrentGear(0);
        System.out.println("Car is moving backward at speed: " + currentSpeed);
    }

    public void stop() {
        brake.apply();
        engine.brake();
        currentSpeed = 0.0f;
        gearBox.setCurrentGear(0);
        System.out.println("Car has stopped.");
    }

    public void turnRight() {
        System.out.println("Car is turning right.");
    }

    public void turnLeft() {
        System.out.println("Car is turning left.");
    }

    public void enableAutopilot() {
        autopilotEnabled = true;
        System.out.println("Autopilot enabled.");
    }

    public void disableAutopilot() {
        autopilotEnabled = false;
        System.out.println("Autopilot disabled.");
    }

    public String getStatus() {
        return "Car{" +
                "registrationNum='" + registrationNum + '\'' +
                ", year=" + year +
                ", licenseNumber='" + licenseNumber + '\'' +
                ", autopilotEnabled=" + autopilotEnabled +
                ", currentSpeed=" + currentSpeed +
                ", engine=" + engine +
                ", gearBox=" + gearBox +
                ", body=" + body +
                ", suspensions=" + suspensions +
                ", brake=" + brake +
                ", wheels=" + Arrays.toString(wheels) +
                '}';
    }

    @Override
    public String toString() {
        return getStatus();
    }
}
