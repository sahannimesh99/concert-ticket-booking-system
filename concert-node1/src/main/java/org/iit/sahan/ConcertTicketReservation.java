package org.iit.sahan;

import org.iit.sahan.rpc.RPCServer;
import org.iit.sahan.service.ConcertService;
import org.iit.sahan.service.CustomerService;

public class ConcertTicketReservation {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java ConcertTicketReservation <port>");
            return;
        }

        try {
            int port = Integer.parseInt(args[0]);

            ConcertService concertService = new ConcertService();
            CustomerService customerService = new CustomerService();

            // Start the RPC server
            System.out.println("Starting Concert Ticket Reservation Node on port " + port);
            if (port == 9001) {
                System.out.println("This node is acting as the leader.");
            }

            new RPCServer(port, concertService, customerService).start();

        } catch (NumberFormatException e) {
            System.err.println("Invalid port number: " + args[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
