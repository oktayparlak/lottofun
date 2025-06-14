package com.oktayparlak.lottofun.dataAccess;

import com.oktayparlak.lottofun.entities.Draw;
import com.oktayparlak.lottofun.entities.Ticket;
import com.oktayparlak.lottofun.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {


    List<Ticket> findByDraw(Draw draw);

    @Query("SELECT t FROM Ticket t JOIN FETCH t.draw WHERE t.user.id = :userId")
    List<Ticket> findByUser_Id(Long userId);

    @Query("SELECT t FROM Ticket t JOIN FETCH t.draw WHERE t.id = :ticketId AND t.user.id = :userId")
    Optional<Ticket> findByIdAndUser_Id(Long ticketId, Long userId);

    Optional<Ticket> findByTicketNumber(String ticketNumber);

    List<Ticket> findByUserAndDraw(User user, Draw draw);

}
