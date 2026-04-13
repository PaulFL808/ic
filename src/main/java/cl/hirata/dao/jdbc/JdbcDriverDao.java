package cl.hirata.dao.jdbc;

import cl.hirata.dao.DriverDao;
import cl.hirata.model.Driver;
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

public class JdbcDriverDao implements DriverDao {

    private final DatabaseConnection databaseConnection;

    public JdbcDriverDao(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @Override
    public List<Driver> findAll() {
        String sql = "SELECT id, name, license_number, phone FROM drivers ORDER BY id";
        List<Driver> drivers = new ArrayList<>();

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                drivers.add(mapDriver(resultSet));
            }
            return drivers;
        } catch (SQLException e) {
            throw new DataAccessException("No fue posible listar los conductores.", e);
        }
    }

    @Override
    public Optional<Driver> findById(int id) {
        String sql = "SELECT id, name, license_number, phone FROM drivers WHERE id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapDriver(resultSet));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DataAccessException("No fue posible buscar el conductor.", e);
        }
    }

    @Override
    public Driver create(Driver driver) {
        String sql = "INSERT INTO drivers(name, license_number, phone) VALUES (?, ?, ?)";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, driver.getName());
            statement.setString(2, driver.getLicenseNumber());
            statement.setString(3, driver.getPhone());
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    driver.setId(keys.getInt(1));
                }
            }
            return driver;
        } catch (SQLException e) {
            throw new DataAccessException("No fue posible crear el conductor.", e);
        }
    }

    @Override
    public void update(Driver driver) {
        String sql = "UPDATE drivers SET name = ?, license_number = ?, phone = ? WHERE id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, driver.getName());
            statement.setString(2, driver.getLicenseNumber());
            statement.setString(3, driver.getPhone());
            statement.setInt(4, driver.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("No fue posible actualizar el conductor.", e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM drivers WHERE id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("No fue posible eliminar el conductor.", e);
        }
    }

    private Driver mapDriver(ResultSet resultSet) throws SQLException {
        return new Driver(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("license_number"),
                resultSet.getString("phone")
        );
    }
}
