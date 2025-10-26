package by.l0gik67.jdbc.dto;

public record TicketFilter(
        String passengerName,
        String seatNumber,
        int limit,
        int offset) {

}
