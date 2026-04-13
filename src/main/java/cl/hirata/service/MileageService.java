package cl.hirata.service;

import cl.hirata.dao.MileageLogDao;
import cl.hirata.dao.TruckDao;
import cl.hirata.model.MileageLog;
import cl.hirata.model.Truck;
import java.time.LocalDate;

public class MileageService {

    private final TruckDao truckDao;
    private final MileageLogDao mileageLogDao;

    public MileageService(TruckDao truckDao, MileageLogDao mileageLogDao) {
        this.truckDao = truckDao;
        this.mileageLogDao = mileageLogDao;
    }

    public MileageLog registerMileage(int truckId, int kilometersTravelled, LocalDate tripDate, String notes) {
        if (kilometersTravelled <= 0) {
            throw new IllegalArgumentException("El kilometraje ingresado debe ser mayor que cero.");
        }
        if (tripDate == null) {
            throw new IllegalArgumentException("La fecha del viaje es obligatoria.");
        }

        Truck truck = truckDao.findById(truckId)
                .orElseThrow(() -> new IllegalArgumentException("No se encontro el camion seleccionado."));

        int updatedMileage = truck.getCurrentMileage() + kilometersTravelled;
        truck.setCurrentMileage(updatedMileage);
        truckDao.update(truck);

        MileageLog mileageLog = new MileageLog(
                null,
                truckId,
                tripDate,
                kilometersTravelled,
                updatedMileage,
                notes
        );
        return mileageLogDao.create(mileageLog);
    }
}
