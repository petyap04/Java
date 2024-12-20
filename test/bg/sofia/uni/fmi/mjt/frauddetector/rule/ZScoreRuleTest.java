package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Channel;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ZScoreRuleTest {

    @Test
    public void testApplicable_AboveZScoreThreshold() {

        List<Transaction> transactions = List.of(
                new Transaction("1", "C", 100.0, LocalDateTime.now(), "Sofia", Channel.ATM),
                new Transaction("2", "C", 50.0, LocalDateTime.now().minusDays(1), "Sofia", Channel.BRANCH),
                new Transaction("3", "C", 10.0, LocalDateTime.now().minusDays(2), "Plovdiv", Channel.BRANCH)
        );

        ZScoreRule rule = new ZScoreRule(1.0, 0.5);

        assertTrue(rule.applicable(transactions));
    }

    @Test
    public void testNotApplicable_BelowZScoreThreshold() {

        List<Transaction> transactions = List.of(
                new Transaction("1", "D", 10.0, LocalDateTime.now(), "Sofia", Channel.ATM),
                new Transaction("2", "D", 15.0, LocalDateTime.now().minusDays(1), "Plovdiv", Channel.BRANCH)
        );

        ZScoreRule rule = new ZScoreRule(1.0, 0.5);

        assertFalse(rule.applicable(transactions));
    }
}
