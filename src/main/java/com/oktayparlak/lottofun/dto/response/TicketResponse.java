package com.oktayparlak.lottofun.dto.response;

import com.oktayparlak.lottofun.entities.enums.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponse {
    private Long id;
    private String ticketNumber;
    private Long drawId;
    private List<Integer> selectedNumbers;
    private Integer matchCount;
    private BigDecimal prizeAmount;
    private TicketStatus status;
    private LocalDateTime purchaseTimestamp;
    private List<Integer> winningNumbers;
}
