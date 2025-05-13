package org.iit.sahan.db;

import org.iit.sahan.model.Concert;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConcertDAO {

    public void insertConcert(Concert c) throws SQLException {
        String sql = "INSERT INTO concerts (title, date, vip_seats, regular_seats, after_party) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.title);
            ps.setString(2, c.date);
            ps.setInt(3, c.vipSeats);
            ps.setInt(4, c.regularSeats);
            ps.setInt(5, c.afterPartyTickets);
            ps.executeUpdate();
        }
    }

    public List<Concert> getAllConcerts() throws SQLException {
        List<Concert> list = new ArrayList<>();
        String sql = "SELECT * FROM concerts";
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Concert c = new Concert();
                c.id = rs.getInt("id");
                c.title = rs.getString("title");
                c.date = rs.getString("date");
                c.vipSeats = rs.getInt("vip_seats");
                c.regularSeats = rs.getInt("regular_seats");
                c.afterPartyTickets = rs.getInt("after_party");
                list.add(c);
            }
        }
        return list;
    }
}
