package cl.hirata.service;

import cl.hirata.dao.MaintenanceDao;
import cl.hirata.model.MaintenanceRecord;
import cl.hirata.model.Truck;
import cl.hirata.model.TruckAlert;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AlertService {

    public static final int MAINTENANCE_THRESHOLD_KM = 5000;

    private final MaintenanceDao maintenanceDao;

    public AlertService(MaintenanceDao maintenanceDao) {
        this.maintenanceDao = maintenanceDao;
    }

    public TruckAlert buildAlert(Truck truck) {
        Optional<MaintenanceRecord> latest = maintenanceDao.findLatestByTruckId(truck.getId());
        int lastMaintenanceMileage = latest.map(MaintenanceRecord::getMileageAtService).orElse(0);
        int kilometersSinceMaintenance = Math.max(0, truck.getCurrentMileage() - lastMaintenanceMileage);
        boolean requiresMaintenance = kilometersSinceMaintenance >= MAINTENANCE_THRESHOLD_KM;
        String message = requiresMaintenance
                ? "Requiere mantenimiento preventivo"
                : "Operacion normal";

        return new TruckAlert(
                truck.getId(),
                truck.getDisplayName(),
                truck.getCurrentMileage(),
                lastMaintenanceMileage,
                kilometersSinceMaintenance,
                requiresMaintenance,
                message
        );
    }

    public List<TruckAlert> generateAlerts(List<Truck> trucks) {
        List<TruckAlert> alerts = new ArrayList<>();
        for (Truck truck : trucks) {
            TruckAlert alert = buildAlert(truck);
            if (alert.isRequiresMaintenance()) {
                alerts.add(alert);
            }
        }
        return alerts;
    }
}
