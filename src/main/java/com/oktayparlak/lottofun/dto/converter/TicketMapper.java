package com.oktayparlak.lottofun.dto.converter;

import com.oktayparlak.lottofun.dto.response.TicketResponse;
import com.oktayparlak.lottofun.entities.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface TicketMapper {

    @Mapping(source = "selectedNumbers", target = "selectedNumbers", qualifiedByName = "stringToIntegerList")
    TicketResponse toResponse(Ticket ticket);

    List<TicketResponse> toResponseList(List<Ticket> tickets);

    @Named("stringToIntegerList")
    default List<Integer> stringToIntegerList(String numbers) {
        if (numbers == null || numbers.isEmpty()) return new ArrayList<>();
        return Arrays.stream(numbers.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

}
