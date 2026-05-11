package autopilotcar.autopilot;

// Menyimpan mode operasi yang tersedia pada sistem autopilot.
public enum AutopilotMode {
    /** Menjaga kecepatan kendaraan tetap stabil. */
    CRUISE_CONTROL,

    /** Menghindari potensi tabrakan dengan objek di sekitar. */
    COLLISION_AVOIDANCE,

    /** Menghentikan kendaraan secara darurat saat ancaman terdeteksi. */
    EMERGENCY_STOP,

    /** Memberikan kendali penuh kembali kepada pengemudi. */
    MANUAL_OVERRIDE,

    /** Menjaga kendaraan tetap berada di jalurnya. */
    LANE_KEEPING
}
