package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Channel;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FrequencyRuleTest {

    @Test
    public void testApplicable_FrequencyExceedsThreshold() {

        List<Transaction> transactions = List.of(
                new Transaction("1", "E", 5.0, LocalDateTime.now(), "Sofia", Channel.ATM),
                new Transaction("2", "E", 10.0, LocalDateTime.now().minusHours(1), "Plovdiv", Channel.ATM),
                new Transaction("3", "E", 20.0, LocalDateTime.now().minusHours(2), "Sofia", Channel.BRANCH)
        );

        FrequencyRule rule = new FrequencyRule(2, Period.ofDays(1), 0.4);

        assertTrue(rule.applicable(transactions));
    }

    @Test
    public void testNotApplicable_FrequencyBelowThreshold() {

        List<Transaction> transactions = List.of(
                new Transaction("1", "F", 50.0, LocalDateTime.now().minusDays(5), "Sofia", Channel.BRANCH)
        );

        FrequencyRule rule = new FrequencyRule(2, Period.ofDays(1), 0.4);

        assertFalse(rule.applicable(transactions));
    }
}
