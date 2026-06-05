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
    private int autopilotElapsedMillis;

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
        JPanel panel = new JPanel(new GridLayout(0, 1, 8, 8));
        panel.setPreferredSize(new Dimension(250, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 0, 8));

        panel.add(createButton("Hidupkan Mesin", event -> handleTurnOn()));
        panel.add(createButton("Matikan Mesin", event -> handleTurnOff()));
        panel.add(createButton("Isi Bensin", event -> handleFillFuel()));
        panel.add(createButton("Aktifkan Autopilot", event -> handleActivateAutopilot()));
        panel.add(createButton("Set Cruise Control", event -> handleCruiseControl()));
        panel.add(createButton("Simulasi Deteksi Objek", event -> handleObjectDetection()));
        panel.add(createButton("Emergency Stop Manual", event -> handleEmergencyStop()));
        panel.add(createButton("Nonaktifkan Autopilot", event -> handleDeactivateAutopilot()));
        panel.add(createButton("Tambah Kecepatan", event -> handleMoveForward()));
        panel.add(createButton("Belok Kiri", event -> handleTurnLeft()));
        panel.add(createButton("Lurus", event -> handleDriveStraight()));
        panel.add(createButton("Belok Kanan", event -> handleTurnRight()));

        return panel;
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
        gearValue.setText(String.valueOf(car.getGearBox().getCurrentGear()));
        speedValue.setText(String.format("%.1f", car.getCurrentSpeed()));
        modeValue.setText(autopilot.getMode().name());
        turnSignalValue.setText(car.getTurnSignal());
        fuelValue.setText(String.format("%.1f L / %.0f%%", fuelTank.getCurrentLiters(), fuelTank.getFuelPercentage()));
        engineValue.setText(car.isPoweredOn() ? "Hidup" : "Mati");
        autopilotValue.setText(autopilot.isActive() ? "Hidup" : "Mati");
        saveFuel();
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

        autopilot.update();
        car.setCurrentSpeed(autopilot.getCurrentSpeed());
        appendLog("Simulasi deteksi objek dijalankan.");
        refreshDashboard();
    }

    private void handleEmergencyStop() {
        if (!car.isPoweredOn()) {
            appendLog("Mesin masih mati.");
            return;
        }

        car.stop();
        car.disableAutopilot();
        autopilot.deactivate();
        appendLog("Emergency stop manual dijalankan.");
        refreshDashboard();
    }

    private void handleDeactivateAutopilot() {
        car.disableAutopilot();
        autopilot.deactivate();
        appendLog("Autopilot dinonaktifkan.");
        refreshDashboard();
    }

    private void handleMoveForward() {
        car.moveForward();
        appendLog("Mobil maju / tambah kecepatan.");
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

    public static void show(Car car, AutopilotSystem autopilot) {
        SwingUtilities.invokeLater(() -> {
            DashboardGUI dashboardGUI = new DashboardGUI(car, autopilot);
            dashboardGUI.setLocationRelativeTo(null);
            dashboardGUI.setVisible(true);
        });
    }
}
