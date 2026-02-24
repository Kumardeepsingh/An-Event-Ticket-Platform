package com.kumar.tickets.mappers;

import com.kumar.tickets.domain.dtos.ListTicketResponseDto;
import com.kumar.tickets.domain.dtos.ListTicketTicketTypeResponseDto;
import com.kumar.tickets.domain.enities.Ticket;
import com.kumar.tickets.domain.enities.TicketType;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TicketMapper {
    ListTicketTicketTypeResponseDto toListTicketTicketTypeResponseDto(TicketType ticketType);

    ListTicketResponseDto toListTicketResponseDto(Ticket ticket);

}
