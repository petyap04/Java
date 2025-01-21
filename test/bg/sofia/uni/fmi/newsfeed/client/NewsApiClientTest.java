package bg.sofia.uni.fmi.newsfeed.client;

import bg.sofia.uni.fmi.newsfeed.config.NewsApiConfig;
import bg.sofia.uni.fmi.newsfeed.exception.BadRequestException;
import bg.sofia.uni.fmi.newsfeed.exception.ServerErrorException;
import bg.sofia.uni.fmi.newsfeed.exception.TooManyRequestsException;
import bg.sofia.uni.fmi.newsfeed.exception.UnauthorizedException;
import bg.sofia.uni.fmi.newsfeed.model.NewsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NewsApiClientTest {

    private NewsApiClient client;
    private HttpClient mockHttpClient;

    @BeforeEach
    void setUp() {
        NewsApiConfig config = new NewsApiConfig("test-api-key", "http://testapi.com", 5000, 5000);
        mockHttpClient = mock(HttpClient.class);
        client = new NewsApiClient(config, mockHttpClient);
    }

    @Test
    void testGetTopHeadlines_Success() throws Exception {
        RequestBuilder builder = createRequestBuilder();
        String jsonResponse = createMockSuccessResponse();
        mockHttpClientResponse(200, jsonResponse);

        NewsResponse response = client.getTopHeadlines(builder);

        assertNotNull(response);
        assertEquals("ok", response.getStatus());
        assertEquals(1, response.getTotalResults());
        assertEquals("Article Title", response.getArticles()[0].getTitle());
    }

    @Test
    void testGetTopHeadlines_BadRequest() throws Exception {
        mockHttpClientResponse(400, "Bad Request");

        BadRequestException exception =
            assertThrows(BadRequestException.class, () -> client.getTopHeadlines(createRequestBuilder()));
        assertEquals("Bad Request: Bad Request", exception.getMessage());
    }

    @Test
    void testGetTopHeadlines_Unauthorized() throws Exception {
        mockHttpClientResponse(401, "Unauthorized");

        UnauthorizedException exception =
            assertThrows(UnauthorizedException.class, () -> client.getTopHeadlines(createRequestBuilder()));
        assertEquals("Unauthorized: Unauthorized", exception.getMessage());
    }

    @Test
    void testGetTopHeadlines_TooManyRequests() throws Exception {
        mockHttpClientResponse(429, "Too Many Requests");

        TooManyRequestsException exception =
            assertThrows(TooManyRequestsException.class, () -> client.getTopHeadlines(createRequestBuilder()));
        assertEquals("Too Many Requests: Too Many Requests", exception.getMessage());
    }

    @Test
    void testGetTopHeadlines_ServerError() throws Exception {
        mockHttpClientResponse(500, "Internal Server Error");

        ServerErrorException exception =
            assertThrows(ServerErrorException.class, () -> client.getTopHeadlines(createRequestBuilder()));
        assertEquals("Server Error: Internal Server Error", exception.getMessage());
    }

    @Test
    void testGetTopHeadlines_TimeoutException() throws Exception {
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenThrow(
            new java.net.http.HttpTimeoutException("Request timed out"));

        assertThrows(TimeoutException.class, () -> client.getTopHeadlines(createRequestBuilder()));
    }

    @Test
    void testGetTopHeadlines_InvalidKeywords() {
        RequestBuilder builder = new RequestBuilder();

        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> client.getTopHeadlines(builder));
        assertEquals("Keywords must not be null or empty", exception.getMessage());
    }

    private RequestBuilder createRequestBuilder() {
        return new RequestBuilder().withKeywords("test").withCategory("technology").withCountry("us").withPage(1)
            .withPageSize(10);
    }

    private String createMockSuccessResponse() {
        return """
            {
              "status": "ok",
              "totalResults": 1,
              "articles": [
                {
                  "source": {"id": "source1", "name": "Source Name"},
                  "author": "Author Name",
                  "title": "Article Title",
                  "description": "Description",
                  "url": "http://testarticle.com",
                  "publishedAt": "2023-01-01T00:00:00Z"
                }
              ]
            }
            """;
    }

    private void mockHttpClientResponse(int statusCode, String body) throws Exception {
        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.statusCode()).thenReturn(statusCode);
        when(mockResponse.body()).thenReturn(body);
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(mockResponse);
    }
}
