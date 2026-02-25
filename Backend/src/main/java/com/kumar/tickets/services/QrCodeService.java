package com.kumar.tickets.services;

import com.kumar.tickets.domain.enities.QrCode;
import com.kumar.tickets.domain.enities.Ticket;

import java.util.UUID;

public interface QrCodeService {
    QrCode generateQrCode(Ticket ticket);
    byte[] getQrCodeImageForUserAndTicket(UUID userId, UUID ticketId);
}
