package com.oktayparlak.lottofun.dto.converter;

import com.oktayparlak.lottofun.dto.response.DrawResponse;
import com.oktayparlak.lottofun.entities.Draw;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface DrawMapper {

    @Mapping(source = "winningNumbers", target = "winningNumbers", qualifiedByName = "stringToIntegerList")
    DrawResponse toResponse(Draw draw);
    List<DrawResponse> toResponseList(List<Draw> drawList);

    @Named("stringToIntegerList")
    default List<Integer> stringToIntegerList(String numbers) {
        if (numbers == null || numbers.isEmpty()) return new ArrayList<>();
        return Arrays.stream(numbers.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

}
