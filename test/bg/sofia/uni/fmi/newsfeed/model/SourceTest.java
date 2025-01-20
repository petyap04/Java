package bg.sofia.uni.fmi.newsfeed.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SourceTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testSourceDeserialization() throws Exception {
        String json = """
                {
                    "id": "source-id",
                    "name": "Source Name"
                }
                """;

        Source source = objectMapper.readValue(json, Source.class);

        assertNotNull(source);
        assertEquals("source-id", source.getId());
        assertEquals("Source Name", source.getName());
    }

    @Test
    void testSourceWithMissingFields() throws Exception {
        String json = """
                {
                    "name": "Source Name"
                }
                """;

        Source source = objectMapper.readValue(json, Source.class);

        assertNotNull(source);
        assertNull(source.getId());
        assertEquals("Source Name", source.getName());
    }
}
