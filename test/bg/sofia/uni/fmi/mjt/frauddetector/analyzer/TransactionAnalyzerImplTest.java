package bg.sofia.uni.fmi.mjt.frauddetector.analyzer;

import bg.sofia.uni.fmi.mjt.frauddetector.rule.Rule;
import bg.sofia.uni.fmi.mjt.frauddetector.rule.SmallTransactionsRule;
import bg.sofia.uni.fmi.mjt.frauddetector.rule.ZScoreRule;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Channel;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;
import org.junit.jupiter.api.Test;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionAnalyzerImplTest {

    @Test
    public void testAccountRating_AllRulesMatch() {

        List<Transaction> transactions = List.of(
                new Transaction("1", "A", 5.0, LocalDateTime.now(), "Sofia", Channel.ATM),
                new Transaction("2", "A", 5.0, LocalDateTime.now().minusDays(1), "Sofia", Channel.ATM),
                new Transaction("3", "A", 50.0, LocalDateTime.now().minusDays(2), "Plovdiv", Channel.BRANCH)
        );

        Rule zScoreRule = new ZScoreRule(1.0, 0.5);
        Rule smallTransactionsRule = new SmallTransactionsRule(2, 10.0, 0.5);

        TransactionAnalyzer analyzer = new TransactionAnalyzerImpl(new StringReader(""), List.of(zScoreRule, smallTransactionsRule));

        double rating = analyzer.accountRating("A");

        assertEquals(1.0, rating, 0.0001);
    }

    @Test
    public void testAccountRating_NoApplicableRules() {

        List<Transaction> transactions = List.of(
                new Transaction("1", "B", 50.0, LocalDateTime.now(), "Sofia", Channel.ATM),
                new Transaction("2", "B", 40.0, LocalDateTime.now().minusDays(1), "Plovdiv", Channel.BRANCH)
        );

        Rule zScoreRule = new ZScoreRule(1.0, 0.5);
        Rule smallTransactionsRule = new SmallTransactionsRule(2, 10.0, 0.5);

        TransactionAnalyzer analyzer = new TransactionAnalyzerImpl(new StringReader(""), List.of(zScoreRule, smallTransactionsRule));

        double rating = analyzer.accountRating("B");

        assertEquals(0.0, rating, 0.0001);
    }

    @Test
    public void testAccountRating_SomeRulesMatch() {

        List<Transaction> transactions = List.of(
                new Transaction("1", "C", 5.0, LocalDateTime.now(), "Sofia", Channel.ATM),
                new Transaction("2", "C", 10.0, LocalDateTime.now().minusDays(1), "Sofia", Channel.ATM)
        );

        Rule zScoreRule = new ZScoreRule(1.0, 0.5);
        Rule smallTransactionsRule = new SmallTransactionsRule(2, 10.0, 0.5);

        TransactionAnalyzer analyzer = new TransactionAnalyzerImpl(new StringReader(""), List.of(zScoreRule, smallTransactionsRule));

        double rating = analyzer.accountRating("C");

        assertEquals(0.5, rating, 0.0001);
    }
}
