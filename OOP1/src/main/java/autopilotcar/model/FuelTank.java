package autopilotcar.model;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

// Merepresentasikan tangki bensin dengan kapasitas dan penyimpanan sederhana.
public class FuelTank {
    public static final float CAPACITY_LITERS = 40.0f;
    private float currentLiters;

    public FuelTank(float currentLiters) {
        setCurrentLiters(currentLiters);
    }

    public float getCapacityLiters() {
        return CAPACITY_LITERS;
    }

    public float getCurrentLiters() {
        return currentLiters;
    }

    public void setCurrentLiters(float currentLiters) {
        if (currentLiters < 0.0f) {
            this.currentLiters = 0.0f;
        } else if (currentLiters > CAPACITY_LITERS) {
            this.currentLiters = CAPACITY_LITERS;
        } else {
            this.currentLiters = currentLiters;
        }
    }

    public float getFuelPercentage() {
        return (currentLiters / CAPACITY_LITERS) * 100.0f;
    }

    public boolean isEmpty() {
        return currentLiters <= 0.0f;
    }

    public void fillFull() {
        currentLiters = CAPACITY_LITERS;
    }

    public boolean consume(float liters) {
        if (liters <= 0.0f || isEmpty()) {
            return false;
        }

        currentLiters = Math.max(0.0f, currentLiters - liters);
        return isEmpty();
    }

    public void save(Path path) {
        try {
            Path parent = path.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Files.write(path, Float.toString(currentLiters).getBytes(StandardCharsets.UTF_8));
        } catch (IOException exception) {
            System.out.println("Gagal menyimpan data bensin: " + exception.getMessage());
        }
    }

    public static FuelTank load(Path path) {
        if (!Files.exists(path)) {
            return new FuelTank(CAPACITY_LITERS);
        }

        try {
            String value = new String(Files.readAllBytes(path), StandardCharsets.UTF_8).trim();
            return new FuelTank(Float.parseFloat(value));
        } catch (IOException | NumberFormatException exception) {
            System.out.println("Gagal membaca data bensin. Tangki diisi penuh sebagai default.");
            return new FuelTank(CAPACITY_LITERS);
        }
    }

    @Override
    public String toString() {
        return "FuelTank{" +
                "capacityLiters=" + CAPACITY_LITERS +
                ", currentLiters=" + currentLiters +
                ", fuelPercentage=" + getFuelPercentage() +
                '}';
    }
}
