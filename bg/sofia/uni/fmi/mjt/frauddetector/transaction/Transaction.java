package bg.sofia.uni.fmi.mjt.frauddetector.transaction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record Transaction(String transactionID, String accountID, double transactionAmount,
                          LocalDateTime transactionDate, String location, Channel channel) {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final int ZERO = 0;
    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int THREE = 3;
    public static final int FOUR = 4;
    public static final int FIVE = 5;
    public static final int SIX = 6;

    public static Transaction of(String line) {
        String[] parts = line.split(",");
        if (parts.length != SIX) {
            throw new IllegalArgumentException("Invalid transaction format: " + line);
        }

        String transactionID = parts[ZERO];
        String accountID = parts[ONE];
        double transactionAmount = Double.parseDouble(parts[TWO]);
        LocalDateTime transactionDate = LocalDateTime.parse(parts[THREE], DATE_FORMATTER);
        String location = parts[FOUR];
        Channel channel = Channel.valueOf(parts[FIVE].toUpperCase());

        return new Transaction(transactionID, accountID, transactionAmount, transactionDate, location, channel);
    }
}
