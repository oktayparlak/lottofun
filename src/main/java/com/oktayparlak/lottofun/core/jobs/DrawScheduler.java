package com.oktayparlak.lottofun.core.jobs;

import com.oktayparlak.lottofun.business.abstracts.ResultService;
import com.oktayparlak.lottofun.dataAccess.DrawRepository;
import com.oktayparlak.lottofun.dataAccess.TicketRepository;
import com.oktayparlak.lottofun.entities.Draw;
import com.oktayparlak.lottofun.entities.Ticket;
import com.oktayparlak.lottofun.entities.enums.DrawStatus;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

@Component
public class DrawScheduler {

    private final DrawRepository drawRepository;
    private final TicketRepository ticketRepository;
    private final ResultService resultService;

    @Autowired
    public DrawScheduler(DrawRepository drawRepository, TicketRepository ticketRepository, ResultService resultService) {
        this.drawRepository = drawRepository;
        this.ticketRepository = ticketRepository;
        this.resultService = resultService;
    }

    public void createNewDraw() {
        System.out.println("Creating a new draw..." + "${draw.cron.expression}");
        // Find the last draw number
        Long lastDrawNumber = drawRepository.findAll().stream()
                .map(Draw::getDrawNumber)
                .max(Long::compareTo)
                .orElse(0L);

        Draw newDraw = Draw.builder()
                .drawNumber(lastDrawNumber + 1)
                .drawDate(LocalDateTime.now().plusDays(7)) // Set the draw date to 7 days from now
                .status(DrawStatus.DRAW_OPEN)
                .build();

        Draw savedDraw = drawRepository.save(newDraw);
        System.out.println("New draw created with ID: " + savedDraw.getId());
    }

    @Scheduled(cron = "${draw.cron.expression}")
    @Transactional
    public void executeDraw() {

        try {
            Draw openedDraw = drawRepository.findFirstByStatusOrderByDrawDateAsc(DrawStatus.DRAW_OPEN)
                    .orElse(null);

            if (Objects.nonNull(openedDraw)) {
                // Choose winning numbers
                List<Integer> winningNumbers = generateWinningNumbers();
                openedDraw.setWinningNumbers(convertNumbersToString(winningNumbers));
                openedDraw.setStatus(DrawStatus.DRAW_EXTRACTED);
                drawRepository.save(openedDraw);

                //check the tickets and list the winners
                List<Ticket> tickets = ticketRepository.findByDraw(openedDraw);
                resultService.calculateResults(openedDraw, tickets);

                // change the status of the draw
                openedDraw.setStatus(DrawStatus.PAYMENTS_PROCESSING);
                drawRepository.save(openedDraw);

                // process payments
                resultService.processPayments(tickets);

                // change the status of the draw
                openedDraw.setStatus(DrawStatus.DRAW_CLOSED);
                drawRepository.save(openedDraw);

                // Notify winners if we want to implement notification system

                // create  new draw
                createNewDraw();
            }


        } catch (Exception e) {
            System.out.println("Error occurred during draw execution: " + e.getMessage());
        }

    }

    private List<Integer> generateWinningNumbers() {
        List<Integer> numbers = new ArrayList<>();
        Random random = new Random();

        while (numbers.size() < 5) {
            int number = random.nextInt(49) + 1;
            if (!numbers.contains(number)) {
                numbers.add(number);
            }
        }

        List<Integer> demoNumbers = new ArrayList<>();
        demoNumbers.add(1);
        demoNumbers.add(2);
        demoNumbers.add(3);
        demoNumbers.add(4);
        demoNumbers.add(5);
        return demoNumbers; // For testing purposes, we return demo numbers

    }

    private String convertNumbersToString(List<Integer> numbers) {
        return numbers.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }


}
