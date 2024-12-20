package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.util.List;

public class ZScoreRule implements Rule {

    private final double zScoreThreshold;
    private final double weight;

    public ZScoreRule(double zScoreThreshold, double weight) {
        if (weight < 0.0 || weight > 1.0) {
            throw new IllegalArgumentException("Weight must be in the interval [0, 1]");
        }
        this.zScoreThreshold = zScoreThreshold;
        this.weight = weight;
    }

    @Override
    public boolean applicable(List<Transaction> transactions) {
        if (transactions.isEmpty()) {
            return false;
        }

        double mean = transactions.stream().mapToDouble(Transaction::transactionAmount).average().orElse(0);
        double variance = transactions.stream()
                .mapToDouble(t -> Math.pow(t.transactionAmount() - mean, 2))
                .average()
                .orElse(0);
        double stdDev = Math.sqrt(variance);

        for (Transaction transaction : transactions) {
            double zScore = (transaction.transactionAmount() - mean) / stdDev;
            if (zScore > zScoreThreshold) {
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
