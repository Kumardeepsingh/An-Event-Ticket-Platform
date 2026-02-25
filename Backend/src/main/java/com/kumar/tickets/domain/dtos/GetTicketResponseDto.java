package com.kumar.tickets.domain.dtos;

import com.kumar.tickets.domain.enities.TicketStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetTicketResponseDto {
    private UUID id;
    private TicketStatusEnum status;
    private double price;
    private String description;
    private String eventName;
    private String eventVenue;
    private LocalDateTime eventStart;
    private LocalDateTime eventEnd;
}
