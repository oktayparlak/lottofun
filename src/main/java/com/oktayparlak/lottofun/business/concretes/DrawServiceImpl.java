package com.oktayparlak.lottofun.business.concretes;

import com.oktayparlak.lottofun.business.abstracts.DrawService;
import com.oktayparlak.lottofun.dataAccess.DrawRepository;
import com.oktayparlak.lottofun.dto.converter.DrawMapper;
import com.oktayparlak.lottofun.dto.response.DrawResponse;
import com.oktayparlak.lottofun.entities.Draw;
import com.oktayparlak.lottofun.entities.enums.DrawStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
public class DrawServiceImpl implements DrawService {

    private final DrawRepository drawRepository;
    private final DrawMapper drawMapper;

    @Autowired
    public DrawServiceImpl(DrawRepository drawRepository, DrawMapper drawMapper) {
        this.drawRepository = drawRepository;
        this.drawMapper = drawMapper;
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
    public Page<DrawResponse> getDrawHistory(Pageable pageable) {
        Page<Draw> drawList = drawRepository.findAll(pageable);
        List<DrawResponse> drawResponses = drawMapper.toResponseList(drawList.getContent());
        return new PageImpl<>(drawResponses, pageable, drawList.getTotalElements());
    }

}
