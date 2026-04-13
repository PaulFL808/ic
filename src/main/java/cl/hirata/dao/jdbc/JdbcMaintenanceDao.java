package cl.hirata.dao.jdbc;

import cl.hirata.dao.MaintenanceDao;
import cl.hirata.model.MaintenanceRecord;
import cl.hirata.util.DataAccessException;
import cl.hirata.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcMaintenanceDao implements MaintenanceDao {

    private final DatabaseConnection databaseConnection;

    public JdbcMaintenanceDao(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @Override
    public List<MaintenanceRecord> findAll() {
        String sql = """
                SELECT m.id, m.truck_id, m.maintenance_date, m.maintenance_type, m.mileage_at_service, m.observations,
                       CONCAT(t.brand, ' ', t.model, ' (', t.manufacture_year, ')') AS truck_name
                FROM maintenance_records m
                INNER JOIN trucks t ON t.id = m.truck_id
                ORDER BY m.maintenance_date DESC, m.id DESC
                """;
        List<MaintenanceRecord> records = new ArrayList<>();
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                records.add(mapRecord(resultSet));
            }
            return records;
        } catch (SQLException e) {
            throw new DataAccessException("No fue posible listar los mantenimientos.", e);
        }
    }

    @Override
    public Optional<MaintenanceRecord> findById(int id) {
        String sql = """
                SELECT m.id, m.truck_id, m.maintenance_date, m.maintenance_type, m.mileage_at_service, m.observations,
                       CONCAT(t.brand, ' ', t.model, ' (', t.manufacture_year, ')') AS truck_name
                FROM maintenance_records m
                INNER JOIN trucks t ON t.id = m.truck_id
                WHERE m.id = ?
                """;
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRecord(resultSet));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DataAccessException("No fue posible buscar el mantenimiento.", e);
        }
    }

    @Override
    public Optional<MaintenanceRecord> findLatestByTruckId(int truckId) {
        String sql = """
                SELECT m.id, m.truck_id, m.maintenance_date, m.maintenance_type, m.mileage_at_service, m.observations,
                       CONCAT(t.brand, ' ', t.model, ' (', t.manufacture_year, ')') AS truck_name
                FROM maintenance_records m
                INNER JOIN trucks t ON t.id = m.truck_id
                WHERE m.truck_id = ?
                ORDER BY m.maintenance_date DESC, m.mileage_at_service DESC, m.id DESC
                LIMIT 1
                """;
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, truckId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRecord(resultSet));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DataAccessException("No fue posible consultar el ultimo mantenimiento.", e);
        }
    }

    @Override
    public MaintenanceRecord create(MaintenanceRecord record) {
        String sql = """
                INSERT INTO maintenance_records(truck_id, maintenance_date, maintenance_type, mileage_at_service, observations)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, record.getTruckId());
            statement.setDate(2, Date.valueOf(record.getMaintenanceDate()));
            statement.setString(3, record.getMaintenanceType());
            statement.setInt(4, record.getMileageAtService());
            statement.setString(5, record.getObservations());
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    record.setId(keys.getInt(1));
                }
            }
            return record;
        } catch (SQLException e) {
            throw new DataAccessException("No fue posible crear el mantenimiento.", e);
        }
    }

    @Override
    public void update(MaintenanceRecord record) {
        String sql = """
                UPDATE maintenance_records
                SET truck_id = ?, maintenance_date = ?, maintenance_type = ?, mileage_at_service = ?, observations = ?
                WHERE id = ?
                """;
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, record.getTruckId());
            statement.setDate(2, Date.valueOf(record.getMaintenanceDate()));
            statement.setString(3, record.getMaintenanceType());
            statement.setInt(4, record.getMileageAtService());
            statement.setString(5, record.getObservations());
            statement.setInt(6, record.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("No fue posible actualizar el mantenimiento.", e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM maintenance_records WHERE id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("No fue posible eliminar el mantenimiento.", e);
        }
    }

    private MaintenanceRecord mapRecord(ResultSet resultSet) throws SQLException {
        return new MaintenanceRecord(
                resultSet.getInt("id"),
                resultSet.getInt("truck_id"),
                resultSet.getString("truck_name"),
                resultSet.getDate("maintenance_date").toLocalDate(),
                resultSet.getString("maintenance_type"),
                resultSet.getInt("mileage_at_service"),
                resultSet.getString("observations")
        );
    }
}
