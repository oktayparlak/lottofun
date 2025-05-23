package com.oktayparlak.lottofun.business.concretes;

import com.oktayparlak.lottofun.business.abstracts.ResultService;
import com.oktayparlak.lottofun.business.abstracts.TicketService;
import com.oktayparlak.lottofun.dataAccess.PrizeRepository;
import com.oktayparlak.lottofun.dataAccess.TicketRepository;
import com.oktayparlak.lottofun.dataAccess.UserRepository;
import com.oktayparlak.lottofun.dto.converter.TicketMapper;
import com.oktayparlak.lottofun.entities.Draw;
import com.oktayparlak.lottofun.entities.Prize;
import com.oktayparlak.lottofun.entities.Ticket;
import com.oktayparlak.lottofun.entities.User;
import com.oktayparlak.lottofun.entities.enums.PrizeType;
import com.oktayparlak.lottofun.entities.enums.TicketStatus;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ResultServiceImpl implements ResultService {

    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;
    private final UserRepository userRepository;
    private final PrizeRepository prizeRepository;

    @Autowired
    public ResultServiceImpl(
            TicketRepository ticketRepository,
            TicketMapper ticketMapper,
            UserRepository userRepository,
            PrizeRepository prizeRepository) {
        this.ticketRepository = ticketRepository;
        this.ticketMapper = ticketMapper;
        this.userRepository = userRepository;
        this.prizeRepository = prizeRepository;
    }

    @Transactional
    @Override
    public void calculateResults(Draw draw, List<Ticket> tickets) {
        if(draw.getWinningNumbers() == null) {
            throw new IllegalArgumentException("Draw winning numbers is null");
        }

        Set<Integer> winningNumbersSet = Arrays.stream(draw.getWinningNumbers().split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toSet());

        for (Ticket ticket : tickets) {

            Set<Integer> selectedNumbersSet = Arrays.stream(ticket.getSelectedNumbers().split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toSet());

            // Calculate the number of matches
            Set<Integer> matchingNumbers = selectedNumbersSet.stream()
                    .filter(winningNumbersSet::contains)
                    .collect(Collectors.toSet());

            int matchCount = matchingNumbers.size();
            ticket.setMatchCount(matchCount);

            // Determine the prize type based on the match count
            PrizeType prizeType = determinePrizeType(matchCount);
            Prize prize = prizeRepository.findByPrizeType(prizeType)
                    .orElseThrow(() -> new RuntimeException("Prize not found for type: " + prizeType));

            ticket.setPrizeAmount(prize.getAmount());
            ticket.setStatus(matchCount >=2 ? TicketStatus.WON : TicketStatus.NOT_WON);

            ticketRepository.save(ticket);

        }

    }

    @Override
    public void processPayments(List<Ticket> tickets) {
        for (Ticket ticket : tickets) {
            if (ticket.getStatus() == TicketStatus.WON && ticket.getPrizeAmount() != null) {
                User user = ticket.getUser();
                user.setBalance(user.getBalance().add(ticket.getPrizeAmount()));
                userRepository.save(user);
            }
        }
    }

    public PrizeType determinePrizeType(int matchCount) {
        switch (matchCount) {
            case 5:
                return PrizeType.JACKPOT;
            case 4:
                return PrizeType.HIGH;
            case 3:
                return PrizeType.MEDIUM;
            case 2:
                return PrizeType.LOW;
            default:
                return PrizeType.NO_PRIZE;
        }
    }
}
