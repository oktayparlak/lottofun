package com.oktayparlak.lottofun.business.concretes;

import com.oktayparlak.lottofun.business.abstracts.DrawService;
import com.oktayparlak.lottofun.dataAccess.DrawRepository;
import com.oktayparlak.lottofun.dataAccess.TicketRepository;
import com.oktayparlak.lottofun.dto.converter.DrawMapper;
import com.oktayparlak.lottofun.dto.response.DrawResponse;
import com.oktayparlak.lottofun.entities.Draw;
import com.oktayparlak.lottofun.entities.Ticket;
import com.oktayparlak.lottofun.entities.enums.DrawStatus;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class DrawServiceImpl implements DrawService {

    private final DrawRepository drawRepository;
    private final DrawMapper drawMapper;
    private final TicketRepository ticketRepository;
    private final ResultServiceImpl resultService;

    @Autowired
    public DrawServiceImpl(DrawRepository drawRepository, DrawMapper drawMapper, TicketRepository ticketRepository,
                           ResultServiceImpl resultService) {
        this.drawRepository = drawRepository;
        this.drawMapper = drawMapper;
        this.ticketRepository = ticketRepository;
        this.resultService = resultService;
    }

    @Override
    public DrawResponse getActiveDraw() {
        Draw activeDraw = drawRepository.findByStatus(DrawStatus.DRAW_OPEN)
                .orElseThrow(() -> new RuntimeException("Active draw not found"));
        return drawMapper.toResponse(activeDraw);
    }

    @Override
    public DrawResponse getDrawById(Long id) {
        Draw draw = drawRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Draw not found"));
        return drawMapper.toResponse(draw);
    }

    @Override
    public List<DrawResponse> getDrawHistory(Pageable pageable) {
        List<Draw> drawList = drawRepository.findAll(pageable).getContent();
        return drawMapper.toResponseList(drawList);
    }

    public Draw createNewDraw() {
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

        return drawRepository.save(newDraw);
    }

    @Scheduled(cron = "${draw.cron.expression}")
    @Transactional
    @Override
    public void executeDraw() {

        Draw finishedDraw = drawRepository.findFirstByStatusOrderByDrawDateAsc(DrawStatus.DRAW_OPEN)
                .filter(draw -> draw.getDrawDate().isBefore(LocalDateTime.now()))
                .orElse(null);

        if (finishedDraw != null) {
            return; // There is no draw that closed to time
        }

        // Choose winning numbers
        List<Integer> winningNumbers = generateWinningNumbers();
        finishedDraw.setWinningNumbers(convertNumbersToString(winningNumbers));
        finishedDraw.setStatus(DrawStatus.DRAW_EXTRACTED);
        drawRepository.save(finishedDraw);

        //check the tickets and list the winners
        List<Ticket> tickets = ticketRepository.findByDraw(finishedDraw);
        resultService.calculateResults(finishedDraw, tickets);

        // change the status of the draw
        finishedDraw.setStatus(DrawStatus.PAYMENTS_PROCESSING);
        drawRepository.save(finishedDraw);

        // process payments
        resultService.processPayments(tickets);

        // change the status of the draw
        finishedDraw.setStatus(DrawStatus.DRAW_CLOSED);
        drawRepository.save(finishedDraw);

        // Notify winners

        // create  new draw
        createNewDraw();

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

        return numbers;
    }

    private String convertNumbersToString(List<Integer> numbers) {
        return numbers.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

}
