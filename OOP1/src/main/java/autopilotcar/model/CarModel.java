package autopilotcar.model;

import java.util.ArrayList;
import java.util.List;

// Merepresentasikan model mobil dan daftar mobil yang menggunakannya.
public class CarModel {
    private String title;
    private String brand;
    private List<Car> cars;

    public CarModel(String title, String brand, List<Car> cars) {
        this.title = title;
        this.brand = brand;
        this.cars = new ArrayList<>(cars);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public List<Car> getCars() {
        return new ArrayList<>(cars);
    }

    public void setCars(List<Car> cars) {
        this.cars = new ArrayList<>(cars);
    }

    @Override
    public String toString() {
        return "CarModel{" +
                "title='" + title + '\'' +
                ", brand='" + brand + '\'' +
                ", cars=" + cars +
                '}';
    }
}
