package bg.sofia.uni.fmi.mjt.newsfeed.model;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class NewsResponseTest {

    private static final Gson gson = new Gson();

    private NewsResponse newsResponse;
    private Article[] articles;

    @BeforeEach
    void setUp() {
        String json = "{" + "  \"status\": \"ok\"," + "  \"totalResults\": 2," + "  \"articles\": [" +
            "    {\"title\": \"Article 1\", \"author\": \"Author 1\"}," +
            "    {\"title\": \"Article 2\", \"author\": \"Author 2\"}" + "  ]" + "}";

        newsResponse = gson.fromJson(json, NewsResponse.class);
        articles = newsResponse.getArticles();
    }

    @Test
    void testGetStatus() {
        assertEquals("ok", newsResponse.getStatus(), "Status should match the expected value");
    }

    @Test
    void testGetTotalResults() {
        assertEquals(2, newsResponse.getTotalResults(), "Total results should match the expected value");
    }

    @Test
    void testGetArticles() {
        assertNotNull(articles, "Articles should not be null");
        assertEquals(2, articles.length, "Articles array length should match the expected value");

        assertEquals("Article 1", articles[0].getTitle(), "First article title should match");
        assertEquals("Author 1", articles[0].getAuthor(), "First article author should match");

        assertEquals("Article 2", articles[1].getTitle(), "Second article title should match");
        assertEquals("Author 2", articles[1].getAuthor(), "Second article author should match");
    }
}