package com.oktayparlak.lottofun.dataAccess;

import com.oktayparlak.lottofun.entities.Draw;
import com.oktayparlak.lottofun.entities.enums.DrawStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DrawRepository extends JpaRepository<Draw, Long> {

    Optional<Draw> findByStatus(DrawStatus status);

    Optional<Draw> findFirstByStatusOrderByDrawDateAsc(DrawStatus status);

    Page<Draw> findAllByOrderByDrawDateDesc(Pageable pageable);

    Optional<Draw> findByDrawNumber(Long drawNumber);

}
