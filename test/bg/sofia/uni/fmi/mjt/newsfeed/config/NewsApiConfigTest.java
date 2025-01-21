package bg.sofia.uni.fmi.mjt.newsfeed.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class NewsApiConfigTest {

    @Test
    void testValidNewsApiConfig() {
        String apiKey = "validApiKey";
        String baseUrl = "https://newsapi.org/v2";
        int connectTimeoutMillis = 5000;
        int readTimeoutMillis = 10000;

        NewsApiConfig config = new NewsApiConfig(apiKey, baseUrl, connectTimeoutMillis, readTimeoutMillis);

        assertNotNull(config);
        assertEquals(apiKey, config.apiKey());
        assertEquals(baseUrl, config.baseUrl());
        assertEquals(connectTimeoutMillis, config.connectTimeoutMillis());
        assertEquals(readTimeoutMillis, config.readTimeoutMillis());
    }

    @Test
    void testNewsApiConfigWithNullApiKeyThrowsException() {
        String baseUrl = "https://newsapi.org/v2";
        int connectTimeoutMillis = 5000;
        int readTimeoutMillis = 10000;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new NewsApiConfig(null, baseUrl, connectTimeoutMillis, readTimeoutMillis);
        });

        assertEquals("API key cannot be null.", exception.getMessage());
    }

    @Test
    void testNewsApiConfigWithNullBaseUrlThrowsException() {
        String apiKey = "validApiKey";
        int connectTimeoutMillis = 5000;
        int readTimeoutMillis = 10000;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new NewsApiConfig(apiKey, null, connectTimeoutMillis, readTimeoutMillis);
        });

        assertEquals("Base URL cannot be null.", exception.getMessage());
    }

    @Test
    void testNewsApiConfigWithValidTimeoutValues() {
        String apiKey = "validApiKey";
        String baseUrl = "https://newsapi.org/v2";

        int validConnectTimeout = 0;
        int validReadTimeout = Integer.MAX_VALUE;

        NewsApiConfig config = new NewsApiConfig(apiKey, baseUrl, validConnectTimeout, validReadTimeout);

        assertNotNull(config);
        assertEquals(validConnectTimeout, config.connectTimeoutMillis());
        assertEquals(validReadTimeout, config.readTimeoutMillis());
    }

    @Test
    void testNewsApiConfigNegativeTimeoutsAreAccepted() {
        String apiKey = "validApiKey";
        String baseUrl = "https://newsapi.org/v2";

        int negativeConnectTimeout = -1;
        int negativeReadTimeout = -1;

        NewsApiConfig config = new NewsApiConfig(apiKey, baseUrl, negativeConnectTimeout, negativeReadTimeout);

        assertNotNull(config);
        assertEquals(negativeConnectTimeout, config.connectTimeoutMillis());
        assertEquals(negativeReadTimeout, config.readTimeoutMillis());
    }
}
