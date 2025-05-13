package org.iit.sahan.db;

import org.iit.sahan.model.Reservation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ReservationDAO {

    public void insertReservation(Reservation r) throws SQLException {
        String sql = "INSERT INTO reservations (concert_id, seat_type, customer_name, after_party) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, r.concertId);
            ps.setString(2, r.seatType);
            ps.setString(3, r.customerName);
            ps.setBoolean(4, r.afterParty);
            ps.executeUpdate();
        }
    }

    public void insertBulk(List<Reservation> reservations) throws SQLException {
        for (Reservation r : reservations) {
            insertReservation(r);
        }
    }
}
