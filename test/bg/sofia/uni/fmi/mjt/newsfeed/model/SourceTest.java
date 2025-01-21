package bg.sofia.uni.fmi.mjt.newsfeed.model;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SourceTest {

    private static final Gson gson = new Gson();

    private Source source;

    @BeforeEach
    void setUp() {
        String json = "{" + "  \"id\": \"source-id\"," + "  \"name\": \"Source Name\"" + "}";
        source = gson.fromJson(json, Source.class);
    }

    @Test
    void testGetId() {
        assertEquals("source-id", source.getId(), "Source ID should match the expected value");
    }

    @Test
    void testGetName() {
        assertEquals("Source Name", source.getName(), "Source name should match the expected value");
    }
}
