package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LocationsRule implements Rule {

    private final int threshold;
    private final double weight;

    public LocationsRule(int threshold, double weight) {
        this.threshold = threshold;
        this.weight = weight;
    }

    @Override
    public boolean applicable(List<Transaction> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            return false;
        }

        Set<String> uniqueLocations = new HashSet<>();
        for (Transaction transaction : transactions) {
            uniqueLocations.add(transaction.location());
        }

        return uniqueLocations.size() >= threshold;
    }

    @Override
    public double weight() {
        return weight;
    }
}
