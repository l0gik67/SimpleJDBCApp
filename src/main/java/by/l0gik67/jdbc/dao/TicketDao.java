package by.l0gik67.jdbc.dao;


import by.l0gik67.jdbc.dto.TicketFilter;
import by.l0gik67.jdbc.entity.Flight;
import by.l0gik67.jdbc.entity.FlightStatus;
import by.l0gik67.jdbc.entity.Ticket;
import by.l0gik67.jdbc.exception.DaoException;
import by.l0gik67.jdbc.utils.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TicketDao implements Dao<Long, Ticket> {
    private final static TicketDao INSTANCE = new TicketDao();
    private final static String SAVE_SQL = """
                                            INSERT INTO ticket (passport_number, passenger_name, flight_id, seat_number, cost)
                                            VALUES (?, ?, ?, ?, ?)
                                            """;

    private final static String DELETE_SQL = """
                                              DELETE FROM ticket
                                              WHERE id = ?
                                              """;


    private final static String FIND_ALL_SQL = """
                                               SELECT ticket.id,
                                                      passport_number,
                                                      passenger_name,
                                                      flight_id,
                                                      seat_number,
                                                      cost,
                                                      flight.id,
                                                      flight_number,
                                                      departure_date,
                                                      departure_airport_code,
                                                      aircraft_id,
                                                      status
                                               FROM ticket JOIN flight ON ticket.flight_id = flight.id
                                               """;

    private final static String FIND_BY_ID_SQL = FIND_ALL_SQL + """
                                                 WHERE id = ?
                                                 """;

    private final static String UPDATE_SQL = """
                                              UPDATE ticket
                                              SET passport_number = ?,
                                                  passenger_name = ?,
                                                  flight_id = ?,
                                                  seat_number = ?,
                                                  cost = ?
                                              WHERE id = ?
                                              """;
    private static final String FIND_BY_FLIGHT_ID_SQL = FIND_ALL_SQL +
                                                        """
                                                        WHERE flight_id = ?
                                                        """;

    public Ticket save(Ticket ticket) {
        try (var connection = ConnectionManager.get();
        var statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);) {
            statement.setInt(1, ticket.getPassportNumber());
            statement.setString(2, ticket.getPassengerName());
            statement.setLong(3, ticket.getFlight().getId());
            statement.setString(4, ticket.getSeatNumber());
            statement.setBigDecimal(5, ticket.getCost());

            statement.executeUpdate();
            var keys = statement.getGeneratedKeys();
            if (keys.next()) {
                ticket.setId(keys.getLong("id"));
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return ticket;
    }

    public boolean delete(Long id) {
        try (var connection = ConnectionManager.get();
        var statement = connection.prepareStatement(DELETE_SQL)) {
            statement.setLong(1, id);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public List<Ticket> findAll(){
        try (var connection = ConnectionManager.get();
        var statement = connection.prepareStatement(FIND_ALL_SQL)) {
            var result = statement.executeQuery();

            List<Ticket> tickets = new ArrayList<>();
            while (result.next()) {
                var ticket = buildTicket(result);
                tickets.add(ticket);
            }
            return tickets;
        } catch (SQLException e){
                throw new DaoException(e);
        }
    }
    public List<Ticket> findAll(TicketFilter filter) {
        List<Object> parameters = new ArrayList<>();
        List<String> whereSql = new ArrayList<>();

        if(filter.passengerName() != null){
            parameters.add(filter.passengerName());
            whereSql.add("passenger_name = ?");
        }
        if (filter.seatNumber() != null){
            parameters.add("%" + filter.seatNumber() + "%");
            whereSql.add("seat_number like ?");
        }
        parameters.add(filter.limit());
        parameters.add(filter.offset());
        var where = whereSql.stream().collect(Collectors.joining(
                " AND ",
                parameters.size() > 2 ? " WHERE " : " ",
                " LIMIT ? OFFSET ?"
        ));
        String sql = FIND_ALL_SQL + where;
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(sql)) {

            for (int i = 0; i < parameters.size(); i++) {
                statement.setObject(i + 1, parameters.get(i));
            }
            var result = statement.executeQuery();

            List<Ticket> tickets = new ArrayList<>();
            while (result.next()) {
                var ticket = buildTicket(result);
                tickets.add(ticket);
            }
            return tickets;
        } catch (SQLException e){
            throw new DaoException(e);
        }
    }

    public List<Ticket> findAllbyFlightId(Long flightId) {
        try (var connection = ConnectionManager.get();
        var statement = connection.prepareStatement(FIND_BY_FLIGHT_ID_SQL) ) {
            statement.setLong(1, flightId);

            var result = statement.executeQuery();
            List<Ticket> tickets = new ArrayList<>();
            while (result.next()) {
                var ticket = buildTicket(result);
                tickets.add(ticket);
            }
            return tickets;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Ticket> findById(Long id){
        try(var connection = ConnectionManager.get();
            var statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            statement.setLong(1, id);

            var result = statement.executeQuery();
            Ticket ticket = null;
            if (result.next()) {
                ticket = buildTicket(result);
            }
            return Optional.ofNullable(ticket);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public boolean update(Ticket ticket) {
        try(var connection  =ConnectionManager.get();
        var statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setInt(1, ticket.getPassportNumber());
            statement.setString(2, ticket.getPassengerName());
            statement.setLong(3, ticket.getFlight().getId());
            statement.setString(4, ticket.getSeatNumber());
            statement.setBigDecimal(5, ticket.getCost());
            statement.setLong(6, ticket.getId());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private static Ticket buildTicket(ResultSet result) throws SQLException {
        var flight = Flight.builder()
                .id(result.getLong("flight_id"))
                .flightNumber(result.getInt("flight_number"))
                .departureDate(result.getTimestamp("departure_date").toLocalDateTime())
                .departureAirportCode(result.getInt("departure_airport_code"))
                .aircraftId(result.getInt("aircraft_id"))
                .status(FlightStatus.valueOf(result.getString("status").toUpperCase()))
                .build();

        var ticket = Ticket.builder()
                .id(result.getLong("id"))
                .passportNumber(result.getInt("passport_number"))
                .passengerName(result.getString("passenger_name"))
                .flight(flight)
                .seatNumber(result.getString("seat_number"))
                .cost(result.getBigDecimal("cost"))
                .build();

        return ticket;
    }


    private TicketDao() {
    }

    public static TicketDao getInstance() {
        return INSTANCE;
    }
}
