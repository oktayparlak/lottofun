package com.oktayparlak.lottofun.controller;

import com.oktayparlak.lottofun.business.abstracts.TicketService;
import com.oktayparlak.lottofun.dto.response.TicketResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/results")
public class ResultController {

    private final TicketService ticketService;

    @Autowired
    public ResultController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/tickets/{id}")
    public ResponseEntity<TicketResponse> getTicketResult(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getUsersTicketById(userDetails.getUsername(), id));
    }

}
