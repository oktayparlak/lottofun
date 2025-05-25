package com.oktayparlak.lottofun.controller;

import com.oktayparlak.lottofun.business.abstracts.DrawService;
import com.oktayparlak.lottofun.dto.response.DrawResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/draws")
public class DrawController {

    private final DrawService drawService;

    @Autowired
    public DrawController(DrawService drawService) {
        this.drawService = drawService;
    }

    @GetMapping("/active")
    public ResponseEntity<DrawResponse> getActiveDraw() {
        return ResponseEntity.ok(drawService.getActiveDraw());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DrawResponse> getDrawById(@PathVariable Long id) {
        return ResponseEntity.ok(drawService.getDrawById(id));
    }

    @GetMapping("/history")
    public ResponseEntity<Page<DrawResponse>> getDrawHistory(Pageable pageable) {
        return ResponseEntity.ok(drawService.getDrawHistory(pageable));
    }

}
