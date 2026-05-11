package autopilotcar.ui;

import autopilotcar.autopilot.AutopilotSystem;
import autopilotcar.autopilot.ObjectDetector;
import autopilotcar.autopilot.SpeedSensor;
import autopilotcar.model.Body;
import autopilotcar.model.Brake;
import autopilotcar.model.Car;
import autopilotcar.model.Engine;
import autopilotcar.model.GearBox;
import autopilotcar.model.GearBoxType;
import autopilotcar.model.Suspension;
import autopilotcar.model.Tire;
import autopilotcar.model.Wheel;

import java.util.ArrayList;
import java.util.List;

// Menyiapkan objek simulasi kendaraan dan menjalankan UI konsol.
public class CarSimulator {
    private Car car;
    private AutopilotSystem autopilot;

    public Car initializeCar() {
        Engine engine = new Engine(2.0f, 4);

        List<GearBoxType> gearBoxTypes = new ArrayList<>();
        gearBoxTypes.add(new GearBoxType("CVT", "Automatic transmission for autopilot mode", true));
        GearBox gearBox = new GearBox(new float[]{3.2f, 2.1f, 1.5f, 1.0f, 0.8f}, 1, gearBoxTypes);

        Body body = new Body(4, "Silver", "Sedan");

        List<Suspension> suspensions = new ArrayList<>();
        suspensions.add(new Suspension(1.8f, 0.7f, "Front Left"));
        suspensions.add(new Suspension(1.8f, 0.7f, "Front Right"));
        suspensions.add(new Suspension(1.6f, 0.6f, "Rear Left"));
        suspensions.add(new Suspension(1.6f, 0.6f, "Rear Right"));

        Brake brake = new Brake("ABS Disc", 8.5f);

        Wheel[] wheels = new Wheel[4];
        wheels[0] = new Wheel(18.0f, "Front Left", 0.0f, new Tire(225.0f, 32.0f, 7.5f));
        wheels[1] = new Wheel(18.0f, "Front Right", 0.0f, new Tire(225.0f, 32.0f, 7.5f));
        wheels[2] = new Wheel(18.0f, "Rear Left", 0.0f, new Tire(225.0f, 32.0f, 7.5f));
        wheels[3] = new Wheel(18.0f, "Rear Right", 0.0f, new Tire(225.0f, 32.0f, 7.5f));

        car = new Car(
                "REG-APC-001",
                2026,
                "B 1234 APC",
                false,
                0.0f,
                engine,
                gearBox,
                body,
                suspensions,
                brake,
                wheels
        );

        return car;
    }

    public AutopilotSystem setupAutopilot() {
        if (car == null) {
            initializeCar();
        }

        ObjectDetector objectDetector = new ObjectDetector(50.0f, 0.8f);
        SpeedSensor speedSensor = new SpeedSensor(car.getCurrentSpeed(), 120.0f);

        autopilot = new AutopilotSystem(
                60.0f,
                15.0f,
                objectDetector,
                speedSensor,
                car.getGearBox(),
                car.getBrake(),
                car.getEngine()
        );

        return autopilot;
    }

    public void startSimulation() {
        if (car == null) {
            initializeCar();
        }
        if (autopilot == null) {
            setupAutopilot();
        }

        ConsoleUI consoleUI = new ConsoleUI(car, autopilot);
        consoleUI.run();
    }

    public static void main(String[] args) {
        CarSimulator simulator = new CarSimulator();
        simulator.initializeCar();
        simulator.setupAutopilot();
        simulator.startSimulation();
    }
}
