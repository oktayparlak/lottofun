package com.oktayparlak.lottofun.business.abstracts;

import com.oktayparlak.lottofun.dto.response.DrawResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DrawService {

    DrawResponse getActiveDraw();

    DrawResponse getDrawById(Long id);

    Page<DrawResponse> getDrawHistory(Pageable pageable);

}
