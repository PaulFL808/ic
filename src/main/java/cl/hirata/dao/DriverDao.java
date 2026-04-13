package cl.hirata.dao;

import cl.hirata.model.Driver;
import java.util.List;
import java.util.Optional;

public interface DriverDao {

    List<Driver> findAll();

    Optional<Driver> findById(int id);

    Driver create(Driver driver);

    void update(Driver driver);

    void delete(int id);
}
