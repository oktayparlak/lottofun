package com.oktayparlak.lottofun.dataAccess;

import com.oktayparlak.lottofun.entities.Prize;
import com.oktayparlak.lottofun.entities.enums.PrizeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrizeRepository extends JpaRepository<Prize, Long> {

    Optional<Prize> findByMatchCount(Integer matchCount);

    Optional<Prize> findByPrizeType(PrizeType prizeType);

}
