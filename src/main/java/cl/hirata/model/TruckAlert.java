package cl.hirata.model;

public class TruckAlert {

    private final Integer truckId;
    private final String truckDisplayName;
    private final int currentMileage;
    private final int lastMaintenanceMileage;
    private final int kilometersSinceMaintenance;
    private final boolean requiresMaintenance;
    private final String message;

    public TruckAlert(Integer truckId, String truckDisplayName, int currentMileage, int lastMaintenanceMileage,
                      int kilometersSinceMaintenance, boolean requiresMaintenance, String message) {
        this.truckId = truckId;
        this.truckDisplayName = truckDisplayName;
        this.currentMileage = currentMileage;
        this.lastMaintenanceMileage = lastMaintenanceMileage;
        this.kilometersSinceMaintenance = kilometersSinceMaintenance;
        this.requiresMaintenance = requiresMaintenance;
        this.message = message;
    }

    public Integer getTruckId() {
        return truckId;
    }

    public String getTruckDisplayName() {
        return truckDisplayName;
    }

    public int getCurrentMileage() {
        return currentMileage;
    }

    public int getLastMaintenanceMileage() {
        return lastMaintenanceMileage;
    }

    public int getKilometersSinceMaintenance() {
        return kilometersSinceMaintenance;
    }

    public boolean isRequiresMaintenance() {
        return requiresMaintenance;
    }

    public String getMessage() {
        return message;
    }
}
