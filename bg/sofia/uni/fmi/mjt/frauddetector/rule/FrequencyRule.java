package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FrequencyRule implements Rule {

    private final int transactionCountThreshold;
    private final TemporalAmount timeWindow;
    private final double weight;

    public FrequencyRule(int transactionCountThreshold, TemporalAmount timeWindow, double weight) {
        this.transactionCountThreshold = transactionCountThreshold;
        this.timeWindow = timeWindow;
        this.weight = weight;
    }

    @Override
    public boolean applicable(List<Transaction> transactions) {
        Map<LocalDateTime, Integer> transactionCounts = new HashMap<>();

        for (Transaction transaction : transactions) {
            LocalDateTime windowStart = transaction.transactionDate().minus(timeWindow);
            transactionCounts.putIfAbsent(windowStart, 0);
            transactionCounts.put(windowStart, transactionCounts.get(windowStart) + 1);
        }

        for (int count : transactionCounts.values()) {
            if (count > transactionCountThreshold) {
                return true;
            }
        }

        return false;
    }

    @Override
    public double weight() {
        return weight;
    }
}
