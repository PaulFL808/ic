package cl.hirata.model;

import java.time.LocalDate;

public class MileageLog {

    private Integer id;
    private Integer truckId;
    private LocalDate tripDate;
    private int kilometersTravelled;
    private int totalMileageAfterTrip;
    private String notes;

    public MileageLog() {
    }

    public MileageLog(Integer id, Integer truckId, LocalDate tripDate, int kilometersTravelled,
                      int totalMileageAfterTrip, String notes) {
        this.id = id;
        this.truckId = truckId;
        this.tripDate = tripDate;
        this.kilometersTravelled = kilometersTravelled;
        this.totalMileageAfterTrip = totalMileageAfterTrip;
        this.notes = notes;
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

    public LocalDate getTripDate() {
        return tripDate;
    }

    public void setTripDate(LocalDate tripDate) {
        this.tripDate = tripDate;
    }

    public int getKilometersTravelled() {
        return kilometersTravelled;
    }

    public void setKilometersTravelled(int kilometersTravelled) {
        this.kilometersTravelled = kilometersTravelled;
    }

    public int getTotalMileageAfterTrip() {
        return totalMileageAfterTrip;
    }

    public void setTotalMileageAfterTrip(int totalMileageAfterTrip) {
        this.totalMileageAfterTrip = totalMileageAfterTrip;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
