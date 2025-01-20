package bg.sofia.uni.fmi.newsfeed.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


public class ArticleTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testArticleDeserialization() throws Exception {
        String json = """
                {
                    "source": {
                        "id": "source-id",
                        "name": "Source Name"
                    },
                    "author": "Ivan Ivanov",
                    "title": "Example Title",
                    "description": "This is a test description.",
                    "url": "https://example.com/article",
                    "publishedAt": "2025-01-20T10:15:30Z"
                }
                """;

        Article article = objectMapper.readValue(json, Article.class);

        assertNotNull(article);
        assertEquals("Ivan Ivanov", article.getAuthor());
        assertEquals("Example Title", article.getTitle());
        assertEquals("https://example.com/article", article.getUrl());
        assertNotNull(article.getSource());
        assertEquals("source-id", article.getSource().getId());
        assertEquals("Source Name", article.getSource().getName());
    }

    @Test
    void testArticleWithMissingFields() throws Exception {
        String json = """
                {
                    "title": "Example Title"
                }
                """;

        Article article = objectMapper.readValue(json, Article.class);

        assertNotNull(article);
        assertEquals("Example Title", article.getTitle());
        assertNull(article.getAuthor());
        assertNull(article.getSource());
    }
}
