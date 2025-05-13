package org.iit.sahan.service;

import org.iit.sahan.db.CustomerDAO;
import org.iit.sahan.model.Customer;

import java.sql.SQLException;
import java.util.List;

public class CustomerService {
    private final CustomerDAO dao = new CustomerDAO();

    public void addCustomer(Customer c) throws SQLException {
        dao.addCustomer(c);
    }

    public void updateCustomer(int id, Customer c) throws SQLException {
        dao.updateCustomer(id, c);
    }

    public void deleteCustomer(int id) throws SQLException {
        dao.deleteCustomer(id);
    }

    public List<Customer> getAllCustomers() throws SQLException {
        return dao.getAllCustomers();
    }
}

