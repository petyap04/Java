package bg.sofia.uni.fmi.mjt.todoist.client;

import java.util.Scanner;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Client {
    private static final String EXIT_COMMAND = "exit";
    private static final String HELP_COMMAND = "help";
    private static final String ADDRESS = "localhost";
    private static final int SERVER_PORT = 7676;

    public static void main(String[] args) {
        try (SocketChannel socketChannel = SocketChannel.open();
             BufferedReader reader = new BufferedReader(Channels.newReader(socketChannel, StandardCharsets.UTF_8));
             PrintWriter writer = new PrintWriter(Channels.newWriter(socketChannel, StandardCharsets.UTF_8), true);
             Scanner scanner = new Scanner(System.in)) {
            socketChannel.connect(new InetSocketAddress(ADDRESS, SERVER_PORT));
            displayStartingInformation();

            while (true) {
                String request = scanner.nextLine();
                if (request.equals(HELP_COMMAND)) {
                    displayHelpInformation();
                    continue;
                }

                writer.println(request);
                if (request.equals(EXIT_COMMAND)) {
                    break;
                }

                printResponse(reader);
            }
        } catch (IOException e) {
            System.out.println("Error with the network communication");
        } catch (Exception e) {
            System.out.println("An unknown error has occurred");
        }
    }

    private static void printResponse(BufferedReader reader) throws IOException {
        String response;
        do {
            response = reader.readLine();
            System.out.println(response);
        } while (reader.ready());
    }

    private static void displayStartingInformation() {
        System.out.println("Welcome to Todoist!");
        System.out.println("Write 'help' for a list of commands");
    }

    private static final String HELP_INFORMATION = """
        List of commands:
        ===Users===
        register <username> <password>
        login <username> <password>
        exit
        
        ===Tasks===
        add-task --name=<name> --date=<date> --due-date=<due-date> --description=<description>
        update-task --name=<name> --date=<date> --due-date=<due-date> --description=<description>
        delete-task --name=<task_name> --date=<date>
        get-task --name=<task_name> --date=<date>
        list-tasks --date=<date> --completed
        list-dashboard
        finish-task --name=<name> --date=<date>
        
        ===Collaborations===
        add-collaboration --name=<name>
        delete-collaboration --name=<name>
        list-collaborations
        add-user --collaboration=<name> --user=<username>
        assign-task --collaboration=<name> --user=<username> --task=<name> --date=<date>
        list-tasks --collaboration=<name>
        list-users --collaboration=<name>
        
        ===Labels===
        add-label --name=<name>
        delete-label --name=<name>
        list-labels
        label-task --name=<name> --date=<date> --label=<name>
        list-tasks --label=<name>
        
        *Date format must be as follows: dd-MM-yyyy
        """;

    private static void displayHelpInformation() {
        System.out.println(HELP_INFORMATION);
    }
}
