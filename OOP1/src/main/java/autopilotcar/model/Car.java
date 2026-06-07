package autopilotcar.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Merepresentasikan mobil sebagai pusat komponen dan simulasi pergerakan.
public class Car {
    private String registrationNum;
    private int year;
    private String licenseNumber;
    private boolean poweredOn;
    private boolean autopilotEnabled;
    private float currentSpeed;
    private FuelTank fuelTank;
    private long lastFuelUpdateMillis;
    private String turnSignal;
    private boolean reverseMode;
    private Engine engine;
    private GearBox gearBox;
    private Body body;
    private List<Suspension> suspensions;
    private Brake brake;
    private Wheel[] wheels;

    public Car(String registrationNum, int year, String licenseNumber, boolean autopilotEnabled,
               float currentSpeed, Engine engine, GearBox gearBox, Body body,
               List<Suspension> suspensions, Brake brake, Wheel[] wheels, FuelTank fuelTank) {
        this.registrationNum = registrationNum;
        this.year = year;
        this.licenseNumber = licenseNumber;
        this.poweredOn = false;
        this.autopilotEnabled = autopilotEnabled;
        this.currentSpeed = currentSpeed;
        this.fuelTank = fuelTank;
        this.lastFuelUpdateMillis = System.currentTimeMillis();
        this.turnSignal = "Mati";
        this.reverseMode = false;
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

    public boolean isPoweredOn() {
        return poweredOn;
    }

    public boolean isAutopilotEnabled() {
        return autopilotEnabled;
    }

    public void setAutopilotEnabled(boolean autopilotEnabled) {
        this.autopilotEnabled = autopilotEnabled;
    }

    public float getCurrentSpeed() {
        updateFuelUsage();
        return currentSpeed;
    }

    public void setCurrentSpeed(float currentSpeed) {
        updateFuelUsage();
        this.currentSpeed = currentSpeed;
        resetFuelTimer();
    }

    public float getFuelLevel() {
        return fuelTank.getFuelPercentage();
    }

    public void setFuelLevel(float fuelLevel) {
        fuelTank.setCurrentLiters((fuelLevel / 100.0f) * fuelTank.getCapacityLiters());
    }

    public FuelTank getFuelTank() {
        updateFuelUsage();
        return fuelTank;
    }

    public void setFuelTank(FuelTank fuelTank) {
        this.fuelTank = fuelTank;
        resetFuelTimer();
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

    public boolean turnOn() {
        if (poweredOn) {
            System.out.println("Car is already turned on.");
            return false;
        }

        if (fuelTank.isEmpty()) {
            System.out.println("Fuel tank is empty. Please refuel before starting the engine.");
            return false;
        }

        poweredOn = true;
        resetFuelTimer();
        engine.start();
        System.out.println("Car is turned on.");
        return true;
    }

    public void fillFuel() {
        fuelTank.fillFull();
        resetFuelTimer();
        System.out.println("Fuel tank is full.");
    }

    public boolean turnOff() {
        updateFuelUsage();
        if (!poweredOn) {
            System.out.println("Car is already turned off.");
            return false;
        }

        if (currentSpeed > 0.0f) {
            System.out.println("Car must stop before turning off.");
            return false;
        }

        poweredOn = false;
        autopilotEnabled = false;
        gearBox.setCurrentGear(0);
        resetFuelTimer();
        System.out.println("Car is turned off.");
        return true;
    }

    public void moveForward() {
        updateFuelUsage();
        if (!poweredOn) {
            System.out.println("Car must be turned on before moving.");
            return;
        }

        if (fuelTank.isEmpty()) {
            stopBecauseFuelEmpty();
            return;
        }

        engine.start();
        engine.accelerate();
        if (reverseMode) {
            currentSpeed += 5.0f;
            gearBox.setCurrentGear(0);
            System.out.println("Car is moving backward faster at speed: " + currentSpeed);
            return;
        }

        reverseMode = false;
        currentSpeed += 10.0f;
        gearBox.autoShift(currentSpeed);
        System.out.println("Car is moving forward at speed: " + currentSpeed);
    }

    public void moveBackward() {
        updateFuelUsage();
        if (!poweredOn) {
            System.out.println("Car must be turned on before moving.");
            return;
        }

        if (fuelTank.isEmpty()) {
            stopBecauseFuelEmpty();
            return;
        }

        if (currentSpeed > 0.0f) {
            System.out.println("Car must stop before shifting to reverse gear.");
            return;
        }

        engine.start();
        reverseMode = true;
        currentSpeed = 5.0f;
        gearBox.setCurrentGear(0);
        System.out.println("Car is moving backward at speed: " + currentSpeed);
    }

    public void applyBrake() {
        applyBrake(10.0f);
    }

    public void applyBrake(float speedReduction) {
        updateFuelUsage();
        if (!poweredOn) {
            System.out.println("Car must be turned on before braking.");
            return;
        }

        brake.apply();
        engine.brake();
        currentSpeed = Math.max(0.0f, currentSpeed - speedReduction);
        if (currentSpeed == 0.0f) {
            reverseMode = false;
            gearBox.setCurrentGear(0);
            resetFuelTimer();
        } else if (!reverseMode) {
            gearBox.autoShift(currentSpeed);
        }
        System.out.println("Car braking. Current speed: " + currentSpeed);
    }

    public void stop() {
        updateFuelUsage();
        brake.apply();
        engine.brake();
        currentSpeed = 0.0f;
        gearBox.setCurrentGear(0);
        reverseMode = false;
        turnSignal = "Mati";
        resetFuelTimer();
        System.out.println("Car has stopped.");
    }

    public void turnRight() {
        turnSignal = "Kanan";
        System.out.println("Car is turning right.");
    }

    public void turnLeft() {
        turnSignal = "Kiri";
        System.out.println("Car is turning left.");
    }

    public void driveStraight() {
        turnSignal = "Mati";
        System.out.println("Car is driving straight.");
    }

    public String getTurnSignal() {
        return turnSignal;
    }

    public String getGearDisplay() {
        if (reverseMode && currentSpeed > 0.0f) {
            return "R";
        }
        return String.valueOf(gearBox.getCurrentGear());
    }

    public boolean isReverseMode() {
        return reverseMode;
    }

    public void enableAutopilot() {
        updateFuelUsage();
        if (!poweredOn) {
            System.out.println("Car must be turned on before enabling autopilot.");
            return;
        }

        if (fuelTank.isEmpty()) {
            System.out.println("Fuel tank is empty. Autopilot cannot be enabled.");
            return;
        }

        autopilotEnabled = true;
        System.out.println("Autopilot enabled.");
    }

    public void disableAutopilot() {
        autopilotEnabled = false;
        System.out.println("Autopilot disabled.");
    }

    public String getStatus() {
        updateFuelUsage();
        return "Car{" +
                "registrationNum='" + registrationNum + '\'' +
                ", year=" + year +
                ", licenseNumber='" + licenseNumber + '\'' +
                ", poweredOn=" + poweredOn +
                ", autopilotEnabled=" + autopilotEnabled +
                ", currentSpeed=" + currentSpeed +
                ", fuelTank=" + fuelTank +
                ", turnSignal='" + turnSignal + '\'' +
                ", gearDisplay='" + getGearDisplay() + '\'' +
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

    public boolean updateFuelUsage() {
        if (!poweredOn || currentSpeed <= 0.0f || fuelTank.isEmpty()) {
            resetFuelTimer();
            return false;
        }

        long now = System.currentTimeMillis();
        long elapsedMillis = now - lastFuelUpdateMillis;
        if (elapsedMillis <= 0) {
            return false;
        }

        float elapsedSeconds = elapsedMillis / 1000.0f;
        boolean empty = fuelTank.consume(elapsedSeconds / 10.0f);
        lastFuelUpdateMillis = now;

        if (empty) {
            stopBecauseFuelEmpty();
            return true;
        }
        return false;
    }

    private void stopBecauseFuelEmpty() {
        brake.apply();
        engine.brake();
        currentSpeed = 0.0f;
        autopilotEnabled = false;
        gearBox.setCurrentGear(0);
        reverseMode = false;
        resetFuelTimer();
        System.out.println("BBM habis. Mobil berhenti, tetapi mesin tetap hidup.");
    }

    private void resetFuelTimer() {
        lastFuelUpdateMillis = System.currentTimeMillis();
    }
}
