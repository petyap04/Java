package bg.sofia.uni.fmi.mjt.newsfeed.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServerErrorExceptionTest {

    @Test
    void testServerErrorExceptionMessage() {
        String errorMessage = "Internal server error occurred";
        ServerErrorException exception = new ServerErrorException(errorMessage);

        assertEquals("Server Error: " + errorMessage, exception.getMessage());
    }
}
