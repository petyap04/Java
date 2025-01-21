package bg.sofia.uni.fmi.mjt.newsfeed.model;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ArticleTest {

    private static final Gson gson = new Gson();

    private Article article;

    @BeforeEach
    void setUp() {
        String json = "{" + "  \"source\": {\"id\": \"source-id\", \"name\": \"Source Name\"}," +
            "  \"author\": \"Ivan Ivanov\"," + "  \"title\": \"Sample Title\"," +
            "  \"description\": \"Sample Description\"," + "  \"url\": \"https://example.com\"," +
            "  \"publishedAt\": \"2025-01-21T10:00:00Z\"" + "}";
        article = gson.fromJson(json, Article.class);
    }

    @Test
    void testGetSource() {
        Source source = article.getSource();
        assertNotNull(source, "Source should not be null");
        assertEquals("source-id", source.getId(), "Source ID should match");
        assertEquals("Source Name", source.getName(), "Source Name should match");
    }

    @Test
    void testGetAuthor() {
        String author = article.getAuthor();
        assertNotNull(author, "Author should not be null");
        assertEquals("Ivan Ivanov", author, "Author should match the expected value");
    }

    @Test
    void testGetTitle() {
        String title = article.getTitle();
        assertNotNull(title, "Title should not be null");
        assertEquals("Sample Title", title, "Title should match the expected value");
    }

    @Test
    void testGetDescription() {
        String description = article.getDescription();
        assertNotNull(description, "Description should not be null");
        assertEquals("Sample Description", description, "Description should match the expected value");
    }

    @Test
    void testGetUrl() {
        String url = article.getUrl();
        assertNotNull(url, "URL should not be null");
        assertEquals("https://example.com", url, "URL should match the expected value");
    }

    @Test
    void testGetPublishedAt() {
        String publishedAt = article.getPublishedAt();
        assertNotNull(publishedAt, "PublishedAt should not be null");
        assertEquals("2025-01-21T10:00:00Z", publishedAt, "PublishedAt should match the expected value");
    }
}