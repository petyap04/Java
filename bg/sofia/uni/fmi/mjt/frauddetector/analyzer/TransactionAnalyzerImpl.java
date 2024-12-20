package bg.sofia.uni.fmi.mjt.frauddetector.analyzer;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Channel;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;
import bg.sofia.uni.fmi.mjt.frauddetector.rule.Rule;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class TransactionAnalyzerImpl implements TransactionAnalyzer {

    private final List<Transaction> transactions;
    private final List<Rule> rules;
    private static final double EPSILON = 1e-6;

    public TransactionAnalyzerImpl(Reader reader, List<Rule> rules) {
        this.rules = rules;

        double totalWeight = rules.stream().mapToDouble(Rule::weight).sum();
        if (Math.abs(totalWeight - 1.0) > EPSILON) {
            throw new IllegalArgumentException("The sum of rule weights must be 1.0");
        }

        this.transactions = new BufferedReader(reader).lines()
                .skip(1) // skip the header
                .map(Transaction::of)
                .collect(Collectors.toList());
    }

    @Override
    public List<Transaction> allTransactions() {
        return List.copyOf(transactions);
    }

    @Override
    public List<String> allAccountIDs() {
        return transactions.stream()
                .map(Transaction::accountID)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public Map<Channel, Integer> transactionCountByChannel() {
        return transactions.stream()
                .collect(Collectors.groupingBy(Transaction::channel, Collectors.summingInt(t -> 1)));
    }

    @Override
    public double amountSpentByUser(String accountID) {
        if (accountID == null || accountID.isEmpty()) {
            throw new IllegalArgumentException("Account ID cannot be null or empty");
        }

        return transactions.stream()
                .filter(t -> t.accountID().equals(accountID))
                .mapToDouble(Transaction::transactionAmount)
                .sum();
    }

    @Override
    public List<Transaction> allTransactionsByUser(String accountId) {
        if (accountId == null || accountId.isEmpty()) {
            throw new IllegalArgumentException("Account ID cannot be null or empty");
        }

        return transactions.stream()
                .filter(t -> t.accountID().equals(accountId))
                .collect(Collectors.toList());
    }

    @Override
    public double accountRating(String accountId) {
        List<Transaction> userTransactions = allTransactionsByUser(accountId);
        double totalWeight = 0.0;

        for (Rule rule : rules) {
            if (rule.applicable(userTransactions)) {
                totalWeight += rule.weight();
            }
        }

        return Math.min(totalWeight, 1.0);
    }

    @Override
    public SortedMap<String, Double> accountsRisk() {
        SortedMap<String, Double> accountsRiskMap = new TreeMap<>();

        for (String accountId : allAccountIDs()) {
            double rating = accountRating(accountId);
            accountsRiskMap.put(accountId, rating);
        }

        return accountsRiskMap;
    }
}
