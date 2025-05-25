package com.oktayparlak.lottofun.business.abstracts;

import com.oktayparlak.lottofun.entities.Draw;
import com.oktayparlak.lottofun.entities.Ticket;

import java.util.List;

public interface ResultService {

    void calculateResults(Draw draw, List<Ticket> tickets);

    void processPayments(List<Ticket> tickets);

}
