package com.oktayparlak.lottofun.business.concretes;

import com.oktayparlak.lottofun.business.abstracts.TicketService;
import com.oktayparlak.lottofun.dataAccess.DrawRepository;
import com.oktayparlak.lottofun.dataAccess.TicketRepository;
import com.oktayparlak.lottofun.dataAccess.UserRepository;
import com.oktayparlak.lottofun.dto.converter.TicketMapper;
import com.oktayparlak.lottofun.dto.request.TicketRequest;
import com.oktayparlak.lottofun.dto.response.TicketResponse;
import com.oktayparlak.lottofun.entities.Draw;
import com.oktayparlak.lottofun.entities.Ticket;
import com.oktayparlak.lottofun.entities.User;
import com.oktayparlak.lottofun.entities.enums.DrawStatus;
import com.oktayparlak.lottofun.entities.enums.TicketStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;
    private final UserRepository userRepository;
    private final DrawRepository drawRepository;

    private static final BigDecimal TICKET_PRICE = new BigDecimal("25.00");

    @Autowired
    public TicketServiceImpl(
            TicketRepository ticketRepository,
            TicketMapper ticketMapper,
            UserRepository userRepository,
            DrawRepository drawRepository
    ) {
        this.ticketRepository = ticketRepository;
        this.ticketMapper = ticketMapper;
        this.userRepository = userRepository;
        this.drawRepository = drawRepository;
    }

    @Override
    public TicketResponse purchaseTicket(String email, TicketRequest ticketRequest) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Draw activeDraw = drawRepository.findByStatus(DrawStatus.DRAW_OPEN)
                .orElseThrow(() -> new RuntimeException("No active draw found"));

        // validate selected numbers
        validateSelectedNumbers(ticketRequest.getSelectedNumbers());

        // check user's balance
        if(user.getBalance().compareTo(TICKET_PRICE) < 0) {
            throw new RuntimeException("You have not enough money");
        }

        // create ticket
        Ticket ticket = Ticket.builder()
                .ticketNumber(generateTicketNumber())
                .user(user)
                .draw(activeDraw)
                .selectedNumbers(convertNumbersToString(ticketRequest.getSelectedNumbers()))
                .status(TicketStatus.WAITING_FOR_DRAW)
                .purchaseTimestamp(LocalDateTime.now())
                .build();

        // set user's balance
        user.setBalance(user.getBalance().subtract(TICKET_PRICE));
        userRepository.save(user);

        // save ticket
        Ticket savedTicket = ticketRepository.save(ticket);
        return ticketMapper.toResponse(savedTicket);
    }

    @Override
    public List<TicketResponse> getUserTickets(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Ticket> tickets = ticketRepository.findByUser_Id(user.getId());
        return ticketMapper.toResponseList(tickets);
    }

    @Override
    public TicketResponse getUsersTicketById(String email, Long ticketId) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Ticket ticket = ticketRepository.findByIdAndUser_Id(ticketId, user.getId())
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        return ticketMapper.toResponse(ticket);
    }

    private void validateSelectedNumbers(List<Integer> selectedNumbers) {
        // Are selected numbers' count 5?
        if (selectedNumbers.size() != 5) {
            throw new RuntimeException("Please select 5 numbers");
        }

        // Are selected numbers between 1 and 49?
        for (Integer number : selectedNumbers) {
            if (number < 1 || number > 49) {
                throw new RuntimeException("The numbers must be between 1 and 49");
            }
        }

        // Are selected numbers unique?
        Set<Integer> uniqueNumbers = new HashSet<>(selectedNumbers);
        if (uniqueNumbers.size() != 5) {
            throw new RuntimeException("Selected numbers must be unique");
        }
    }

    private String generateTicketNumber() {
        String createdNumber = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Ticket existingTicket = ticketRepository.findByTicketNumber(createdNumber)
                .orElse(null);
        while (existingTicket != null) {
            createdNumber = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            existingTicket = ticketRepository.findByTicketNumber(createdNumber)
                    .orElse(null);
        }
        return createdNumber;
    }

    private String convertNumbersToString(List<Integer> numbers) {
        return numbers.stream()
                .sorted()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

}
