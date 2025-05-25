package com.oktayparlak.lottofun.core;

import com.oktayparlak.lottofun.dataAccess.DrawRepository;
import com.oktayparlak.lottofun.dataAccess.PrizeRepository;
import com.oktayparlak.lottofun.entities.Draw;
import com.oktayparlak.lottofun.entities.Prize;
import com.oktayparlak.lottofun.entities.enums.DrawStatus;
import com.oktayparlak.lottofun.entities.enums.PrizeType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Configuration
public class InitData {

    @Bean
    public CommandLineRunner initPrizes(PrizeRepository prizeRepository, DrawRepository drawRepository) {
        return args -> {
            if (prizeRepository.count() == 0) {
                prizeRepository.save(Prize.builder()
                        .matchCount(5)
                        .prizeType(PrizeType.JACKPOT)
                        .amount(new BigDecimal("10000.00"))
                        .build());

                prizeRepository.save(Prize.builder()
                        .matchCount(4)
                        .prizeType(PrizeType.HIGH)
                        .amount(new BigDecimal("1000.00"))
                        .build());

                prizeRepository.save(Prize.builder()
                        .matchCount(3)
                        .prizeType(PrizeType.MEDIUM)
                        .amount(new BigDecimal("100.00"))
                        .build());

                prizeRepository.save(Prize.builder()
                        .matchCount(2)
                        .prizeType(PrizeType.LOW)
                        .amount(new BigDecimal("10.00"))
                        .build());

                prizeRepository.save(Prize.builder()
                        .matchCount(1)
                        .prizeType(PrizeType.NO_PRIZE)
                        .amount(new BigDecimal("0.00"))
                        .build());

                prizeRepository.save(Prize.builder()
                        .matchCount(0)
                        .prizeType(PrizeType.NO_PRIZE)
                        .amount(new BigDecimal("0.00"))
                        .build());
            }
            if (drawRepository.count() == 0) {
                drawRepository.save(Draw.builder()
                        .drawNumber(3432423L)
                        .drawDate(LocalDateTime.now())
                        .status(DrawStatus.DRAW_OPEN)
                        .build());
            }
        };
    }

}
