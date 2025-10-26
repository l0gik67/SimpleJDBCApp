package by.l0gik67.jdbc.entity;

import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Ticket {
    private Long id;
    private int passportNumber;
    private String passengerName;
    private Long flightId;
    private String seatNumber;
    private BigDecimal cost;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return Objects.equals(id, ticket.id) && Objects.equals(passportNumber, ticket.passportNumber) && Objects.equals(passengerName, ticket.passengerName) && Objects.equals(flightId, ticket.flightId) && Objects.equals(seatNumber, ticket.seatNumber) && Objects.equals(cost, ticket.cost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, passportNumber, passengerName, flightId, seatNumber, cost);
    }
}
