package bg.sofia.uni.fmi.newsfeed.config;

public record NewsApiConfig(String apiKey, String baseUrl, int connectTimeoutMillis, int readTimeoutMillis) {
}
