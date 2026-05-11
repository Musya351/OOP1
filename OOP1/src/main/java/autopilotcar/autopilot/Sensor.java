package autopilotcar.autopilot;

// Mendefinisikan kontrak dasar untuk semua sensor pada sistem autopilot.
public interface Sensor {
    void initialize();

    Object readData();

    boolean isReady();
}
