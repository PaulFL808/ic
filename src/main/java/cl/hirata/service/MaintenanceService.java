package cl.hirata.service;

import cl.hirata.dao.MaintenanceDao;
import cl.hirata.model.MaintenanceRecord;
import java.util.List;
import java.util.Optional;

public class MaintenanceService {

    private final MaintenanceDao maintenanceDao;

    public MaintenanceService(MaintenanceDao maintenanceDao) {
        this.maintenanceDao = maintenanceDao;
    }

    public List<MaintenanceRecord> listMaintenance() {
        return maintenanceDao.findAll();
    }

    public Optional<MaintenanceRecord> findLatestByTruckId(int truckId) {
        return maintenanceDao.findLatestByTruckId(truckId);
    }

    public MaintenanceRecord save(MaintenanceRecord record) {
        validate(record);
        if (record.getId() == null) {
            return maintenanceDao.create(record);
        }
        maintenanceDao.update(record);
        return record;
    }

    public void delete(int id) {
        maintenanceDao.delete(id);
    }

    private void validate(MaintenanceRecord record) {
        if (record.getTruckId() == null) {
            throw new IllegalArgumentException("Debe seleccionar un camion.");
        }
        if (record.getMaintenanceDate() == null) {
            throw new IllegalArgumentException("La fecha del mantenimiento es obligatoria.");
        }
        if (record.getMaintenanceType() == null || record.getMaintenanceType().isBlank()) {
            throw new IllegalArgumentException("El tipo de mantenimiento es obligatorio.");
        }
        if (record.getMileageAtService() < 0) {
            throw new IllegalArgumentException("El kilometraje del mantenimiento no puede ser negativo.");
        }
    }
}
