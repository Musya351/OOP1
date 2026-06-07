package autopilotcar.ui;

import autopilotcar.autopilot.AutopilotSystem;
import autopilotcar.model.Car;
import autopilotcar.model.FuelTank;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.file.Path;
import java.nio.file.Paths;

// Menampilkan input dan dashboard realtime untuk simulasi AutopilotCar.
public class DashboardGUI extends JFrame {
    private static final int DASHBOARD_REFRESH_MS = 500;
    private static final int AUTOPILOT_UPDATE_MS = 1000;

    private final Car car;
    private final AutopilotSystem autopilot;
    private final Path fuelSavePath;
    private final Timer dashboardTimer;
    private Timer emergencyStopTimer;
    private int autopilotElapsedMillis;
    private String currentInputState;

    private JPanel inputPanel;
    private JLabel gearValue;
    private JLabel speedValue;
    private JLabel modeValue;
    private JLabel turnSignalValue;
    private JLabel fuelValue;
    private JLabel engineValue;
    private JLabel autopilotValue;
    private JTextArea logArea;

    public DashboardGUI(Car car, AutopilotSystem autopilot) {
        this.car = car;
        this.autopilot = autopilot;
        this.fuelSavePath = Paths.get("fuel-level.txt");
        this.dashboardTimer = new Timer(DASHBOARD_REFRESH_MS, event -> refreshDashboard());
        this.currentInputState = "";

        setTitle("AutopilotCar Realtime Dashboard");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(920, 560));
        setLayout(new BorderLayout(12, 12));

