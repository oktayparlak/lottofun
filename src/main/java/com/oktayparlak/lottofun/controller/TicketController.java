package com.oktayparlak.lottofun.controller;

import com.oktayparlak.lottofun.business.abstracts.TicketService;
import com.oktayparlak.lottofun.dto.request.TicketRequest;
import com.oktayparlak.lottofun.dto.response.TicketResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/")
    public ResponseEntity<TicketResponse> purchaseTicket(@AuthenticationPrincipal UserDetails userDetails, @Valid TicketRequest ticketRequest) {
        return ResponseEntity.ok(ticketService.purchaseTicket(userDetails.getUsername(), ticketRequest));
    }

    @GetMapping("/my-tickets")
    public ResponseEntity<List<TicketResponse>> getMyTickets(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ticketService.getUserTickets(userDetails.getUsername()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponse> getTicketById(@AuthenticationPrincipal UserDetails userDetails, Long id) {
        return ResponseEntity.ok(ticketService.getUsersTicketById(userDetails.getUsername(), id));
    }

}
