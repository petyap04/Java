package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Channel;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SmallTransactionsRuleTest {

    @Test
    public void testApplicable_AboveThreshold() {

        List<Transaction> transactions = List.of(
                new Transaction("1", "A", 5.0, LocalDateTime.now(), "Sofia", Channel.ATM),
                new Transaction("2", "A", 5.0, LocalDateTime.now().minusDays(1), "Sofia", Channel.ATM)
        );

        SmallTransactionsRule rule = new SmallTransactionsRule(1, 10.0, 0.5);

        assertTrue(rule.applicable(transactions));
    }

    @Test
    public void testNotApplicable_BelowThreshold() {

        List<Transaction> transactions = List.of(
                new Transaction("1", "B", 15.0, LocalDateTime.now(), "Sofia", Channel.ATM),
                new Transaction("2", "B", 20.0, LocalDateTime.now().minusDays(1), "Sofia", Channel.ATM)
        );

        SmallTransactionsRule rule = new SmallTransactionsRule(1, 10.0, 0.5);

        assertFalse(rule.applicable(transactions));
    }
}
