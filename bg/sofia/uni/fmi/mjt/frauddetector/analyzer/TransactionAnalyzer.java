package bg.sofia.uni.fmi.mjt.frauddetector.analyzer;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Channel;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;

public interface TransactionAnalyzer {

    List<Transaction> allTransactions();

    List<String> allAccountIDs();

    Map<Channel, Integer> transactionCountByChannel();

    double amountSpentByUser(String accountID);

    List<Transaction> allTransactionsByUser(String accountId);

    double accountRating(String accountId);

    SortedMap<String, Double> accountsRisk();

}