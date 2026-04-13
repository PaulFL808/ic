package cl.hirata.dao;

import cl.hirata.model.Truck;
import java.util.List;
import java.util.Optional;

public interface TruckDao {

    List<Truck> findAll();

    Optional<Truck> findById(int id);

    Truck create(Truck truck);

    void update(Truck truck);

    void delete(int id);
}
