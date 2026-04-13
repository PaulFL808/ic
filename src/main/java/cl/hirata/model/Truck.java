package cl.hirata.model;

public class Truck {

    private Integer id;
    private String brand;
    private String model;
    private int manufactureYear;
    private Integer assignedDriverId;
    private String assignedDriverName;
    private int currentMileage;

    public Truck() {
    }

    public Truck(Integer id, String brand, String model, int manufactureYear, Integer assignedDriverId,
                 String assignedDriverName, int currentMileage) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.manufactureYear = manufactureYear;
        this.assignedDriverId = assignedDriverId;
        this.assignedDriverName = assignedDriverName;
        this.currentMileage = currentMileage;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getManufactureYear() {
        return manufactureYear;
    }

    public void setManufactureYear(int manufactureYear) {
        this.manufactureYear = manufactureYear;
    }

    public Integer getAssignedDriverId() {
        return assignedDriverId;
    }

    public void setAssignedDriverId(Integer assignedDriverId) {
        this.assignedDriverId = assignedDriverId;
    }

    public String getAssignedDriverName() {
        return assignedDriverName;
    }

    public void setAssignedDriverName(String assignedDriverName) {
        this.assignedDriverName = assignedDriverName;
    }

    public int getCurrentMileage() {
        return currentMileage;
    }

    public void setCurrentMileage(int currentMileage) {
        this.currentMileage = currentMileage;
    }

    public String getDisplayName() {
        return brand + " " + model + " (" + manufactureYear + ")";
    }

    @Override
    public String toString() {
        return getDisplayName();
    }
}
