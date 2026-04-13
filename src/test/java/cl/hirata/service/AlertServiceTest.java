package cl.hirata.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cl.hirata.dao.MaintenanceDao;
import cl.hirata.model.MaintenanceRecord;
import cl.hirata.model.Truck;
import cl.hirata.model.TruckAlert;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class AlertServiceTest {

    @Test
    void shouldGenerateAlertWhenTruckReachesThresholdWithoutMaintenance() {
        MaintenanceDao maintenanceDao = new StubMaintenanceDao(Map.of());
        AlertService service = new AlertService(maintenanceDao);
        Truck truck = new Truck(1, "Volvo", "FH16", 2022, null, null, 5000);

        TruckAlert alert = service.buildAlert(truck);

        assertTrue(alert.isRequiresMaintenance());
        assertEquals(5000, alert.getKilometersSinceMaintenance());
    }

    @Test
    void shouldNotGenerateAlertWhenMileageSinceMaintenanceIsBelowThreshold() {
        MaintenanceDao maintenanceDao = new StubMaintenanceDao(Map.of(
                1, new MaintenanceRecord(1, 1, "Volvo FH16 (2022)", LocalDate.now(), "Aceite", 3000, "")
        ));
        AlertService service = new AlertService(maintenanceDao);
        Truck truck = new Truck(1, "Volvo", "FH16", 2022, null, null, 6500);

        TruckAlert alert = service.buildAlert(truck);

        assertFalse(alert.isRequiresMaintenance());
        assertEquals(3500, alert.getKilometersSinceMaintenance());
    }

    @Test
    void shouldReturnOnlyActiveAlerts() {
        MaintenanceDao maintenanceDao = new StubMaintenanceDao(Map.of(
                1, new MaintenanceRecord(1, 1, "Volvo FH16 (2022)", LocalDate.now(), "Aceite", 1000, ""),
                2, new MaintenanceRecord(2, 2, "Scania R450 (2021)", LocalDate.now(), "Filtros", 4000, "")
        ));
        AlertService service = new AlertService(maintenanceDao);

        List<TruckAlert> alerts = service.generateAlerts(List.of(
                new Truck(1, "Volvo", "FH16", 2022, null, null, 7000),
                new Truck(2, "Scania", "R450", 2021, null, null, 8200)
        ));

        assertEquals(1, alerts.size());
        assertEquals("Volvo FH16 (2022)", alerts.get(0).getTruckDisplayName());
    }

    private static class StubMaintenanceDao implements MaintenanceDao {

        private final Map<Integer, MaintenanceRecord> recordsByTruck;

        private StubMaintenanceDao(Map<Integer, MaintenanceRecord> recordsByTruck) {
            this.recordsByTruck = recordsByTruck;
        }

        @Override
        public List<MaintenanceRecord> findAll() {
            return List.copyOf(recordsByTruck.values());
        }

        @Override
        public Optional<MaintenanceRecord> findById(int id) {
            return recordsByTruck.values().stream().filter(record -> record.getId() == id).findFirst();
        }

        @Override
        public Optional<MaintenanceRecord> findLatestByTruckId(int truckId) {
            return Optional.ofNullable(recordsByTruck.get(truckId));
        }

        @Override
        public MaintenanceRecord create(MaintenanceRecord record) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void update(MaintenanceRecord record) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void delete(int id) {
            throw new UnsupportedOperationException();
        }
    }
}
