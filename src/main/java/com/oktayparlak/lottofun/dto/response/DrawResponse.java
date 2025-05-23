package com.oktayparlak.lottofun.dto.response;

import com.oktayparlak.lottofun.entities.enums.DrawStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DrawResponse {
    private Long id;
    private Long drawNumber;
    private LocalDateTime drawDate;
    private DrawStatus status;
    private List<Integer> winningNumbers;
    private LocalDateTime createdAt;
}
