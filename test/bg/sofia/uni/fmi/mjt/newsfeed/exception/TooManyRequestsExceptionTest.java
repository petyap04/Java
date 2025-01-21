package bg.sofia.uni.fmi.mjt.newsfeed.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TooManyRequestsExceptionTest {

    @Test
    void testTooManyRequestsExceptionMessage() {
        String errorMessage = "Rate limit exceeded";
        TooManyRequestsException exception = new TooManyRequestsException(errorMessage);

        assertEquals("Too Many Requests: " + errorMessage, exception.getMessage());
    }
}
