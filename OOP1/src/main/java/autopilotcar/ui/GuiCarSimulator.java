package autopilotcar.ui;

import autopilotcar.autopilot.AutopilotSystem;
import autopilotcar.model.Car;

// Entry point untuk menjalankan dashboard GUI AutopilotCar.
public class GuiCarSimulator {
    public static void main(String[] args) {
        CarSimulator simulator = new CarSimulator();
        Car car = simulator.initializeCar();
        AutopilotSystem autopilot = simulator.setupAutopilot();
        DashboardGUI.show(car, autopilot);
    }
}
