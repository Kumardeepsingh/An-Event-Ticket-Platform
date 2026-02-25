package com.kumar.tickets.services;

import com.kumar.tickets.domain.enities.TicketValidation;

import java.util.UUID;

public interface TicketValidationService {

    TicketValidation validateTicketByQrCode(UUID qrCodeId);
    TicketValidation validateTicketManually(UUID TicketId);
}
