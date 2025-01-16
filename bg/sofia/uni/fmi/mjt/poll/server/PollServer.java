package bg.sofia.uni.fmi.mjt.poll.server;

import bg.sofia.uni.fmi.mjt.poll.server.repository.PollRepository;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PollServer {
    private static final int DEFAULT_THREAD_POOL_SIZE = 10;

    private final int port;
    private final PollRepository pollRepository;
    private boolean isRunning;
    private ExecutorService clientPool;
    private ServerSocket serverSocket;

    public PollServer(int port, PollRepository pollRepository) {
        this.port = port;
        this.pollRepository = pollRepository;
    }

    public void start() {
        isRunning = true;
        clientPool = Executors.newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE);
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port: " + port);

            while (isRunning) {
                Socket clientSocket = serverSocket.accept();
                clientPool.execute(new ClientHandler(clientSocket, pollRepository));
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }

    public void stop() {
        isRunning = false;
        try {
            serverSocket.close();
            clientPool.shutdown();
        } catch (IOException e) {
            System.out.println("Error shutting down the server: " + e.getMessage());
        }
    }
}
