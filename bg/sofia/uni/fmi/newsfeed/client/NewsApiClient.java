package bg.sofia.uni.fmi.newsfeed.client;

import bg.sofia.uni.fmi.newsfeed.config.NewsApiConfig;
import bg.sofia.uni.fmi.newsfeed.exception.BadRequestException;
import bg.sofia.uni.fmi.newsfeed.exception.ServerErrorException;
import bg.sofia.uni.fmi.newsfeed.exception.TooManyRequestsException;
import bg.sofia.uni.fmi.newsfeed.exception.UnauthorizedException;
import bg.sofia.uni.fmi.newsfeed.model.NewsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import bg.sofia.uni.fmi.newsfeed.exception.NewsApiException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
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

    public NewsApiClient(NewsApiConfig config) {
        this.config = config;
    }

    public NewsResponse getTopHeadlines(RequestBuilder builder) throws NewsApiException, TimeoutException {
        validateKeywords(builder.getKeywords());
        String fullUrl = buildUrl(builder);
        HttpURLConnection connection = null;
        try {
            connection = createConnection(fullUrl);
            int responseCode = connection.getResponseCode();

            if (isSuccess(responseCode)) {
                return readSuccessResponse(connection);
            } else {
                handleErrorResponse(connection, responseCode);
            }
        } catch (SocketTimeoutException e) {
            throw new TimeoutException();
        } catch (IOException e) {
            throw new NewsApiException("I/O error connecting to News API", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
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

    protected HttpURLConnection createConnection(String fullUrl) throws IOException {
        URL url = new URL(fullUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(config.connectTimeoutMillis());
        connection.setReadTimeout(config.readTimeoutMillis());
        connection.setRequestMethod("GET");
        return connection;
    }

    private boolean isSuccess(int code) {
        return code >= OK_START && code <= OK_END;
    }

    private NewsResponse readSuccessResponse(HttpURLConnection connection) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            return objectMapper.readValue(reader, NewsResponse.class);
        }
    }

    private void handleErrorResponse(HttpURLConnection connection, int responseCode) throws NewsApiException {
        String errorMsg = readErrorStream(connection);

        if (responseCode == BAD_REQUEST) {
            throw new BadRequestException(errorMsg);
        } else if (responseCode == UNAUTHORIZED) {
            throw new UnauthorizedException(errorMsg);
        } else if (responseCode == TOO_MANY_REQUESTS) {
            throw new TooManyRequestsException(errorMsg);
        } else if (responseCode >= SERVER_ERROR_START && responseCode <= SERVER_ERROR_END) {
            throw new ServerErrorException(errorMsg);
        } else {
            throw new NewsApiException("Unknown error (status " + responseCode + "): " + errorMsg);
        }
    }

    private String readErrorStream(HttpURLConnection connection) throws NewsApiException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
            StringBuilder errorMsg = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                errorMsg.append(line);
            }
            return errorMsg.toString();
        } catch (IOException e) {
            throw new NewsApiException("Error reading error response stream", e);
        }
    }
}
