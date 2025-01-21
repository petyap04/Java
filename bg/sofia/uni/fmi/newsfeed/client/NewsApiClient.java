package bg.sofia.uni.fmi.newsfeed.client;

import bg.sofia.uni.fmi.newsfeed.config.NewsApiConfig;
import bg.sofia.uni.fmi.newsfeed.exception.BadRequestException;
import bg.sofia.uni.fmi.newsfeed.exception.ServerErrorException;
import bg.sofia.uni.fmi.newsfeed.exception.TooManyRequestsException;
import bg.sofia.uni.fmi.newsfeed.exception.UnauthorizedException;
import bg.sofia.uni.fmi.newsfeed.model.NewsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import bg.sofia.uni.fmi.newsfeed.exception.NewsApiException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.util.concurrent.TimeoutException;

public class NewsApiClient {

    private static final int OK_START = 200;
    private static final int OK_END = 299;
    private static final int BAD_REQUEST = 400;
    private static final int UNAUTHORIZED = 401;
    private static final int TOO_MANY_REQUESTS = 429;
    private static final int SERVER_ERROR_START = 500;
    private static final int SERVER_ERROR_END = 599;

    private final NewsApiConfig config;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient;

    public NewsApiClient(NewsApiConfig config) {
        this.config = config;
        this.httpClient =
            HttpClient.newBuilder().connectTimeout(Duration.ofMillis(config.connectTimeoutMillis())).build();
    }

    public NewsApiClient(NewsApiConfig config, HttpClient httpClient) {
        this.config = config;
        this.httpClient = httpClient;
    }

    public NewsResponse getTopHeadlines(RequestBuilder builder) throws NewsApiException, TimeoutException {
        validateKeywords(builder.getKeywords());
        String fullUrl = buildUrl(builder);

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(fullUrl)).GET()
            .timeout(Duration.ofMillis(config.readTimeoutMillis())).build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int responseCode = response.statusCode();

            if (isSuccess(responseCode)) {
                return objectMapper.readValue(response.body(), NewsResponse.class);
            } else {
                handleErrorResponse(responseCode, response.body());
            }
        } catch (HttpTimeoutException e) {
            throw new TimeoutException("Request timed out");
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new NewsApiException("Error connecting to News API", e);
        }

        throw new NewsApiException("Unhandled case occurred while processing the API response.");
    }

    private void validateKeywords(String keywords) {
        if (keywords == null || keywords.isEmpty()) {
            throw new IllegalArgumentException("Keywords must not be null or empty");
        }
    }

    private String buildUrl(RequestBuilder builder) {
        StringBuilder urlBuilder =
            new StringBuilder(config.baseUrl()).append("/top-headlines?").append("q=").append(builder.getKeywords());

        appendParam(urlBuilder, "category", builder.getCategory());
        appendParam(urlBuilder, "country", builder.getCountry());
        appendPositiveParam(urlBuilder, "page", builder.getPage());
        appendPositiveParam(urlBuilder, "pageSize", builder.getPageSize());

        urlBuilder.append("&apiKey=").append(config.apiKey());
        return urlBuilder.toString();
    }

    private void appendParam(StringBuilder sb, String key, String value) {
        if (value != null && !value.isEmpty()) {
            sb.append("&").append(key).append("=").append(value);
        }
    }

    private void appendPositiveParam(StringBuilder sb, String key, int value) {
        if (value > 0) {
            sb.append("&").append(key).append("=").append(value);
        }
    }

    private boolean isSuccess(int code) {
        return code >= OK_START && code <= OK_END;
    }

    private void handleErrorResponse(int responseCode, String errorBody) throws NewsApiException {
        if (responseCode == BAD_REQUEST) {
            throw new BadRequestException(errorBody);
        } else if (responseCode == UNAUTHORIZED) {
            throw new UnauthorizedException(errorBody);
        } else if (responseCode == TOO_MANY_REQUESTS) {
            throw new TooManyRequestsException(errorBody);
        } else if (responseCode >= SERVER_ERROR_START && responseCode <= SERVER_ERROR_END) {
            throw new ServerErrorException(errorBody);
        } else {
            throw new NewsApiException("Unknown error (status " + responseCode + "): " + errorBody);
        }
    }
}