        add(buildDashboardPanel(), BorderLayout.CENTER);
        add(buildInputPanel(), BorderLayout.WEST);
        add(buildLogPanel(), BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                saveFuel();
                dashboardTimer.stop();
                if (emergencyStopTimer != null) {
                    emergencyStopTimer.stop();
                }
            }
        });

        refreshDashboard();
        dashboardTimer.start();
    }

    private JPanel buildDashboardPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 12, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(16, 0, 0, 16));

        gearValue = createValueLabel();
        speedValue = createValueLabel();
        modeValue = createValueLabel();
        turnSignalValue = createValueLabel();
        fuelValue = createValueLabel();
        engineValue = createValueLabel();
        autopilotValue = createValueLabel();

        panel.add(createDashboardItem("Gigi", gearValue));
        panel.add(createDashboardItem("Kecepatan (KM/Jam)", speedValue));
        panel.add(createDashboardItem("Mode Berjalan", modeValue));
        panel.add(createDashboardItem("Lampu Sen", turnSignalValue));
        panel.add(createDashboardItem("Bensin", fuelValue));
        panel.add(createDashboardItem("Kondisi Mesin", engineValue));
        panel.add(createDashboardItem("Kondisi Autopilot", autopilotValue));

        JLabel realtimeValue = createValueLabel();
        realtimeValue.setText("Aktif");
        realtimeValue.setForeground(new Color(16, 120, 74));
        panel.add(createDashboardItem("Dashboard Realtime", realtimeValue));

        return panel;
    }

    private JPanel buildInputPanel() {
        inputPanel = new JPanel(new GridLayout(0, 1, 8, 8));
        inputPanel.setPreferredSize(new Dimension(250, 0));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 0, 8));
        updateInputButtons();
        return inputPanel;
    }

    private JScrollPane buildLogPanel() {
        logArea = new JTextArea(6, 20);
        logArea.setEditable(false);
        logArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Log Aktivitas"));
        return scrollPane;
    }

    private JPanel createDashboardItem(String title, JLabel valueLabel) {
        JPanel panel = new JPanel(new BorderLayout(4, 8));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(205, 210, 218)),
                BorderFactory.createEmptyBorder(14, 14, 14, 14)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(valueLabel, BorderLayout.CENTER);
        return panel;
    }

    private JLabel createValueLabel() {
        JLabel label = new JLabel("-", SwingConstants.LEFT);
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
        return label;
    }

    private JButton createButton(String text, java.awt.event.ActionListener listener) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.addActionListener(listener);
        return button;
    }

    private void refreshDashboard() {
        boolean fuelEmptied = car.updateFuelUsage();
        if (fuelEmptied && autopilot.isActive()) {
            autopilot.deactivate();
            appendLog("BBM habis. Mobil berhenti, mesin tetap hidup.");
        }

        if (autopilot.isActive()) {
            autopilotElapsedMillis += DASHBOARD_REFRESH_MS;
            if (autopilotElapsedMillis >= AUTOPILOT_UPDATE_MS) {
                autopilotElapsedMillis = 0;
                autopilot.update();
                car.setCurrentSpeed(autopilot.getCurrentSpeed());
            }
        } else {
            autopilotElapsedMillis = 0;
        }

        FuelTank fuelTank = car.getFuelTank();
        gearValue.setText(car.getGearDisplay());
        speedValue.setText(String.format("%.1f", car.getCurrentSpeed()));
        modeValue.setText(autopilot.getMode().name());
        turnSignalValue.setText(car.getTurnSignal());
        fuelValue.setText(String.format("%.1f L / %.0f%%", fuelTank.getCurrentLiters(), fuelTank.getFuelPercentage()));
        engineValue.setText(car.isPoweredOn() ? "Hidup" : "Mati");
        autopilotValue.setText(autopilot.isActive() ? "Hidup" : "Mati");
        updateInputButtons();
        saveFuel();
    }

    private void updateInputButtons() {
        String nextState = getInputState();
        if (inputPanel == null || nextState.equals(currentInputState)) {
            return;
        }

        currentInputState = nextState;
        inputPanel.removeAll();

        if ("AUTOPILOT".equals(nextState)) {
            inputPanel.add(createButton("Set Cruise Control", event -> handleCruiseControl()));
            inputPanel.add(createButton("Simulasi Deteksi Objek", event -> handleObjectDetection()));
            inputPanel.add(createButton("Emergency Stop Manual", event -> handleEmergencyStop()));
            inputPanel.add(createButton("Nonaktifkan Autopilot", event -> handleDeactivateAutopilot()));
        } else if ("ENGINE_ON".equals(nextState)) {
            inputPanel.add(createButton("Matikan Mesin", event -> handleTurnOff()));
            inputPanel.add(createButton("Status Mobil", event -> handleStatus()));
            inputPanel.add(createButton("Tambah Kecepatan", event -> handleMoveForward()));
            inputPanel.add(createButton("Rem", event -> handleBrake()));
            inputPanel.add(createButton("Belok Kiri", event -> handleTurnLeft()));
            inputPanel.add(createButton("Belok Kanan", event -> handleTurnRight()));
            inputPanel.add(createButton("Lurus", event -> handleDriveStraight()));
            inputPanel.add(createButton("Mundur (Gigi R)", event -> handleMoveBackward()));
            inputPanel.add(createButton("Aktifkan Autopilot", event -> handleActivateAutopilot()));
        } else {
            inputPanel.add(createButton("Hidupkan Mesin", event -> handleTurnOn()));
            inputPanel.add(createButton("Isi Bensin", event -> handleFillFuel()));
            inputPanel.add(createButton("Keluar", event -> handleExit()));
        }

        inputPanel.revalidate();
        inputPanel.repaint();
    }

    private String getInputState() {
        if (autopilot.isActive()) {
            return "AUTOPILOT";
        }
        if (car.isPoweredOn()) {
            return "ENGINE_ON";
        }
        return "INITIAL";
    }

    private void handleTurnOn() {
        if (car.turnOn()) {
            appendLog("Mesin dihidupkan.");
        } else {
            appendLog("Mesin gagal dihidupkan atau sudah hidup.");
        }
        refreshDashboard();
    }

    private void handleTurnOff() {
        if (car.turnOff()) {
            autopilot.deactivate();
            appendLog("Mesin dimatikan.");
        } else {
            appendLog("Mesin tidak bisa dimatikan saat mobil masih bergerak.");
        }
        refreshDashboard();
    }

    private void handleStatus() {
        JTextArea statusArea = new JTextArea(buildStatusReport());
        statusArea.setEditable(false);
        statusArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        statusArea.setBackground(new Color(248, 250, 252));
        statusArea.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JScrollPane scrollPane = new JScrollPane(statusArea);
        scrollPane.setPreferredSize(new Dimension(560, 430));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(205, 210, 218)));

        JOptionPane.showMessageDialog(this, scrollPane, "Status Mobil", JOptionPane.INFORMATION_MESSAGE);
        appendLog("Status mobil ditampilkan.");
        refreshDashboard();
    }

    private String buildStatusReport() {
        FuelTank fuelTank = car.getFuelTank();
        StringBuilder report = new StringBuilder();

        report.append("STATUS MOBIL").append(System.lineSeparator());
        report.append("============").append(System.lineSeparator()).append(System.lineSeparator());

        appendStatusLine(report, "Nomor Registrasi", car.getRegistrationNum());
        appendStatusLine(report, "Nomor Polisi", car.getLicenseNumber());
        appendStatusLine(report, "Tahun", String.valueOf(car.getYear()));
        report.append(System.lineSeparator());

        report.append("KONDISI SAAT INI").append(System.lineSeparator());
        report.append("----------------").append(System.lineSeparator());
        appendStatusLine(report, "Mesin", car.isPoweredOn() ? "Hidup" : "Mati");
        appendStatusLine(report, "Autopilot", autopilot.isActive() ? "Hidup" : "Mati");
        appendStatusLine(report, "Mode", autopilot.getMode().name());
        appendStatusLine(report, "Kecepatan", String.format("%.1f KM/Jam", car.getCurrentSpeed()));
        appendStatusLine(report, "Gigi", car.getGearDisplay());
        appendStatusLine(report, "Lampu Sen", car.getTurnSignal());
        report.append(System.lineSeparator());

        report.append("BENSIN").append(System.lineSeparator());
        report.append("------").append(System.lineSeparator());
        appendStatusLine(report, "Isi Tangki", String.format("%.1f / %.1f Liter", fuelTank.getCurrentLiters(), fuelTank.getCapacityLiters()));
        appendStatusLine(report, "Persentase", String.format("%.0f%%", fuelTank.getFuelPercentage()));
        report.append(System.lineSeparator());

        report.append("AUTOPILOT").append(System.lineSeparator());
        report.append("---------").append(System.lineSeparator());
        appendStatusLine(report, "Target Cruise", String.format("%.1f KM/Jam", autopilot.getTargetSpeed()));
        appendStatusLine(report, "Jarak Aman", String.format("%.1f Meter", autopilot.getMinSafeDistance()));
        appendStatusLine(report, "Speed Sensor", String.format("%.1f KM/Jam", autopilot.getCurrentSpeed()));
        report.append(System.lineSeparator());

        report.append("KOMPONEN UTAMA").append(System.lineSeparator());
        report.append("--------------").append(System.lineSeparator());
        appendStatusLine(report, "Engine", car.getEngine().toString());
        appendStatusLine(report, "Gearbox", "Gigi " + car.getGearDisplay());
        appendStatusLine(report, "Body", car.getBody().toString());
        appendStatusLine(report, "Brake", car.getBrake().toString());
        appendStatusLine(report, "Jumlah Roda", String.valueOf(car.getWheels().length));

        return report.toString();
    }

    private void appendStatusLine(StringBuilder report, String label, String value) {
        report.append(String.format("%-17s: %s%n", label, value));
    }

    private void handleFillFuel() {
        car.fillFuel();
        appendLog("Bensin diisi penuh.");
        refreshDashboard();
    }

    private void handleActivateAutopilot() {
        if (!car.isPoweredOn()) {
            appendLog("Nyalakan mesin sebelum mengaktifkan autopilot.");
            return;
        }
        if (car.getFuelTank().isEmpty()) {
            appendLog("BBM habis. Isi bensin terlebih dahulu.");
            return;
        }

        car.enableAutopilot();
        autopilot.activate();
        autopilot.update();
        car.setCurrentSpeed(autopilot.getCurrentSpeed());
        appendLog("Autopilot aktif.");
        refreshDashboard();
    }

    private void handleCruiseControl() {
        if (!car.isPoweredOn() || car.getFuelTank().isEmpty()) {
            appendLog("Cruise Control membutuhkan mesin hidup dan BBM tersedia.");
            return;
        }

        String input = JOptionPane.showInputDialog(this, "Masukkan target speed:", autopilot.getTargetSpeed());
        if (input == null) {
            return;
        }

        try {
            float targetSpeed = Float.parseFloat(input.trim());
            if (!autopilot.isActive()) {
                car.enableAutopilot();
                autopilot.activate();
            }
            autopilot.setCruiseSpeed(targetSpeed);
            appendLog("Cruise Control diset ke " + targetSpeed + " KM/Jam.");
            refreshDashboard();
        } catch (NumberFormatException exception) {
            appendLog("Target speed harus berupa angka.");
        }
    }

    private void handleObjectDetection() {
        if (!autopilot.isActive()) {
            appendLog("Aktifkan autopilot sebelum simulasi deteksi objek.");
            return;
        }

        appendLog("Simulasi deteksi objek dijalankan.");
        new Thread(() -> {
            autopilot.simulateObjectDetection();
            car.setCurrentSpeed(autopilot.getCurrentSpeed());
            SwingUtilities.invokeLater(() -> {
                appendLog("Simulasi deteksi objek selesai.");
                refreshDashboard();
            });
        }).start();
    }

    private void handleEmergencyStop() {
        if (!car.isPoweredOn()) {
            appendLog("Mesin masih mati.");
            return;
        }
        if (emergencyStopTimer != null && emergencyStopTimer.isRunning()) {
            appendLog("Emergency stop sedang berjalan.");
            return;
        }

        car.disableAutopilot();
        autopilot.deactivate();
        appendLog("Emergency stop manual dimulai. Kecepatan turun 5 KM/Jam per detik.");
        emergencyStopTimer = new Timer(1000, event -> {
            if (car.getCurrentSpeed() <= 0.0f) {
                emergencyStopTimer.stop();
                car.stop();
                appendLog("Emergency stop selesai. Mobil berhenti.");
                refreshDashboard();
                return;
            }

            car.applyBrake(5.0f);
            refreshDashboard();
        });
        emergencyStopTimer.setInitialDelay(0);
        emergencyStopTimer.start();
    }

    private void handleDeactivateAutopilot() {
        car.disableAutopilot();
        autopilot.deactivate();
        appendLog("Autopilot dinonaktifkan.");
        refreshDashboard();
    }

    private void handleMoveForward() {
        boolean wasReverseMode = car.isReverseMode();
        car.moveForward();
        if (wasReverseMode) {
            appendLog("Kecepatan mundur bertambah.");
        } else {
            appendLog("Mobil maju / tambah kecepatan.");
        }
        refreshDashboard();
    }

    private void handleBrake() {
        car.applyBrake();
        appendLog("Rem digunakan. Kecepatan berkurang.");
        refreshDashboard();
    }

    private void handleMoveBackward() {
        if (car.getCurrentSpeed() > 0.0f) {
            appendLog("Gigi R hanya bisa digunakan saat mobil diam.");
            refreshDashboard();
            return;
        }

        car.moveBackward();
        if (car.isReverseMode()) {
            appendLog("Mobil mundur. Gigi R aktif.");
        }
        refreshDashboard();
    }

    private void handleTurnLeft() {
        car.turnLeft();
        appendLog("Lampu sen kiri aktif.");
        refreshDashboard();
    }

    private void handleDriveStraight() {
        car.driveStraight();
        appendLog("Mobil lurus. Lampu sen mati.");
        refreshDashboard();
    }

    private void handleTurnRight() {
        car.turnRight();
        appendLog("Lampu sen kanan aktif.");
        refreshDashboard();
    }

    private void appendLog(String message) {
        logArea.append(message + System.lineSeparator());
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    private void saveFuel() {
        car.getFuelTank().save(fuelSavePath);
    }

    private void handleExit() {
        appendLog("Program GUI ditutup.");
        shutdownGui();
    }

    private void shutdownGui() {
        saveFuel();
        dashboardTimer.stop();
        if (emergencyStopTimer != null) {
            emergencyStopTimer.stop();
        }
        dispose();
        System.exit(0);
    }

    public static void show(Car car, AutopilotSystem autopilot) {
        SwingUtilities.invokeLater(() -> {
            DashboardGUI dashboardGUI = new DashboardGUI(car, autopilot);
            dashboardGUI.setLocationRelativeTo(null);
            dashboardGUI.setVisible(true);
        });
    }
}
