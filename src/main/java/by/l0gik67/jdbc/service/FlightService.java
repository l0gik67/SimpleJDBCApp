package by.l0gik67.jdbc.service;

import by.l0gik67.jdbc.dao.FlightDao;
import by.l0gik67.jdbc.dto.FlightDto;
import by.l0gik67.jdbc.entity.Flight;

import java.util.List;
import java.util.stream.Collectors;

public class FlightService {

    private static final FlightService INSTANCE = new FlightService();
    private static final FlightDao flightDao = FlightDao.getInstance();

    private FlightService() {}

    public List<FlightDto> findAll()  {
        List<Flight> flights = flightDao.findAll();

        return flightDao.findAll().stream()
                .map(flight -> new FlightDto(flight.getId(), "%s - %s - %s".formatted(
                                                            flight.getDepartureAirportCode(),
                                                            flight.getStatus(),
                                                            flight.getDepartureDate())))
                .collect(Collectors.toList());
    }


    public static FlightService getInstance() {
        return INSTANCE;
    }
}
