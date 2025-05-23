package com.oktayparlak.lottofun.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketRequest {
    @NotEmpty(message = "Selected numbers cannot be empty")
    @Size(min = 5, max = 5, message = "You must select exactly 5 numbers")
    private List<Integer> selectedNumbers;
}
