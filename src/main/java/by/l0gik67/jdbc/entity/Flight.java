package by.l0gik67.jdbc.entity;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@ToString
@Builder
@AllArgsConstructor
public class Flight {
    private Long id;
    private int flightNumber;
    private LocalDateTime departureDate;
    private int departureAirportCode;
    private int aircraftId;
    private FlightStatus status;
}
