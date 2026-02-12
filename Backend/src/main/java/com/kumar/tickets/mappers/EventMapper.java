package com.kumar.tickets.mappers;

import com.kumar.tickets.domain.CreateEventRequest;
import com.kumar.tickets.domain.CreateTicketTypeRequest;
import com.kumar.tickets.domain.dtos.*;
import com.kumar.tickets.domain.enities.Event;
import com.kumar.tickets.domain.enities.TicketType;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {
    CreateTicketTypeRequest fromDto(CreateTicketTypeRequestDto dto);

    CreateEventRequest fromDto(CreateEventRequestDto dto);

    CreateEventResponseDto toDto(Event event);

    ListEventTicketTypeResponseDto toDto(TicketType ticketType);

    ListEventResponseDto toListEventResponseDto(Event event);
}
