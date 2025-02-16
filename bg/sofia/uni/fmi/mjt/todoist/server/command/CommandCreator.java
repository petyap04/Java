package bg.sofia.uni.fmi.mjt.todoist.server.command;

import java.util.List;
import java.util.ArrayList;

public class CommandCreator {
    private static final char COMMAND_ARGS_SEPARATOR = ' ';
    private static final char COMMAND_ARGS_GROUPING_SYMBOL = '"';
    private static final String COMMAND_ARGS_GROUPING_STRING = String.valueOf(COMMAND_ARGS_GROUPING_SYMBOL);

    public static Command newCommand(String clientInput) {
        List<String> tokens = CommandCreator.getCommandArguments(clientInput.trim());
        String[] args = tokens.subList(1, tokens.size()).toArray(new String[0]);

        return new Command(tokens.getFirst(), args);
    }

    private static List<String> getCommandArguments(String input) {
        List<String> tokens = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();

        boolean insideQuote = false;

        for (char symbol : input.toCharArray()) {
            if (symbol == COMMAND_ARGS_GROUPING_SYMBOL) {
                insideQuote = !insideQuote;
            }

            if (symbol == COMMAND_ARGS_SEPARATOR && !insideQuote) {
                tokens.add(stringBuilder.toString().replace(COMMAND_ARGS_GROUPING_STRING, ""));
                stringBuilder.delete(0, stringBuilder.length());
            } else {
                stringBuilder.append(symbol);
            }
        }

        tokens.add(stringBuilder.toString().replace(COMMAND_ARGS_GROUPING_STRING, ""));
        return tokens;
    }
}
