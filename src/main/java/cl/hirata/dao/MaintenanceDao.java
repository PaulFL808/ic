package cl.hirata.dao;

import cl.hirata.model.MaintenanceRecord;
import java.util.List;
import java.util.Optional;

public interface MaintenanceDao {

    List<MaintenanceRecord> findAll();

    Optional<MaintenanceRecord> findById(int id);

    Optional<MaintenanceRecord> findLatestByTruckId(int truckId);

    MaintenanceRecord create(MaintenanceRecord record);

    void update(MaintenanceRecord record);

    void delete(int id);
}
