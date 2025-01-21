package bg.sofia.uni.fmi.mjt.newsfeed.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RequestBuilderTest {

    @Test
    void testWithKeywordsValidInput() {
        String keywords = "technology";

        RequestBuilder builder = new RequestBuilder().withKeywords(keywords);

        assertEquals(keywords, builder.getKeywords());
    }

    @Test
    void testWithKeywordsThrowsExceptionForNull() {
        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> new RequestBuilder().withKeywords(null));

        assertEquals("Keywords must not be null or empty", exception.getMessage());
    }

    @Test
    void testWithKeywordsThrowsExceptionForEmptyString() {
        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> new RequestBuilder().withKeywords(""));

        assertEquals("Keywords must not be null or empty", exception.getMessage());
    }

    @Test
    void testWithCategoryValidInput() {
        String category = "sports";

        RequestBuilder builder = new RequestBuilder().withCategory(category);

        assertEquals(category, builder.getCategory());
    }

    @Test
    void testWithCategoryThrowsExceptionForNull() {
        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> new RequestBuilder().withCategory(null));

        assertEquals("Category must not be null or empty", exception.getMessage());
    }

    @Test
    void testWithCategoryThrowsExceptionForEmptyString() {
        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> new RequestBuilder().withCategory(""));

        assertEquals("Category must not be null or empty", exception.getMessage());
    }

    @Test
    void testWithCountryValidInput() {
        String country = "US";

        RequestBuilder builder = new RequestBuilder().withCountry(country);

        assertEquals(country, builder.getCountry());
    }

    @Test
    void testWithCountryThrowsExceptionForNull() {
        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> new RequestBuilder().withCountry(null));

        assertEquals("Country must not be null or empty", exception.getMessage());
    }

    @Test
    void testWithCountryThrowsExceptionForEmptyString() {
        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> new RequestBuilder().withCountry(""));

        assertEquals("Country must not be null or empty", exception.getMessage());
    }

    @Test
    void testWithPageValidInput() {
        int page = 1;

        RequestBuilder builder = new RequestBuilder().withPage(page);

        assertEquals(page, builder.getPage());
    }

    @Test
    void testWithPageThrowsExceptionForZero() {
        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> new RequestBuilder().withPage(0));

        assertEquals("Page number must be greater than 0", exception.getMessage());
    }

    @Test
    void testWithPageThrowsExceptionForNegative() {
        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> new RequestBuilder().withPage(-1));

        assertEquals("Page number must be greater than 0", exception.getMessage());
    }

    @Test
    void testWithPageSizeValidInput() {
        int pageSize = 20;

        RequestBuilder builder = new RequestBuilder().withPageSize(pageSize);

        assertEquals(pageSize, builder.getPageSize());
    }

    @Test
    void testWithPageSizeThrowsExceptionForZero() {
        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> new RequestBuilder().withPageSize(0));

        assertEquals("Page size must be greater than 0", exception.getMessage());
    }

    @Test
    void testWithPageSizeThrowsExceptionForNegative() {
        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> new RequestBuilder().withPageSize(-10));

        assertEquals("Page size must be greater than 0", exception.getMessage());
    }
}
