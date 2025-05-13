//package org.iit.sahan.rpc;
//
//import org.iit.sahan.model.Concert;
//import org.iit.sahan.service.ConcertService;
//
//import java.io.*;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.sql.SQLException;
//
//public class RPCServer extends Thread {
//    private final int port;
//    private final ConcertService service;
//
//    public RPCServer(int port, ConcertService service) {
//        this.port = port;
//        this.service = service;
//    }
//
//    public void run() {
//        try (ServerSocket server = new ServerSocket(port)) {
//            while (true) {
//                Socket client = server.accept();
//                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
//                PrintWriter out = new PrintWriter(client.getOutputStream(), true);
//
//                String request = in.readLine();
//                String[] parts = request.split("\\|");
//                if (parts[0].equals("ADD")) {
//                    Concert c = new Concert();
//                    c.title = parts[1];
//                    c.date = parts[2];
//                    c.vipSeats = Integer.parseInt(parts[3]);
//                    c.regularSeats = Integer.parseInt(parts[4]);
//                    c.afterPartyTickets = Integer.parseInt(parts[5]);
//                    service.addConcert(c);
//                    out.println("OK");
//                }
//
//                client.close();
//            }
//        } catch (IOException | SQLException e) {
//            e.printStackTrace();
//        }
//    }
//}

package org.iit.sahan.rpc;

import com.google.gson.Gson;
import org.iit.sahan.model.Concert;
import org.iit.sahan.model.Customer;
import org.iit.sahan.service.ConcertService;
import org.iit.sahan.service.CustomerService;
import org.iit.sahan.model.Reservation;
import org.iit.sahan.service.ReservationService;
import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class RPCServer extends Thread {
    private final int port;
    private final ConcertService concertService;
    private final CustomerService customerService;
    private final ReservationService reservationService;
    private final Gson gson = new Gson();

    public RPCServer(int port, ConcertService concertService, CustomerService customerService) {
        this.port = port;
        this.concertService = concertService;
        this.customerService = customerService;
        this.reservationService = new ReservationService();
    }

    public void run() {
        try (ServerSocket server = new ServerSocket(port, 0, InetAddress.getByName("127.0.0.1"))) {
            System.out.println("RPC Server started on port " + port);
            while (true) {
                Socket client = server.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter out = new PrintWriter(client.getOutputStream(), true);

                String request = in.readLine();
                System.out.println("Received request: " + request);

                if (request == null || request.isEmpty()) {
                    out.println("Invalid request: empty");
                    client.close();
                    continue;
                }

                try {
                    if (request.startsWith("ADD|")) {
                        String[] parts = request.split("\\|");
                        if (parts.length < 6) throw new IllegalArgumentException("Missing fields");
                        Concert c = new Concert();
                        c.title = parts[1];
                        c.date = parts[2];
                        c.vipSeats = Integer.parseInt(parts[3]);
                        c.regularSeats = Integer.parseInt(parts[4]);
                        c.afterPartyTickets = Integer.parseInt(parts[5]);
                        concertService.addConcert(c);
                        out.println("Concert added");

                    } else if (request.equals("GET_CONCERTS")) {
                        List<Concert> concerts = concertService.getAllConcerts();
                        out.println(gson.toJson(concerts));

                    } else if (request.startsWith("RESERVE_JSON|")) {
                        String json = request.replace("RESERVE_JSON|", "");
                        Reservation r = gson.fromJson(json, Reservation.class);
                        reservationService.addReservation(r);
                        out.println("Reservation added");

                    } else if (request.startsWith("BULK_RESERVE_JSON|")) {
                        String json = request.replace("BULK_RESERVE_JSON|", "");
                        Reservation[] reservations = gson.fromJson(json, Reservation[].class);
                        reservationService.addBulkReservations(List.of(reservations));
                        out.println("Bulk reservation successful");

                    } else if (request.startsWith("CUSTOMER_ADD_JSON|")) {
                        String json = request.replace("CUSTOMER_ADD_JSON|", "");
                        Customer c = gson.fromJson(json, Customer.class);
                        customerService.addCustomer(c);
                        out.println("Customer added");

                    } else if (request.startsWith("CUSTOMER_UPDATE_JSON|")) {
                        String[] parts = request.replace("CUSTOMER_UPDATE_JSON|", "").split("\\|", 2);
                        int id = Integer.parseInt(parts[0]);
                        Customer c = gson.fromJson(parts[1], Customer.class);
                        customerService.updateCustomer(id, c);
                        out.println("Customer updated");

                    } else if (request.startsWith("CUSTOMER_DELETE|")) {
                        int id = Integer.parseInt(request.replace("CUSTOMER_DELETE|", ""));
                        customerService.deleteCustomer(id);
                        out.println("Customer deleted");

                    } else if (request.equals("CUSTOMER_ALL")) {
                        List<Customer> customers = customerService.getAllCustomers();
                        out.println(gson.toJson(customers));

                    } else {
                        out.println("Unknown request");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    out.println("Error handling request: " + e.getMessage());
                }

                client.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

