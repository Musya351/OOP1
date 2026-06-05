package autopilotcar.ui;

import autopilotcar.autopilot.AutopilotSystem;
import autopilotcar.model.Car;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

// Menyediakan antarmuka konsol untuk simulasi AutopilotCar.
public class ConsoleUI {
    private final Scanner scanner;
    private final Car car;
    private final AutopilotSystem autopilot;
    private final Path fuelSavePath;

    public ConsoleUI(Car car, AutopilotSystem autopilot) {
        this.scanner = new Scanner(System.in);
        this.car = car;
        this.autopilot = autopilot;
        this.fuelSavePath = Paths.get("fuel-level.txt");
    }

    public void displayMenu() {
        updateCarFuelState();
        System.out.println();
        if (autopilot.isActive()) {
            displayAutopilotMenu();
        } else if (car.isPoweredOn()) {
            displayEngineOnMenu();
        } else {
            displayInitialMenu();
        }
        System.out.print("Pilih menu: ");
    }

    public boolean processInput() {
        Integer choice = readIntegerInput();
        if (choice == null) {
            displayAlert("Input harus berupa angka.");
            return true;
        }

        if (autopilot.isActive()) {
            return processAutopilotMenu(choice);
        }
        if (car.isPoweredOn()) {
            return processEngineOnMenu(choice);
        }
        return processInitialMenu(choice);
    }

    private void displayInitialMenu() {
        System.out.println("===== MENU AWAL AUTOPILOT CAR =====");
        System.out.println("[1] Hidupkan Mesin");
        System.out.println("[2] Isi Bensin");
        System.out.println("[3] Keluar");
    }

    private void displayEngineOnMenu() {
        System.out.println("===== MENU MESIN HIDUP =====");
        System.out.println("[1] Matikan Mesin");
        System.out.println("[2] Status Mobil");
        System.out.println("[3] Aktifkan Autopilot");
        System.out.println("[4] Isi Bensin");
    }

    private void displayAutopilotMenu() {
        System.out.println("===== MENU AUTOPILOT AKTIF =====");
        System.out.println("[1] Set Cruise Control");
        System.out.println("[2] Simulasi Deteksi Objek");
        System.out.println("[3] Emergency Stop Manual");
        System.out.println("[4] Nonaktifkan Autopilot");
    }

    private boolean processInitialMenu(int choice) {
        switch (choice) {
            case 1:
                handleTurnOn();
                break;
            case 2:
                handleFillFuel();
                break;
            case 3:
                displayAlert("Keluar dari simulasi.");
                saveFuel();
                return false;
            default:
                displayAlert("Input tidak valid.");
                break;
        }
        return true;
    }

    private boolean processEngineOnMenu(int choice) {
        switch (choice) {
            case 1:
                handleTurnOff();
                break;
            case 2:
                displayStatus();
                break;
            case 3:
                handleActivateAutopilot();
                break;
            case 4:
                handleFillFuel();
                break;
            default:
                displayAlert("Input tidak valid.");
                break;
        }
        return true;
    }

    private boolean processAutopilotMenu(int choice) {
        switch (choice) {
            case 1:
                handleCruiseControl();
                break;
            case 2:
                simulateObjectDetection();
                break;
            case 3:
                handleEmergencyStop();
                break;
            case 4:
                handleDeactivateAutopilot();
                break;
            default:
                displayAlert("Input tidak valid.");
                break;
        }
        return true;
    }

    public void displayStatus() {
        updateCarFuelState();
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
        saveFuel();
        scanner.close();
    }

    private void handleCruiseControl() {
        updateCarFuelState();
        if (!car.isPoweredOn()) {
            displayAlert("Mobil masih mati. Nyalakan mobil terlebih dahulu.");
            return;
        }
        if (car.getFuelTank().isEmpty()) {
            displayAlert("BBM habis. Isi bensin terlebih dahulu.");
            return;
        }

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
        updateCarFuelState();
        saveFuel();

        if (autopilot.isActive()) {
            displayAlert("Cruise Control aktif pada target speed: " + targetSpeed);
        } else {
            displayAlert("Cruise Control dibatalkan. Sistem berpindah ke " + autopilot.getMode() + ".");
        }
    }

    private void simulateObjectDetection() {
        updateCarFuelState();
        if (!car.isPoweredOn()) {
            displayAlert("Mobil masih mati. Nyalakan mobil terlebih dahulu.");
            return;
        }
        if (car.getFuelTank().isEmpty()) {
            displayAlert("BBM habis. Isi bensin terlebih dahulu.");
            return;
        }

        if (!autopilot.isActive()) {
            car.enableAutopilot();
            autopilot.activate();
        }

        autopilot.simulateObjectDetection();
        car.setCurrentSpeed(autopilot.getCurrentSpeed());
        updateCarFuelState();
        saveFuel();
        displayAlert("Simulasi deteksi objek selesai.\n" + autopilot.getStatusReport());
    }

    private void handleEmergencyStop() {
        updateCarFuelState();
        if (!car.isPoweredOn()) {
            displayAlert("Mobil masih mati.");
            return;
        }

        car.disableAutopilot();
        autopilot.deactivate();
        while (car.getCurrentSpeed() > 0.0f) {
            car.applyBrake(5.0f);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        car.stop();
        saveFuel();
        displayAlert("Emergency stop manual dijalankan.");
    }

    private void handleTurnOn() {
        boolean turnedOn = car.turnOn();
        if (turnedOn) {
            displayAlert("Mesin berhasil dihidupkan.");
        } else {
            displayAlert("Mesin sudah hidup.");
        }
    }

    private void handleTurnOff() {
        boolean turnedOff = car.turnOff();
        if (turnedOff) {
            autopilot.deactivate();
            saveFuel();
            displayAlert("Mesin berhasil dimatikan.");
        } else if (car.getCurrentSpeed() > 0.0f) {
            displayAlert("Mobil harus berhenti sebelum mesin dimatikan.");
        } else {
            displayAlert("Mesin sudah mati.");
        }
    }

    private void handleFillFuel() {
        car.fillFuel();
        saveFuel();
        displayAlert("Bensin sudah penuh.");
    }

    private void handleActivateAutopilot() {
        updateCarFuelState();
        if (!car.isPoweredOn()) {
            displayAlert("Mobil masih mati. Nyalakan mobil terlebih dahulu.");
            return;
        }
        if (car.getFuelTank().isEmpty()) {
            displayAlert("BBM habis. Isi bensin terlebih dahulu.");
            return;
        }

        car.enableAutopilot();
        autopilot.activate();
        autopilot.update();
        car.setCurrentSpeed(autopilot.getCurrentSpeed());
        saveFuel();
        displayAlert("Autopilot aktif.");
    }

    private void handleDeactivateAutopilot() {
        car.disableAutopilot();
        autopilot.deactivate();
        saveFuel();
        displayAlert("Autopilot nonaktif.");
    }

    private void updateCarFuelState() {
        boolean fuelEmptied = car.updateFuelUsage();
        if (fuelEmptied && autopilot.isActive()) {
            autopilot.deactivate();
            displayAlert("BBM habis. Mobil berhenti, tetapi mesin tetap hidup.");
        }
        saveFuel();
    }

    private void saveFuel() {
        car.getFuelTank().save(fuelSavePath);
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
