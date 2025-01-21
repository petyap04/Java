package bg.sofia.uni.fmi.mjt.newsfeed.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NewsApiExceptionTest {

    @Test
    void testNewsApiExceptionMessage() {
        String errorMessage = "General API error";
        NewsApiException exception = new NewsApiException(errorMessage);

        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void testNewsApiExceptionWithCause() {
        String errorMessage = "Error with cause";
        Throwable cause = new RuntimeException("Underlying cause");
        NewsApiException exception = new NewsApiException(errorMessage, cause);

        assertEquals(errorMessage, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
