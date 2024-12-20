package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Channel;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LocationsRuleTest {

    @Test
    void testLocationsRuleApplicable() {
        LocationsRule rule = new LocationsRule(2, 0.3);

        List<Transaction> transactions = List.of(
                new Transaction("1", "1234", 50.0, LocalDateTime.now(), "Location1", Channel.ATM),
                new Transaction("2", "1234", 30.0, LocalDateTime.now(), "Location2", Channel.ATM)
        );

        assertTrue(rule.applicable(transactions));
    }

    @Test
    void testLocationsRuleNotApplicable() {
        LocationsRule rule = new LocationsRule(3, 0.3);

        List<Transaction> transactions = List.of(
                new Transaction("1", "1234", 50.0, LocalDateTime.now(), "Location1", Channel.ATM),
                new Transaction("2", "1234", 30.0, LocalDateTime.now(), "Location1", Channel.ATM)
        );

        assertFalse(rule.applicable(transactions));
    }
}
