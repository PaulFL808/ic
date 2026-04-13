package cl.hirata.service;

import cl.hirata.dao.DriverDao;
import cl.hirata.dao.TruckDao;
import cl.hirata.model.Driver;
import cl.hirata.model.Truck;
import java.util.List;

public class FleetService {

    private final DriverDao driverDao;
    private final TruckDao truckDao;

    public FleetService(DriverDao driverDao, TruckDao truckDao) {
        this.driverDao = driverDao;
        this.truckDao = truckDao;
    }

    public List<Driver> listDrivers() {
        return driverDao.findAll();
    }

    public Driver saveDriver(Driver driver) {
        validateDriver(driver);
        if (driver.getId() == null) {
            return driverDao.create(driver);
        }
        driverDao.update(driver);
        return driver;
    }

    public void deleteDriver(int id) {
        driverDao.delete(id);
    }

    public List<Truck> listTrucks() {
        return truckDao.findAll();
    }

    public Truck saveTruck(Truck truck) {
        validateTruck(truck);
        if (truck.getId() == null) {
            return truckDao.create(truck);
        }
        truckDao.update(truck);
        return truck;
    }

    public void deleteTruck(int id) {
        truckDao.delete(id);
    }

    private void validateDriver(Driver driver) {
        if (driver.getName() == null || driver.getName().isBlank()) {
            throw new IllegalArgumentException("El nombre del conductor es obligatorio.");
        }
        if (driver.getLicenseNumber() == null || driver.getLicenseNumber().isBlank()) {
            throw new IllegalArgumentException("El numero de licencia es obligatorio.");
        }
    }

    private void validateTruck(Truck truck) {
        if (truck.getBrand() == null || truck.getBrand().isBlank()) {
            throw new IllegalArgumentException("La marca del camion es obligatoria.");
        }
        if (truck.getModel() == null || truck.getModel().isBlank()) {
            throw new IllegalArgumentException("El modelo del camion es obligatorio.");
        }
        if (truck.getManufactureYear() < 1980) {
            throw new IllegalArgumentException("El ano del camion debe ser valido.");
        }
        if (truck.getCurrentMileage() < 0) {
            throw new IllegalArgumentException("El kilometraje actual no puede ser negativo.");
        }
    }
}
