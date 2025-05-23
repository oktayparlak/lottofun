package com.oktayparlak.lottofun.dataAccess;

import com.oktayparlak.lottofun.entities.Draw;
import com.oktayparlak.lottofun.entities.Ticket;
import com.oktayparlak.lottofun.entities.User;
import com.oktayparlak.lottofun.entities.enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByUser(User user);

    List<Ticket> findByUserAndStatus(User user, TicketStatus status);

    List<Ticket> findByDraw(Draw draw);

    List<Ticket> findByUser_Id(Long userId);

    Optional<Ticket> findByIdAndUser_Id(Long ticketId, Long userId);

    Optional<Ticket> findByTicketNumber(String ticketNumber);

    List<Ticket> findByUserAndDraw(User user, Draw draw);

}
