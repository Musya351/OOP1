package autopilotcar.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Merepresentasikan gearbox dan logika perpindahan gigi sederhana.
public class GearBox {
    private float[] gearRatio;
    private int currentGear;
    private List<GearBoxType> gearBoxTypes;

    public GearBox(float[] gearRatio, int currentGear, List<GearBoxType> gearBoxTypes) {
        this.gearRatio = Arrays.copyOf(gearRatio, gearRatio.length);
        this.currentGear = currentGear;
        this.gearBoxTypes = new ArrayList<>(gearBoxTypes);
    }

    public float[] getGearRatio() {
        return Arrays.copyOf(gearRatio, gearRatio.length);
    }

    public void setGearRatio(float[] gearRatio) {
        this.gearRatio = Arrays.copyOf(gearRatio, gearRatio.length);
    }

    public int getCurrentGear() {
        return currentGear;
    }

    public void setCurrentGear(int currentGear) {
        if (currentGear >= 0 && currentGear <= gearRatio.length) {
            this.currentGear = currentGear;
        }
    }

    public List<GearBoxType> getGearBoxTypes() {
        return new ArrayList<>(gearBoxTypes);
    }

    public void setGearBoxTypes(List<GearBoxType> gearBoxTypes) {
        this.gearBoxTypes = new ArrayList<>(gearBoxTypes);
    }

    public void shiftUp() {
        if (currentGear < gearRatio.length) {
            currentGear++;
        }
        System.out.println("Gear shifted up to: " + currentGear);
    }

    public void shiftDown() {
        if (currentGear > 0) {
            currentGear--;
        }
        System.out.println("Gear shifted down to: " + currentGear);
    }

    public void autoShift(float speed) {
        if (speed <= 0) {
            currentGear = 0;
        } else if (speed < 20) {
            currentGear = Math.min(1, gearRatio.length);
        } else if (speed < 40) {
            currentGear = Math.min(2, gearRatio.length);
        } else if (speed < 60) {
            currentGear = Math.min(3, gearRatio.length);
        } else if (speed < 80) {
            currentGear = Math.min(4, gearRatio.length);
        } else {
            currentGear = gearRatio.length;
        }
        System.out.println("Auto shift selected gear: " + currentGear + " at speed: " + speed);
    }

    @Override
    public String toString() {
        return "GearBox{" +
                "gearRatio=" + Arrays.toString(gearRatio) +
                ", currentGear=" + currentGear +
                ", gearBoxTypes=" + gearBoxTypes +
                '}';
    }
}
