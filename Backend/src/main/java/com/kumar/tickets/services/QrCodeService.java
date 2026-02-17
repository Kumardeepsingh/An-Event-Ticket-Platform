package com.kumar.tickets.services;

import com.kumar.tickets.domain.enities.QrCode;
import com.kumar.tickets.domain.enities.Ticket;

public interface QrCodeService {
    QrCode generateQrCode(Ticket ticket);
}
