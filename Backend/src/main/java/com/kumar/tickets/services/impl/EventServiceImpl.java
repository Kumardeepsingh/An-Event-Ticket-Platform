package com.kumar.tickets.services.impl;

import com.kumar.tickets.domain.CreateEventRequest;
import com.kumar.tickets.domain.enities.Event;
import com.kumar.tickets.domain.enities.TicketType;
import com.kumar.tickets.domain.enities.User;
import com.kumar.tickets.exceptions.UserNotFoundException;
import com.kumar.tickets.repositories.EventRepository;
import com.kumar.tickets.repositories.UserRepository;
import com.kumar.tickets.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public Event createEvent(UUID organizerId, CreateEventRequest event) {
        User organizer = userRepository.findById(organizerId)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("User with ID %s not found", organizerId))
                );

        List<TicketType> ticketTypesToCreate = event.getTicketTypes().stream().map(
                ticketType -> {
                    TicketType ticketTypeToCreate = new TicketType();
                    ticketTypeToCreate.setName(ticketType.getName());
                    ticketTypeToCreate.setPrice(ticketType.getPrice());
                    ticketTypeToCreate.setDescription(ticketType.getDescription());
                    ticketTypeToCreate.setTotalAvailable(ticketType.getTotalAvailable());
                    return ticketTypeToCreate;
                }
        ).toList();

        Event eventToCreate = new Event();
        eventToCreate.setName(event.getName());
        eventToCreate.setStartAt(event.getStartAt());
        eventToCreate.setEndAt(event.getEndAt());
        eventToCreate.setVenue(event.getVenue());
        eventToCreate.setSalesStart(event.getSalesStart());
        eventToCreate.setSaleEnd(event.getSaleEnd());
        eventToCreate.setStatus(event.getStatus());
        eventToCreate.setOrganizer(organizer);
        eventToCreate.setTicketTypes(ticketTypesToCreate);

        return eventRepository.save(eventToCreate);
    }
}
