package com.kumar.tickets.domain;

import com.kumar.tickets.domain.enities.EventStatusEnum;
import com.kumar.tickets.domain.enities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateEventRequest {

    private String name;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    private String venue;

    private LocalDateTime salesStart;

    private LocalDateTime saleEnd;

    private EventStatusEnum status;

    private List<CreateTicketTypeRequest> ticketTypes = new ArrayList<>();

}
