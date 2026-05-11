package autopilotcar.autopilot;

import java.util.Arrays;

// Mensimulasikan sensor kecepatan kendaraan dan riwayat pembacaannya.
public class SpeedSensor implements Sensor {
    private float currentSpeed;
    private float maxSpeed;
    private float[] speedHistory;
    private boolean ready;

    public SpeedSensor(float currentSpeed, float maxSpeed) {
        this.currentSpeed = currentSpeed;
        this.maxSpeed = maxSpeed;
        this.speedHistory = new float[10];
        this.ready = false;
    }

    public float getCurrentSpeed() {
        return currentSpeed;
    }

    public void setCurrentSpeed(float currentSpeed) {
        this.currentSpeed = currentSpeed;
        updateSpeedHistory(currentSpeed);
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public float[] getSpeedHistory() {
        return Arrays.copyOf(speedHistory, speedHistory.length);
    }

    public float getSpeed() {
        return currentSpeed;
    }

    public boolean isOverLimit() {
        return currentSpeed > maxSpeed;
    }

    public int getOptimalGear() {
        if (currentSpeed <= 20.0f) {
            return 1;
        }
        if (currentSpeed <= 40.0f) {
            return 2;
        }
        if (currentSpeed <= 60.0f) {
            return 3;
        }
        if (currentSpeed <= 80.0f) {
            return 4;
        }
        return 5;
    }

    public boolean maintainSpeed(float target) {
        return Math.abs(currentSpeed - target) <= 2.0f;
    }

    private void updateSpeedHistory(float speed) {
        for (int i = speedHistory.length - 1; i > 0; i--) {
            speedHistory[i] = speedHistory[i - 1];
        }
        speedHistory[0] = speed;
    }

    @Override
    public void initialize() {
        ready = true;
        Arrays.fill(speedHistory, 0.0f);
        updateSpeedHistory(currentSpeed);
    }

    @Override
    public Object readData() {
        if (!ready) {
            initialize();
        } else {
            updateSpeedHistory(currentSpeed);
        }
        return currentSpeed;
    }

    @Override
    public boolean isReady() {
        return ready;
    }
}
