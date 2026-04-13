package cl.hirata.model;

import java.time.LocalDate;

public class MaintenanceRecord {

    private Integer id;
    private Integer truckId;
    private String truckDisplayName;
    private LocalDate maintenanceDate;
    private String maintenanceType;
    private int mileageAtService;
    private String observations;

    public MaintenanceRecord() {
    }

    public MaintenanceRecord(Integer id, Integer truckId, String truckDisplayName, LocalDate maintenanceDate,
                             String maintenanceType, int mileageAtService, String observations) {
        this.id = id;
        this.truckId = truckId;
        this.truckDisplayName = truckDisplayName;
        this.maintenanceDate = maintenanceDate;
        this.maintenanceType = maintenanceType;
        this.mileageAtService = mileageAtService;
        this.observations = observations;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTruckId() {
        return truckId;
    }

    public void setTruckId(Integer truckId) {
        this.truckId = truckId;
    }

    public String getTruckDisplayName() {
        return truckDisplayName;
    }

    public void setTruckDisplayName(String truckDisplayName) {
        this.truckDisplayName = truckDisplayName;
    }

    public LocalDate getMaintenanceDate() {
        return maintenanceDate;
    }

    public void setMaintenanceDate(LocalDate maintenanceDate) {
        this.maintenanceDate = maintenanceDate;
    }

    public String getMaintenanceType() {
        return maintenanceType;
    }

    public void setMaintenanceType(String maintenanceType) {
        this.maintenanceType = maintenanceType;
    }

    public int getMileageAtService() {
        return mileageAtService;
    }

    public void setMileageAtService(int mileageAtService) {
        this.mileageAtService = mileageAtService;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }
}
