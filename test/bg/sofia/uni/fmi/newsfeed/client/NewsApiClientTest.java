package bg.sofia.uni.fmi.newsfeed.client;

import bg.sofia.uni.fmi.newsfeed.config.NewsApiConfig;
import bg.sofia.uni.fmi.newsfeed.exception.BadRequestException;
import bg.sofia.uni.fmi.newsfeed.exception.ServerErrorException;
import bg.sofia.uni.fmi.newsfeed.exception.TooManyRequestsException;
import bg.sofia.uni.fmi.newsfeed.exception.UnauthorizedException;
import bg.sofia.uni.fmi.newsfeed.model.NewsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NewsApiClientTest {

    private NewsApiClient client;
    private NewsApiConfig config;

    @BeforeEach
    void setUp() {
        config = new NewsApiConfig("test-api-key", "http://testapi.com", 5000, 5000);
        client = new NewsApiClient(config);
    }

    private HttpURLConnection mockConnection(int responseCode, String responseBody, boolean isError) throws Exception {
        HttpURLConnection mockConnection = mock(HttpURLConnection.class);
        when(mockConnection.getResponseCode()).thenReturn(responseCode);

        InputStream stream = new ByteArrayInputStream(responseBody.getBytes());
        if (isError) {
            when(mockConnection.getErrorStream()).thenReturn(stream);
        } else {
            when(mockConnection.getInputStream()).thenReturn(stream);
        }
        return mockConnection;
    }

    @Test
    void testGetTopHeadlines_Success() throws Exception {
        RequestBuilder builder =
            new RequestBuilder().withKeywords("test").withCategory("technology").withCountry("us").withPage(1)
                .withPageSize(10);

        String jsonResponse = """
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

        HttpURLConnection mockConnection = mockConnection(200, jsonResponse, false);

        NewsApiClient clientSpy = Mockito.spy(client);
        doReturn(mockConnection).when(clientSpy).createConnection(anyString());

        NewsResponse response = clientSpy.getTopHeadlines(builder);

        assertNotNull(response);
        assertEquals("ok", response.getStatus());
        assertEquals(1, response.getTotalResults());
        assertEquals("Article Title", response.getArticles()[0].getTitle());

        verify(mockConnection).disconnect();
    }

    @Test
    void testGetTopHeadlines_BadRequest() throws Exception {
        HttpURLConnection mockConnection = mockConnection(400, "Bad Request", true);

        NewsApiClient clientSpy = Mockito.spy(client);
        doReturn(mockConnection).when(clientSpy).createConnection(anyString());

        BadRequestException exception = assertThrows(BadRequestException.class,
            () -> clientSpy.getTopHeadlines(new RequestBuilder().withKeywords("test")));
        assertEquals("Bad Request: Bad Request", exception.getMessage());

        verify(mockConnection).disconnect();
    }

    @Test
    void testGetTopHeadlines_Unauthorized() throws Exception {
        HttpURLConnection mockConnection = mockConnection(401, "Unauthorized", true);

        NewsApiClient clientSpy = Mockito.spy(client);
        doReturn(mockConnection).when(clientSpy).createConnection(anyString());

        UnauthorizedException exception = assertThrows(UnauthorizedException.class,
            () -> clientSpy.getTopHeadlines(new RequestBuilder().withKeywords("test")));
        assertEquals("Unauthorized: Unauthorized", exception.getMessage());

        verify(mockConnection).disconnect();
    }

    @Test
    void testGetTopHeadlines_TooManyRequests() throws Exception {
        HttpURLConnection mockConnection = mockConnection(429, "Too Many Requests", true);

        NewsApiClient clientSpy = Mockito.spy(client);
        doReturn(mockConnection).when(clientSpy).createConnection(anyString());

        TooManyRequestsException exception = assertThrows(TooManyRequestsException.class,
            () -> clientSpy.getTopHeadlines(new RequestBuilder().withKeywords("test")));
        assertEquals("Too Many Requests: Too Many Requests", exception.getMessage());

        verify(mockConnection).disconnect();
    }

    @Test
    void testGetTopHeadlines_ServerError() throws Exception {
        HttpURLConnection mockConnection = mockConnection(500, "Internal Server Error", true);

        NewsApiClient clientSpy = Mockito.spy(client);
        doReturn(mockConnection).when(clientSpy).createConnection(anyString());

        ServerErrorException exception = assertThrows(ServerErrorException.class,
            () -> clientSpy.getTopHeadlines(new RequestBuilder().withKeywords("test")));
        assertEquals("Server Error: Internal Server Error", exception.getMessage());

        verify(mockConnection).disconnect();
    }

    @Test
    void testGetTopHeadlines_TimeoutException() throws Exception {
        NewsApiClient clientSpy = Mockito.spy(client);

        doThrow(new SocketTimeoutException()).when(clientSpy).createConnection(anyString());

        assertThrows(TimeoutException.class,
            () -> clientSpy.getTopHeadlines(new RequestBuilder().withKeywords("test")));
    }

    @Test
    void testGetTopHeadlines_InvalidKeywords() {
        RequestBuilder builder = new RequestBuilder();

        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> client.getTopHeadlines(builder));
        assertEquals("Keywords must not be null or empty", exception.getMessage());
    }
}
