package cl.hirata.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import cl.hirata.dao.MileageLogDao;
import cl.hirata.dao.TruckDao;
import cl.hirata.model.MileageLog;
import cl.hirata.model.Truck;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class MileageServiceTest {

    @Test
    void shouldUpdateTruckMileageAndPersistLog() {
        StubTruckDao truckDao = new StubTruckDao(new Truck(1, "Scania", "R450", 2021, null, null, 1200));
        StubMileageLogDao mileageLogDao = new StubMileageLogDao();
        MileageService service = new MileageService(truckDao, mileageLogDao);

        MileageLog result = service.registerMileage(1, 350, LocalDate.of(2026, 4, 12), "Ruta norte");

        assertEquals(1550, truckDao.storedTruck.getCurrentMileage());
        assertNotNull(result.getId());
        assertEquals(1550, result.getTotalMileageAfterTrip());
        assertEquals("Ruta norte", mileageLogDao.lastCreatedLog.getNotes());
    }

    @Test
    void shouldRejectNonPositiveMileage() {
        StubTruckDao truckDao = new StubTruckDao(new Truck(1, "Scania", "R450", 2021, null, null, 1200));
        MileageService service = new MileageService(truckDao, new StubMileageLogDao());

        assertThrows(IllegalArgumentException.class,
                () -> service.registerMileage(1, 0, LocalDate.of(2026, 4, 12), ""));
    }

    private static class StubTruckDao implements TruckDao {

        private Truck storedTruck;

        private StubTruckDao(Truck storedTruck) {
            this.storedTruck = storedTruck;
        }

        @Override
        public List<Truck> findAll() {
            return List.of(storedTruck);
        }

        @Override
        public Optional<Truck> findById(int id) {
            return storedTruck.getId() == id ? Optional.of(storedTruck) : Optional.empty();
        }

        @Override
        public Truck create(Truck truck) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void update(Truck truck) {
            this.storedTruck = truck;
        }

        @Override
        public void delete(int id) {
            throw new UnsupportedOperationException();
        }
    }

    private static class StubMileageLogDao implements MileageLogDao {

        private MileageLog lastCreatedLog;
        private int nextId = 1;

        @Override
        public MileageLog create(MileageLog mileageLog) {
            mileageLog.setId(nextId++);
            lastCreatedLog = mileageLog;
            return mileageLog;
        }

        @Override
        public List<MileageLog> findByTruckId(int truckId) {
            return lastCreatedLog == null ? List.of() : List.of(lastCreatedLog);
        }
    }
}
