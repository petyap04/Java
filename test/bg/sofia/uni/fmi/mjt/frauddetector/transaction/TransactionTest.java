package bg.sofia.uni.fmi.mjt.frauddetector.transaction;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    @Test
    void testTransactionOf() {
        String line = "1,1234,50.5,2023-12-01T10:15:30,New York,ATM";

        Transaction transaction = Transaction.of(line);

        assertEquals("1", transaction.transactionID());
        assertEquals("1234", transaction.accountID());
        assertEquals(50.5, transaction.transactionAmount());
        assertEquals(LocalDateTime.of(2023, 12, 1, 10, 15, 30), transaction.transactionDate());
        assertEquals("New York", transaction.location());
        assertEquals(Channel.ATM, transaction.channel());
    }
}
