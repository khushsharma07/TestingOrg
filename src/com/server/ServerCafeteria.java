package com.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerCafeteria {

    private static final int PORT = 12345;

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(ServerConfiguration.TOTAL_CONNECTIONS);
        try (final ServerSocket listenerSocket = new ServerSocket(ServerConfiguration.PORT)) {
            System.out.println("[SERVER] waiting for client on port" + ServerConfiguration.PORT);
            while (true) {
                Socket clientSocket = listenerSocket.accept();
                executor.submit(new ClientThread(clientSocket));
            }
        } catch (IOException issue) {
            System.err.println("Server error: " + issue.getLocalizedMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally{
            executor.shutdown();
        }
    }
}