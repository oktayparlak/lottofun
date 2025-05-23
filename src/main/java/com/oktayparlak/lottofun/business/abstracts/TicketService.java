package com.oktayparlak.lottofun.business.abstracts;

import com.oktayparlak.lottofun.dto.request.TicketRequest;
import com.oktayparlak.lottofun.dto.response.TicketResponse;

import java.util.List;

public interface TicketService {

    TicketResponse purchaseTicket(String email, TicketRequest ticketRequest);

    List<TicketResponse> getUserTickets(String email);

    TicketResponse getUsersTicketById(String email, Long ticketId);

}
