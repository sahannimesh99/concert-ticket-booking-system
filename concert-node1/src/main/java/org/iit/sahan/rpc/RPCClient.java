package org.iit.sahan.rpc;

import java.io.*;
import java.net.Socket;

public class RPCClient {
    private final String host;
    private final int port;

    public RPCClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void send(String request) throws IOException {
        try (Socket socket = new Socket(host, port)) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println(request);
            in.readLine();
        }
    }
}
