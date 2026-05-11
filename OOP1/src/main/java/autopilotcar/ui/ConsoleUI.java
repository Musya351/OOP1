package autopilotcar.ui;

import autopilotcar.autopilot.AutopilotSystem;
import autopilotcar.model.Car;

import java.util.Scanner;

// Menyediakan antarmuka konsol untuk simulasi AutopilotCar.
public class ConsoleUI {
    private final Scanner scanner;
    private final Car car;
    private final AutopilotSystem autopilot;

    public ConsoleUI(Car car, AutopilotSystem autopilot) {
        this.scanner = new Scanner(System.in);
        this.car = car;
        this.autopilot = autopilot;
    }

    public void displayMenu() {
        System.out.println();
        System.out.println("===== MENU AUTOPILOT CAR =====");
        System.out.println("[1] Lihat Status Mobil");
        System.out.println("[2] Aktifkan Autopilot");
        System.out.println("[3] Set Cruise Control");
        System.out.println("[4] Simulasi Deteksi Objek");
        System.out.println("[5] Emergency Stop Manual");
        System.out.println("[6] Nonaktifkan Autopilot");
        System.out.println("[7] Keluar");
        System.out.print("Pilih menu: ");
    }

    public boolean processInput() {
        Integer choice = readIntegerInput();
        if (choice == null) {
            displayAlert("Input harus berupa angka.");
            return true;
        }

        switch (choice) {
            case 1:
                displayStatus();
                break;
            case 2:
                car.enableAutopilot();
                autopilot.activate();
                displayAlert("Autopilot aktif.");
                break;
            case 3:
                handleCruiseControl();
                break;
            case 4:
                simulateObjectDetection();
                break;
            case 5:
                handleEmergencyStop();
                break;
            case 6:
                car.disableAutopilot();
                autopilot.deactivate();
                displayAlert("Autopilot nonaktif.");
                break;
            case 7:
                displayAlert("Keluar dari simulasi.");
                return false;
            default:
                displayAlert("Input tidak valid.");
                break;
        }
        return true;
    }

    public void displayStatus() {
        System.out.println(car.getStatus());
        System.out.println(autopilot.getStatusReport());
    }

    public void displayAlert(String msg) {
        System.out.println("================================");
        System.out.println(msg);
        System.out.println("================================");
    }

    public void run() {
        boolean isRunning = true;
        while (isRunning) {
            displayMenu();
            isRunning = processInput();
        }
        scanner.close();
    }

    private void handleCruiseControl() {
        System.out.print("Masukkan target speed: ");
        Float targetSpeed = readFloatInput();
        if (targetSpeed == null) {
            displayAlert("Target speed harus berupa angka.");
            return;
        }

        if (!autopilot.isActive()) {
            car.enableAutopilot();
            autopilot.activate();
        }

        autopilot.setCruiseSpeed(targetSpeed);
        autopilot.update();
        car.setCurrentSpeed(autopilot.getCurrentSpeed());

        if (autopilot.isActive()) {
            displayAlert("Cruise Control aktif pada target speed: " + targetSpeed);
        } else {
            displayAlert("Cruise Control dibatalkan. Sistem berpindah ke " + autopilot.getMode() + ".");
        }
    }

    private void simulateObjectDetection() {
        if (!autopilot.isActive()) {
            car.enableAutopilot();
            autopilot.activate();
        }

        autopilot.update();
        car.setCurrentSpeed(autopilot.getCurrentSpeed());
        displayAlert("Simulasi deteksi objek selesai.\n" + autopilot.getStatusReport());
    }

    private void handleEmergencyStop() {
        car.stop();
        car.disableAutopilot();
        autopilot.deactivate();
        displayAlert("Emergency stop manual dijalankan.");
    }

    private Integer readIntegerInput() {
        String input = scanner.nextLine();
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    private Float readFloatInput() {
        String input = scanner.nextLine();
        try {
            return Float.parseFloat(input.trim());
        } catch (NumberFormatException exception) {
            return null;
        }
    }
}
