package com.kumar.tickets.services;

import com.kumar.tickets.domain.CreateEventRequest;
import com.kumar.tickets.domain.enities.Event;

import java.util.List;
import java.util.UUID;

public interface EventService {
    Event createEvent(UUID organizerId, CreateEventRequest event);
}
