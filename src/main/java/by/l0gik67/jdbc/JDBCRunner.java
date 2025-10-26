package by.l0gik67.jdbc;

import by.l0gik67.jdbc.utils.ConnectionManager;
import by.l0gik67.jdbc.utils.PropertiesUtil;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JDBCRunner {
    public static void main(String[] args) throws SQLException {

        String sql = """
                select * from ticket;
                """;
        try (var connection = ConnectionManager.get();
        var statement = connection.createStatement()) {
            var result = statement.executeQuery(sql);
            while (result.next()) {
                System.out.println(result.getString("passenger_name"));
                System.out.println(result.getLong("flight_id"));
                System.out.println(result.getBigDecimal("cost"));
            }
        }
    }


    public static List<Long> getTicketsByFlightId(Long flightId) {
        String sqlRequest = """
                select id from ticket 
                where flight_id = ?;
                """;
        List<Long> result = new ArrayList<>();
        try (var connection = ConnectionManager.get();
        var statement = connection.prepareStatement(sqlRequest)) {
            statement.setLong(1, flightId);


            var resultSet = statement.executeQuery(sqlRequest);
            while (resultSet.next()){
                result.add(resultSet.getLong("id"));
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }


    public static List<Long> getFlightBetween(LocalDateTime start, LocalDateTime end) {
        List<Long> result = new ArrayList<>();

        String sqlRequest = """
                select * from flight 
                where departure_date between ? and ?;
                """;
        try (var connection = ConnectionManager.get();
        var statement = connection.prepareStatement(sqlRequest)) {
            statement.setTimestamp(1, Timestamp.valueOf(start));
            statement.setTimestamp(2, Timestamp.valueOf(end));
            var resultSet = statement.executeQuery(sqlRequest);
            while (resultSet.next()) {
                result.add(resultSet.getLong("id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
