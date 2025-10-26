package by.l0gik67.jdbc.dao;


import by.l0gik67.jdbc.entity.Ticket;
import by.l0gik67.jdbc.exception.DaoException;
import by.l0gik67.jdbc.utils.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TicketDao {
    private final static TicketDao INSTANCE = new TicketDao();
    private final static String SAVE_SQL = """
                                            INSERT INTO ticket (passport_number, passenger_name, flight_id, seat_number, cost)
                                            VALUES (?, ?, ?, ?, ?);
                                            """;

    private final static String DELETE_SQL = """
                                              DELETE FROM ticket
                                              WHERE id = ?;  
                                              """;

    private final static String FIND_BY_ID_SQL = """
                                                 SELECT id, passport_number, passenger_name, flight_id, seat_number, cost 
                                                 FROM ticket
                                                 WHERE id = ?;
                                                 """;

    private final static String FIND_ALL_SQL = """
                                               SELECT id, passport_number, passenger_name, flight_id, seat_number, cost
                                               FROM ticket;
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

    public Ticket save(Ticket ticket) {
        try (var connection = ConnectionManager.get();
        var statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);) {
            statement.setInt(1, ticket.getPassportNumber());
            statement.setString(2, ticket.getPassengerName());
            statement.setLong(3, ticket.getFlightId());
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

    private static boolean update(Ticket ticket) {
        try(var connection  =ConnectionManager.get();
        var statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setInt(1, ticket.getPassportNumber());
            statement.setString(2, ticket.getPassengerName());
            statement.setLong(3, ticket.getFlightId());
            statement.setString(4, ticket.getSeatNumber());
            statement.setBigDecimal(5, ticket.getCost());
            statement.setLong(6, ticket.getId());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private static Ticket buildTicket(ResultSet result) throws SQLException {
        return Ticket.builder()
                .id(result.getLong("id"))
                .passportNumber(result.getInt("passport_number"))
                .passengerName(result.getString("passenger_name"))
                .flightId(result.getLong("flight_id"))
                .seatNumber(result.getString("seat_number"))
                .cost(result.getBigDecimal("cost"))
                .build();
    }


    private TicketDao() {
    }

    public static TicketDao getInstance() {
        return INSTANCE;
    }
}
