package bg.sofia.uni.fmi.mjt.newsfeed.client;

import bg.sofia.uni.fmi.mjt.newsfeed.config.NewsApiConfig;
import bg.sofia.uni.fmi.mjt.newsfeed.exception.BadRequestException;
import bg.sofia.uni.fmi.mjt.newsfeed.model.NewsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

public class NewsApiClientTest {

    private NewsApiClient newsApiClient;
    private NewsApiConfig config;
    private HttpClient httpClientMock;
    private RequestCache requestCacheMock;

    @BeforeEach
    void setUp() {
        initializeMocks();
        initializeNewsApiClient();
    }

    private void initializeMocks() {
        config = mock(NewsApiConfig.class);
        when(config.baseUrl()).thenReturn("https://newsapi.org/v2");
        when(config.apiKey()).thenReturn("testApiKey");
        when(config.connectTimeoutMillis()).thenReturn(5000);
        when(config.readTimeoutMillis()).thenReturn(5000);

        httpClientMock = mock(HttpClient.class);
        requestCacheMock = mock(RequestCache.class);
    }

    private void initializeNewsApiClient() {
        newsApiClient = new NewsApiClient(config, httpClientMock, requestCacheMock);
    }

    @Test
    void testGetTopHeadlines_CachedResponse() throws Exception {
        RequestBuilder builder = createRequestBuilder("technology");
        String cachedResponse = "{\"status\":\"ok\",\"totalResults\":1,\"articles\":[]}";
        mockCachedResponse(cachedResponse);

        NewsResponse response = newsApiClient.getTopHeadlines(builder);

        validateCachedResponse(response);
    }

    private RequestBuilder createRequestBuilder(String keywords) {
        return new RequestBuilder().withKeywords(keywords);
    }

    private void mockCachedResponse(String cachedResponse) {
        when(requestCacheMock.getCachedResponse(anyString())).thenReturn(Optional.of(cachedResponse));
    }

    private void validateCachedResponse(NewsResponse response) {
        assertNotNull(response);
        assertEquals("ok", response.getStatus());
        assertEquals(1, response.getTotalResults());
        verify(requestCacheMock, times(1)).getCachedResponse(anyString());
        verifyNoInteractions(httpClientMock);
    }

    @Test
    void testGetTopHeadlines_SuccessfulResponse() throws Exception {
        RequestBuilder builder = createRequestBuilder("technology");
        mockEmptyCache();
        HttpResponse<String> httpResponseMock =
            createMockHttpResponse(200, "{\"status\":\"ok\",\"totalResults\":1,\"articles\":[]}");

        mockHttpClientResponse(httpResponseMock);

        NewsResponse response = newsApiClient.getTopHeadlines(builder);

        validateSuccessfulResponse(response, httpResponseMock.body());
    }

    private void mockEmptyCache() {
        when(requestCacheMock.getCachedResponse(anyString())).thenReturn(Optional.empty());
    }

    private HttpResponse<String> createMockHttpResponse(int statusCode, String body) {
        HttpResponse<String> httpResponseMock = mock(HttpResponse.class);
        when(httpResponseMock.statusCode()).thenReturn(statusCode);
        when(httpResponseMock.body()).thenReturn(body);
        return httpResponseMock;
    }

    private void mockHttpClientResponse(HttpResponse<String> httpResponseMock) throws Exception {
        when(httpClientMock.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(
            httpResponseMock);
    }

    private void validateSuccessfulResponse(NewsResponse response, String apiResponse)
        throws IOException, InterruptedException {
        assertNotNull(response);
        assertEquals("ok", response.getStatus());
        assertEquals(1, response.getTotalResults());
        verify(httpClientMock, times(1)).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
        verify(requestCacheMock, times(1)).cacheResponse(anyString(), eq(apiResponse));
    }

    @Test
    void testGetTopHeadlines_BadRequest() throws Exception {
        RequestBuilder builder = createRequestBuilder("technology");
        mockEmptyCache();
        HttpResponse<String> httpResponseMock = createMockHttpResponse(400, "Bad Request");

        mockHttpClientResponse(httpResponseMock);

        assertThrows(BadRequestException.class, () -> newsApiClient.getTopHeadlines(builder));
    }

    @Test
    void testGetTopHeadlines_Timeout() throws Exception {
        RequestBuilder builder = createRequestBuilder("technology");
        mockEmptyCache();
        mockHttpClientTimeout();

        assertThrows(TimeoutException.class, () -> newsApiClient.getTopHeadlines(builder));
    }

    private void mockHttpClientTimeout() throws Exception {
        when(httpClientMock.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenThrow(
            new java.net.http.HttpTimeoutException("Timeout"));
    }

}
