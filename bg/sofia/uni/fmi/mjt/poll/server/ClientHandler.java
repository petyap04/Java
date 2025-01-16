package bg.sofia.uni.fmi.mjt.poll.server;

import bg.sofia.uni.fmi.mjt.poll.server.model.Poll;
import bg.sofia.uni.fmi.mjt.poll.server.repository.PollRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

public class ClientHandler implements Runnable {
    private static final String ERROR_INVALID_COMMAND = "{\"status\":\"ERROR\",\"message\":\"Invalid command.\"}";
    private static final String ERROR_INVALID_CREATE_COMMAND =
        "{\"status\":\"ERROR\",\"message\":\"Usage: create-poll <question> <option-1> <option-2> [... <option-N>]\"}";
    private static final String ERROR_INVALID_VOTE_COMMAND =
        "{\"status\":\"ERROR\",\"message\":\"Usage: submit-vote <poll-id> <option>\"}";
    private static final String ERROR_NO_POLLS = "{\"status\":\"ERROR\",\"message\":\"No active polls available.\"}";
    private static final String SUCCESS_DISCONNECT = "Goodbye!";
    private static final int MIN_CREATE_POLL_ARGS = 3;
    private static final int VALID_VOTE_ARGS = 3;
    private static final int MINIMUM_REQUIRED_TOKENS = 4;
    private static final int MINIMUM_OPTIONS_COUNT = 2;

    private final Socket clientSocket;
    private final PollRepository pollRepository;

    public ClientHandler(Socket clientSocket, PollRepository pollRepository) {
        this.clientSocket = clientSocket;
        this.pollRepository = pollRepository;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String command;
            while ((command = reader.readLine()) != null) {
                String[] tokens = command.split(" ");
                switch (tokens[0]) {
                    case "create-poll" -> handleCreatePoll(tokens, writer);
                    case "list-polls" -> handleListPolls(writer);
                    case "submit-vote" -> handleSubmitVote(tokens, writer);
                    case "disconnect" -> {
                        writer.println(SUCCESS_DISCONNECT);
                        clientSocket.close();
                        return;
                    }
                    default -> writer.println(ERROR_INVALID_COMMAND);
                }
            }

        } catch (IOException e) {
            System.out.println("Client disconnected: " + e.getMessage());
        }
    }

    private void handleCreatePoll(String[] tokens, PrintWriter writer) {
        if (tokens.length < MIN_CREATE_POLL_ARGS) {
            writer.println(ERROR_INVALID_CREATE_COMMAND);
            return;
        }
        int optionsCount = tokens.length - 2;
        if (optionsCount < MINIMUM_OPTIONS_COUNT) {
            writer.println("{\"status\":\"ERROR\",\"message\":\"A poll must have at least two options.\"}");
            return;
        }
        String question = tokens[1];
        Map<String, Integer> options = new java.util.HashMap<>();
        for (int i = 2; i < tokens.length; i++) {
            options.put(tokens[i], 0);
        }
        int pollId = pollRepository.addPoll(new Poll(question, options));
        writer.println("{\"status\":\"OK\",\"message\":\"Poll " + pollId + " created successfully.\"}");
    }

    private void handleListPolls(PrintWriter writer) {
        Map<Integer, Poll> polls = pollRepository.getAllPolls();
        if (polls.isEmpty()) {
            writer.println(ERROR_NO_POLLS);
        } else {
            writer.println("{\"status\":\"OK\",\"polls\":" + polls.toString() + "}");
        }
    }

    private void handleSubmitVote(String[] tokens, PrintWriter writer) {
        if (tokens.length != VALID_VOTE_ARGS) {
            writer.println(ERROR_INVALID_VOTE_COMMAND);
            return;
        }
        int pollId;
        try {
            pollId = Integer.parseInt(tokens[1]);
        } catch (NumberFormatException e) {
            writer.println("{\"status\":\"ERROR\",\"message\":\"Poll ID must be a valid integer.\"}");
            return;
        }

        String option = tokens[2];
        Poll poll = pollRepository.getPoll(pollId);

        if (poll == null) {
            writer.println("{\"status\":\"ERROR\",\"message\":\"Poll with ID " + pollId + " does not exist.\"}");
            return;
        }

        if (!poll.options().containsKey(option)) {
            writer.println(
                "{\"status\":\"ERROR\",\"message\":\"Invalid option. Option " + option + " does not exist.\"}");
            return;
        }
        poll.options().put(option, poll.options().get(option) + 1);
        writer.println("{\"status\":\"OK\",\"message\":\"Vote submitted successfully for option: " + option + "\"}");
    }
}
