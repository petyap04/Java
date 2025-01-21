package bg.sofia.uni.fmi.mjt.newsfeed.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BadRequestExceptionTest {

    @Test
    void testBadRequestExceptionMessage() {
        String errorMessage = "Invalid request format";
        BadRequestException exception = new BadRequestException(errorMessage);

        assertEquals("Bad Request: " + errorMessage, exception.getMessage());
    }
}
