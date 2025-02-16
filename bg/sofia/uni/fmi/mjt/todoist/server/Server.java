package bg.sofia.uni.fmi.mjt.todoist.server;

import bg.sofia.uni.fmi.mjt.todoist.server.database.UserDatabase;
import bg.sofia.uni.fmi.mjt.todoist.server.command.CommandCreator;
import bg.sofia.uni.fmi.mjt.todoist.server.command.CommandExecutor;
import bg.sofia.uni.fmi.mjt.todoist.server.database.CollaborationDatabase;
import bg.sofia.uni.fmi.mjt.todoist.server.application.Application;

import java.util.Set;
import java.util.Iterator;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.net.InetSocketAddress;
import java.nio.channels.Selector;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.channels.ServerSocketChannel;

public class Server {
    private static final String EXIT_COMMAND = "exit";
    private static final String SERVER_HOST = "localhost";
    private static final int DEFAULT_PORT = 7676;
    private static final int BUFFER_SIZE = 1024;
    private final int port;
    private final CommandExecutor commandExecutor;
    private ByteBuffer buffer;
    private Selector selector;
    private boolean isRunning;

    public Server(CommandExecutor commandExecutor) {
        this(DEFAULT_PORT, commandExecutor);
    }

    public Server(int port, CommandExecutor commandExecutor) {
        this.port = port;
        this.commandExecutor = commandExecutor;
    }

    public void start() {
        try (ServerSocketChannel server = ServerSocketChannel.open()) {
            buffer = ByteBuffer.allocate(BUFFER_SIZE);
            selector = Selector.open();

            configureServerSocketChannel(server);

            isRunning = true;
            while (isRunning) {
                int readyChannels = selector.select();
                if (readyChannels == 0) {
                    continue;
                }

                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> selectionKeyIterator = selectedKeys.iterator();

                while (selectionKeyIterator.hasNext()) {
                    processRequest(selectionKeyIterator.next());

                    selectionKeyIterator.remove();
                }
            }
        } catch (IOException e) {
            System.out.println("IO Exception reached: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Exception reached: " + e.getMessage());
        }
    }

    private void processRequest(SelectionKey key) throws IOException, InterruptedException {
        if (key.isReadable()) {
            SocketChannel socketChannel = (SocketChannel) key.channel();

            String clientMessage = getClientInput(socketChannel);

            String response = commandExecutor.execute(key, CommandCreator.newCommand(clientMessage));
            respond(socketChannel, response + System.lineSeparator());

            if (clientMessage.startsWith(EXIT_COMMAND)) {
                socketChannel.close();
                key.cancel();
            }
        } else if (key.isAcceptable()) {
            accept(key);
        }
    }

    private String getClientInput(SocketChannel clientChannel) throws IOException {
        buffer.clear();

        int readBytes = clientChannel.read(buffer);
        if (readBytes < 0) {
            clientChannel.close();
            return null;
        }

        buffer.flip();

        byte[] clientInputBytes = new byte[buffer.remaining()];
        buffer.get(clientInputBytes);

        return new String(clientInputBytes, StandardCharsets.UTF_8);
    }

    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
        SocketChannel accept = sockChannel.accept();

        accept.configureBlocking(false);
        accept.register(this.selector, SelectionKey.OP_READ);
    }

    public void stop() {
        this.isRunning = false;
        if (selector.isOpen()) {
            selector.wakeup();
        }
    }

    private void configureServerSocketChannel(ServerSocketChannel server) throws IOException {
        server.bind(new InetSocketAddress(SERVER_HOST, this.port));
        server.configureBlocking(false);
        server.register(this.selector, SelectionKey.OP_ACCEPT);
    }

    private void respond(SocketChannel clientChannel, String response) throws IOException {
        buffer.clear();
        buffer.put(response.getBytes());
        buffer.flip();

        clientChannel.write(buffer);
    }

    public static void main(String[] args) {
        try {
            Application app = new Application(new UserDatabase(), new CollaborationDatabase());
            CommandExecutor executor = new CommandExecutor(app);
            Server server = new Server(executor);
            server.start();
        } catch (Exception e) {
            System.out.println("Unexpected exception inside of main method! " + e.getMessage());
        }
    }
}
