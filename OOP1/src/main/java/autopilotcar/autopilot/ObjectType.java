package autopilotcar.autopilot;

// Menyimpan kategori objek yang dapat dikenali sistem autopilot.
public enum ObjectType {
    /** Menunjukkan objek berupa kendaraan lain. */
    VEHICLE,

    /** Menunjukkan objek berupa pejalan kaki. */
    PEDESTRIAN,

    /** Menunjukkan objek berupa hambatan umum di jalan. */
    OBSTACLE,

    /** Menunjukkan objek berupa rambu lalu lintas. */
    TRAFFIC_SIGN,

    /** Menunjukkan objek yang belum dapat diklasifikasikan. */
    UNKNOWN
}
