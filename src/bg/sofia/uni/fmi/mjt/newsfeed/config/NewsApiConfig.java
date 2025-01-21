package bg.sofia.uni.fmi.mjt.newsfeed.config;

public record NewsApiConfig(String apiKey, String baseUrl, int connectTimeoutMillis, int readTimeoutMillis) {

    public NewsApiConfig {
        if (apiKey == null) {
            throw new IllegalArgumentException("API key cannot be null.");
        }
        if (baseUrl == null) {
            throw new IllegalArgumentException("Base URL cannot be null.");
        }
    }
}
