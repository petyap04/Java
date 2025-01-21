package bg.sofia.uni.fmi.mjt.newsfeed.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UnauthorizedExceptionTest {

    @Test
    void testUnauthorizedExceptionMessage() {
        String errorMessage = "Invalid API key";
        UnauthorizedException exception = new UnauthorizedException(errorMessage);

        assertEquals("Unauthorized: " + errorMessage, exception.getMessage());
    }
}
