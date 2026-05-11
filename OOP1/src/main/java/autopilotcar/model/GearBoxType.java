package autopilotcar.model;

// Merepresentasikan tipe gearbox yang dapat digunakan mobil.
public class GearBoxType {
    private String name;
    private String remarks;
    private boolean automatic;

    public GearBoxType(String name, String remarks, boolean automatic) {
        this.name = name;
        this.remarks = remarks;
        this.automatic = automatic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public boolean isAutomatic() {
        return automatic;
    }

    public void setAutomatic(boolean automatic) {
        this.automatic = automatic;
    }

    @Override
    public String toString() {
        return "GearBoxType{" +
                "name='" + name + '\'' +
                ", remarks='" + remarks + '\'' +
                ", automatic=" + automatic +
                '}';
    }
}
