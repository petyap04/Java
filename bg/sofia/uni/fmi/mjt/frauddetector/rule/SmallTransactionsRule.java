package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.util.List;

public class SmallTransactionsRule implements Rule {

    private final int countThreshold;
    private final double amountThreshold;
    private final double weight;

    public SmallTransactionsRule(int countThreshold, double amountThreshold, double weight) {
        if (weight < 0.0 || weight > 1.0) {
            throw new IllegalArgumentException("Weight must be in the interval [0, 1]");
        }
        this.countThreshold = countThreshold;
        this.amountThreshold = amountThreshold;
        this.weight = weight;
    }

    @Override
    public boolean applicable(List<Transaction> transactions) {
        int smallTransactionCount = 0;

        for (Transaction transaction : transactions) {
            if (transaction.transactionAmount() < amountThreshold) {
                smallTransactionCount++;
            }
        }

        return smallTransactionCount > countThreshold;
    }

    @Override
    public double weight() {
        return weight;
    }
}
