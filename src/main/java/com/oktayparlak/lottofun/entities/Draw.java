package com.oktayparlak.lottofun.entities;

import com.oktayparlak.lottofun.entities.enums.DrawStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "draws")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Draw {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "draw_number", unique = true, nullable = false)
    private Long drawNumber;

    @Column(name = "draw_date", nullable = false)
    private LocalDateTime drawDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DrawStatus status;

    @Column(name = "winning_numbers")
    private String winningNumbers;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
