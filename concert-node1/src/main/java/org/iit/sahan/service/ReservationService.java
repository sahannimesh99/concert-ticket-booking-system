package org.iit.sahan.service;

import org.iit.sahan.db.ReservationDAO;
import org.iit.sahan.model.Reservation;

import java.sql.SQLException;
import java.util.List;

public class ReservationService {

    private final ReservationDAO reservationDAO = new ReservationDAO();

    public void addReservation(Reservation r) throws SQLException {
        reservationDAO.insertReservation(r);
    }

    public void addBulkReservations(List<Reservation> reservations) throws SQLException {
        reservationDAO.insertBulk(reservations);
    }
}
