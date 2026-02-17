package com.kumar.tickets.services;

import com.kumar.tickets.domain.enities.Ticket;

import java.util.UUID;

public interface TicketTypeService {
    Ticket purchaseTicket(UUID userId, UUID ticketTypeId);
}
