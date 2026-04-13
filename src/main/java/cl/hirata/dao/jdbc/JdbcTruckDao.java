package cl.hirata.dao.jdbc;

import cl.hirata.dao.TruckDao;
import cl.hirata.model.Truck;
import cl.hirata.util.DataAccessException;
import cl.hirata.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcTruckDao implements TruckDao {

    private final DatabaseConnection databaseConnection;

    public JdbcTruckDao(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @Override
    public List<Truck> findAll() {
        String sql = """
                SELECT t.id, t.brand, t.model, t.manufacture_year, t.assigned_driver_id, t.current_mileage,
                       d.name AS driver_name
                FROM trucks t
                LEFT JOIN drivers d ON d.id = t.assigned_driver_id
                ORDER BY t.id
                """;

        List<Truck> trucks = new ArrayList<>();
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                trucks.add(mapTruck(resultSet));
            }
            return trucks;
        } catch (SQLException e) {
            throw new DataAccessException("No fue posible listar los camiones.", e);
        }
    }

    @Override
    public Optional<Truck> findById(int id) {
        String sql = """
                SELECT t.id, t.brand, t.model, t.manufacture_year, t.assigned_driver_id, t.current_mileage,
                       d.name AS driver_name
                FROM trucks t
                LEFT JOIN drivers d ON d.id = t.assigned_driver_id
                WHERE t.id = ?
                """;
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapTruck(resultSet));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DataAccessException("No fue posible buscar el camion.", e);
        }
    }

    @Override
    public Truck create(Truck truck) {
        String sql = """
                INSERT INTO trucks(brand, model, manufacture_year, assigned_driver_id, current_mileage)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, truck.getBrand());
            statement.setString(2, truck.getModel());
            statement.setInt(3, truck.getManufactureYear());
            if (truck.getAssignedDriverId() == null) {
                statement.setNull(4, java.sql.Types.INTEGER);
            } else {
                statement.setInt(4, truck.getAssignedDriverId());
            }
            statement.setInt(5, truck.getCurrentMileage());
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    truck.setId(keys.getInt(1));
                }
            }
            return truck;
        } catch (SQLException e) {
            throw new DataAccessException("No fue posible crear el camion.", e);
        }
    }

    @Override
    public void update(Truck truck) {
        String sql = """
                UPDATE trucks
                SET brand = ?, model = ?, manufacture_year = ?, assigned_driver_id = ?, current_mileage = ?
                WHERE id = ?
                """;
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, truck.getBrand());
            statement.setString(2, truck.getModel());
            statement.setInt(3, truck.getManufactureYear());
            if (truck.getAssignedDriverId() == null) {
                statement.setNull(4, java.sql.Types.INTEGER);
            } else {
                statement.setInt(4, truck.getAssignedDriverId());
            }
            statement.setInt(5, truck.getCurrentMileage());
            statement.setInt(6, truck.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("No fue posible actualizar el camion.", e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM trucks WHERE id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("No fue posible eliminar el camion.", e);
        }
    }

    private Truck mapTruck(ResultSet resultSet) throws SQLException {
        int driverId = resultSet.getInt("assigned_driver_id");
        Integer assignedDriverId = resultSet.wasNull() ? null : driverId;

        return new Truck(
                resultSet.getInt("id"),
                resultSet.getString("brand"),
                resultSet.getString("model"),
                resultSet.getInt("manufacture_year"),
                assignedDriverId,
                resultSet.getString("driver_name"),
                resultSet.getInt("current_mileage")
        );
    }
}
