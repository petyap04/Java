package bg.sofia.uni.fmi.newsfeed.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class NewsResponseTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testNewsResponseDeserialization() throws Exception {
        String json = """
            {
                "status": "ok",
                "totalResults": 1,
                "articles": [
                    {
                        "source": {
                            "id": "source-id",
                            "name": "Source Name"
                        },
                        "author": "Ivan Ivanov",
                        "title": "Example Title",
                        "description": "Description",
                        "url": "https://example.com/article",
                        "publishedAt": "2025-01-20T10:15:30Z"
                    }
                ]
            }
            """;

        NewsResponse response = objectMapper.readValue(json, NewsResponse.class);

        assertNotNull(response);
        assertEquals("ok", response.getStatus());
        assertEquals(1, response.getTotalResults());
        assertNotNull(response.getArticles());
        assertEquals(1, response.getArticles().length);

        Article article = response.getArticles()[0];
        assertEquals("Ivan Ivanov", article.getAuthor());
        assertEquals("Example Title", article.getTitle());
        assertNotNull(article.getSource());
        assertEquals("source-id", article.getSource().getId());
        assertEquals("Source Name", article.getSource().getName());
    }

    @Test
    void testNewsResponseWithNoArticles() throws Exception {
        String json = """
            {
                "status": "ok",
                "totalResults": 0,
                "articles": []
            }
            """;

        NewsResponse response = objectMapper.readValue(json, NewsResponse.class);

        assertNotNull(response);
        assertEquals("ok", response.getStatus());
        assertEquals(0, response.getTotalResults());
        assertNotNull(response.getArticles());
        assertEquals(0, response.getArticles().length);
    }
}
