package by.l0gik67.jdbc.service;

import by.l0gik67.jdbc.dao.TicketDao;
import by.l0gik67.jdbc.dto.TicketDto;

import java.util.List;
import java.util.stream.Collectors;

public class TicketService {

    private TicketService() {}
    private static final TicketService INSTANCE = new TicketService();
    private static final TicketDao ticketDao = TicketDao.getInstance();

    public static TicketService getInstance() {
        return INSTANCE;
    }

    public List<TicketDto> findAllByFlightId(Long flightId) {
        return ticketDao.findAllbyFlightId(flightId).stream()
                .map(ticket -> new TicketDto(ticket.getId(), ticket.getFlight().getId(), ticket.getSeatNumber()))
                .collect(Collectors.toList());
    }
}
