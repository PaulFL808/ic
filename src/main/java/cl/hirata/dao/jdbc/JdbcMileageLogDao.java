package cl.hirata.dao.jdbc;

import cl.hirata.dao.MileageLogDao;
import cl.hirata.model.MileageLog;
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

public class JdbcMileageLogDao implements MileageLogDao {

    private final DatabaseConnection databaseConnection;

    public JdbcMileageLogDao(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @Override
    public MileageLog create(MileageLog mileageLog) {
        String sql = """
                INSERT INTO mileage_logs(truck_id, trip_date, kilometers_travelled, total_mileage_after_trip, notes)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, mileageLog.getTruckId());
            statement.setDate(2, Date.valueOf(mileageLog.getTripDate()));
            statement.setInt(3, mileageLog.getKilometersTravelled());
            statement.setInt(4, mileageLog.getTotalMileageAfterTrip());
            statement.setString(5, mileageLog.getNotes());
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    mileageLog.setId(keys.getInt(1));
                }
            }
            return mileageLog;
        } catch (SQLException e) {
            throw new DataAccessException("No fue posible registrar el kilometraje.", e);
        }
    }

    @Override
    public List<MileageLog> findByTruckId(int truckId) {
        String sql = """
                SELECT id, truck_id, trip_date, kilometers_travelled, total_mileage_after_trip, notes
                FROM mileage_logs
                WHERE truck_id = ?
                ORDER BY trip_date DESC, id DESC
                """;
        List<MileageLog> logs = new ArrayList<>();
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, truckId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    logs.add(new MileageLog(
                            resultSet.getInt("id"),
                            resultSet.getInt("truck_id"),
                            resultSet.getDate("trip_date").toLocalDate(),
                            resultSet.getInt("kilometers_travelled"),
                            resultSet.getInt("total_mileage_after_trip"),
                            resultSet.getString("notes")
                    ));
                }
            }
            return logs;
        } catch (SQLException e) {
            throw new DataAccessException("No fue posible listar los kilometrajes.", e);
        }
    }
}
