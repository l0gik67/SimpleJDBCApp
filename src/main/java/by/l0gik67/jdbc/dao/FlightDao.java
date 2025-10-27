package by.l0gik67.jdbc.dao;

import by.l0gik67.jdbc.entity.Flight;
import by.l0gik67.jdbc.entity.FlightStatus;
import by.l0gik67.jdbc.utils.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FlightDao implements Dao<Long, Flight>{

    private static final FlightDao INSTANCE = new FlightDao();
    public static FlightDao getInstance() {
        return INSTANCE;
    }
    private FlightDao() {}

    private final static String UPDATE_SQL = """
                                             UPDATE flight 
                                             SET flight_number = ?,
                                                 departure_date = ?,
                                                 departure_airport_code = ?,
                                                 aircraft_id = ?,
                                                 status = ?
                                             """;
    private final static String FIND_ALL_SQL = """
                                               SELECT id,
                                                      flight_number,
                                                      departure_date,
                                                      departure_airport_code,
                                                      aircraft_id,
                                                      status
                                               FROM flight
                                               """;
    private final static String FIND_BY_ID_SQL = FIND_ALL_SQL +
                                                "WHERE id = ?";
    private final static String DELETE_SQL = """
                                             DELETE FROM flight
                                             WHERE id = ?
                                             """;
    private final static String SAVE_SQL = """
                                           INSERT INTO flight (flight_number,
                                                               departure_date,
                                                               departure_airport_code,
                                                               aircraft_id,
                                                               status)
                                           VALUES (?,?,?,?,?)
                                           """;

    @Override
    public boolean update(Flight flight) {
        try (var connection = ConnectionManager.get();
        var statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setInt(1, flight.getFlightNumber());
            statement.setTimestamp(2, Timestamp.valueOf(flight.getDepartureDate()));
            statement.setInt(3, flight.getDepartureAirportCode());
            statement.setInt(4, flight.getAircraftId());
            statement.setString(5, flight.getStatus().name());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Flight> findAll() {
        try (var connection = ConnectionManager.get();
        var statement = connection.prepareStatement(FIND_ALL_SQL)) {
            var result = statement.executeQuery();
            List<Flight> flights = new ArrayList<>();
            while (result.next()) {
                var flight = buildFlight(result);
                flights.add(flight);
            }
            return flights;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Flight> findById(Long id) {
        try (var connection = ConnectionManager.get();
        var statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            statement.setLong(1, id);

            var result = statement.executeQuery();
            Flight flight = null;
            if (result.next()) {
                flight = buildFlight(result);
            }
            return Optional.of(flight);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(Long id) {
        try (var connection = ConnectionManager.get();
        var statement = connection.prepareStatement(DELETE_SQL)) {
            statement.setLong(1, id);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Flight save(Flight flight) {
        try (var connection = ConnectionManager.get();
        var statement = connection.prepareStatement(SAVE_SQL)) {
            statement.setInt(1, flight.getFlightNumber());
            statement.setTimestamp(2, Timestamp.valueOf(flight.getDepartureDate()));
            statement.setInt(3, flight.getDepartureAirportCode());
            statement.setInt(4, flight.getAircraftId());
            statement.setString(5, flight.getStatus().name());

            statement.execute();
            var keys = statement.getGeneratedKeys();
            if (keys.next()) {
                flight.setId(keys.getLong(1));
            }
            return flight;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static Flight buildFlight(ResultSet result) throws SQLException {
        var flight = Flight.builder()
                .id(result.getLong("id"))
                .flightNumber(result.getInt("flight_number"))
                .departureDate(result.getTimestamp("departure_date").toLocalDateTime())
                .departureAirportCode(result.getInt("departure_airport_code"))
                .status(FlightStatus.valueOf(result.getString("status")))
                .build();
        return flight;
    }

}
