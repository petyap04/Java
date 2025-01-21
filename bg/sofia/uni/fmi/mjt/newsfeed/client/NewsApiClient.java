package bg.sofia.uni.fmi.mjt.newsfeed.client;

import bg.sofia.uni.fmi.mjt.newsfeed.config.NewsApiConfig;
import bg.sofia.uni.fmi.mjt.newsfeed.exception.BadRequestException;
import bg.sofia.uni.fmi.mjt.newsfeed.exception.ServerErrorException;
import bg.sofia.uni.fmi.mjt.newsfeed.exception.TooManyRequestsException;
import bg.sofia.uni.fmi.mjt.newsfeed.exception.UnauthorizedException;
import bg.sofia.uni.fmi.mjt.newsfeed.model.NewsResponse;
import bg.sofia.uni.fmi.mjt.newsfeed.exception.NewsApiException;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.util.Optional;
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
    private final HttpClient httpClient;
    private final RequestCache requestCache;
    private final Gson gson;

    public NewsApiClient(NewsApiConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("Config must not be null");
        }
        this.config = config;
        this.httpClient =
            HttpClient.newBuilder().connectTimeout(Duration.ofMillis(config.connectTimeoutMillis())).build();
        this.requestCache = new RequestCache();
        this.gson = new Gson(); // Инициализиране на Gson
    }

    public NewsApiClient(NewsApiConfig config, HttpClient httpClient, RequestCache requestCache) {
        if (config == null) {
            throw new IllegalArgumentException("Config must not be null");
        }
        if (httpClient == null) {
            throw new IllegalArgumentException("HttpClient must not be null");
        }
        if (requestCache == null) {
            throw new IllegalArgumentException("RequestCache must not be null");
        }
        this.config = config;
        this.httpClient = httpClient;
        this.requestCache = requestCache;
        this.gson = new Gson();
    }

    public NewsResponse getTopHeadlines(RequestBuilder builder) throws NewsApiException, TimeoutException {
        if (builder == null) {
            throw new IllegalArgumentException("RequestBuilder must not be null");
        }
        validateKeywords(builder.getKeywords());
        String fullUrl = buildUrl(builder);

        Optional<String> cachedResponse = getCachedResponse(fullUrl);
        if (cachedResponse.isPresent()) {
            return parseCachedResponse(cachedResponse.get());
        }

        return fetchAndCacheResponse(fullUrl);
    }

    private Optional<String> getCachedResponse(String fullUrl) {
        if (fullUrl == null) {
            throw new IllegalArgumentException("URL must not be null");
        }
        return requestCache.getCachedResponse(fullUrl);
    }

    private NewsResponse parseCachedResponse(String cachedResponse) throws NewsApiException {
        if (cachedResponse == null) {
            throw new IllegalArgumentException("Cached response must not be null");
        }
        try {
            return gson.fromJson(cachedResponse, NewsResponse.class);
        } catch (Exception e) {
            throw new NewsApiException("Error parsing cached response", e);
        }
    }

    private NewsResponse fetchAndCacheResponse(String fullUrl) throws NewsApiException, TimeoutException {
        if (fullUrl == null) {
            throw new IllegalArgumentException("URL must not be null");
        }
        HttpRequest request = createHttpRequest(fullUrl);
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return processHttpResponse(fullUrl, response);
        } catch (HttpTimeoutException e) {
            throw new TimeoutException("Request timed out");
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new NewsApiException("Error connecting to News API", e);
        }
    }

    private HttpRequest createHttpRequest(String fullUrl) {
        if (fullUrl == null) {
            throw new IllegalArgumentException("URL must not be null");
        }
        return HttpRequest.newBuilder().uri(URI.create(fullUrl)).GET()
            .timeout(Duration.ofMillis(config.readTimeoutMillis())).build();
    }

    private NewsResponse processHttpResponse(String fullUrl, HttpResponse<String> response) throws NewsApiException {
        if (fullUrl == null || response == null) {
            throw new IllegalArgumentException("Arguments must not be null");
        }
        int responseCode = response.statusCode();

        if (isSuccess(responseCode)) {
            cacheResponse(fullUrl, response.body());
            return parseHttpResponse(response.body());
        } else {
            handleErrorResponse(responseCode, response.body());
        }

        throw new NewsApiException("Unhandled case occurred while processing the API response.");
    }

    private NewsResponse parseHttpResponse(String responseBody) throws NewsApiException {
        if (responseBody == null) {
            throw new IllegalArgumentException("Response body must not be null");
        }
        try {
            return gson.fromJson(responseBody, NewsResponse.class);
        } catch (Exception e) {
            throw new NewsApiException("Error parsing API response", e);
        }
    }

    private void cacheResponse(String fullUrl, String responseBody) {
        if (fullUrl == null || responseBody == null) {
            throw new IllegalArgumentException("Arguments must not be null");
        }
        requestCache.cacheResponse(fullUrl, responseBody);
    }

    private void validateKeywords(String keywords) {
        if (keywords == null || keywords.isEmpty()) {
            throw new IllegalArgumentException("Keywords must not be null or empty");
        }
    }

    private String buildUrl(RequestBuilder builder) {
        if (builder == null) {
            throw new IllegalArgumentException("RequestBuilder must not be null");
        }
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
        if (sb == null || key == null) {
            throw new IllegalArgumentException("StringBuilder and key must not be null");
        }
        if (value != null && !value.isEmpty()) {
            sb.append("&").append(key).append("=").append(value);
        }
    }

    private void appendPositiveParam(StringBuilder sb, String key, int value) {
        if (sb == null || key == null) {
            throw new IllegalArgumentException("StringBuilder and key must not be null");
        }
        if (value > 0) {
            sb.append("&").append(key).append("=").append(value);
        }
    }

    private boolean isSuccess(int code) {
        return code >= OK_START && code <= OK_END;
    }

    private void handleErrorResponse(int responseCode, String errorBody) throws NewsApiException {
        if (errorBody == null) {
            throw new IllegalArgumentException("Error body must not be null");
        }
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
