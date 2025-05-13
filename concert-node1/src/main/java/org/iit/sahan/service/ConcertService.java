package org.iit.sahan.service;

import org.iit.sahan.db.ConcertDAO;
import org.iit.sahan.model.Concert;

import java.sql.SQLException;
import java.util.List;

public class ConcertService {

    private final ConcertDAO concertDAO = new ConcertDAO();

    public void addConcert(Concert c) throws SQLException {
        concertDAO.insertConcert(c);
    }

    public List<Concert> getAllConcerts() throws SQLException {
        return concertDAO.getAllConcerts();
    }
}
