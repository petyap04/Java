package bg.sofia.uni.fmi.mjt.newsfeed.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RequestCacheTest {

    private RequestCache requestCache;

    @BeforeEach
    void setUp() {
        requestCache = new RequestCache();
    }

    @Test
    void testGetCachedResponseWhenCacheIsEmpty() {
        String requestUrl = "https://example.com/news";
        Optional<String> cachedResponse = requestCache.getCachedResponse(requestUrl);
        assertTrue(cachedResponse.isEmpty(), "Cache should return an empty Optional when no response is cached.");
    }

    @Test
    void testCacheResponseAndRetrieveIt() {
        String requestUrl = "https://example.com/news";
        String response = "{\"title\":\"Example News\"}";

        requestCache.cacheResponse(requestUrl, response);

        Optional<String> cachedResponse = requestCache.getCachedResponse(requestUrl);
        assertTrue(cachedResponse.isPresent(), "Cache should return a non-empty Optional for a cached response.");
        assertEquals(response, cachedResponse.get(), "Cached response should match the stored value.");
    }

    @Test
    void testClearCache() {
        String requestUrl = "https://example.com/news";
        String response = "{\"title\":\"Example News\"}";

        requestCache.cacheResponse(requestUrl, response);
        requestCache.clearCache();

        Optional<String> cachedResponse = requestCache.getCachedResponse(requestUrl);
        assertTrue(cachedResponse.isEmpty(), "Cache should be empty after clearCache is called.");
    }

    @Test
    void testClearCacheEntry() {
        String requestUrl1 = "https://example.com/news1";
        String response1 = "{\"title\":\"News 1\"}";

        String requestUrl2 = "https://example.com/news2";
        String response2 = "{\"title\":\"News 2\"}";

        requestCache.cacheResponse(requestUrl1, response1);
        requestCache.cacheResponse(requestUrl2, response2);

        requestCache.clearCacheEntry(requestUrl1);

        Optional<String> cachedResponse1 = requestCache.getCachedResponse(requestUrl1);
        Optional<String> cachedResponse2 = requestCache.getCachedResponse(requestUrl2);

        assertTrue(cachedResponse1.isEmpty(), "Cache entry for requestUrl1 should be removed.");
        assertTrue(cachedResponse2.isPresent(), "Cache entry for requestUrl2 should still exist.");
        assertEquals(response2, cachedResponse2.get(),
            "Cached response for requestUrl2 should match the stored value.");
    }

    @Test
    void testOverrideCacheEntry() {
        String requestUrl = "https://example.com/news";
        String initialResponse = "{\"title\":\"Initial News\"}";
        String updatedResponse = "{\"title\":\"Updated News\"}";

        requestCache.cacheResponse(requestUrl, initialResponse);
        requestCache.cacheResponse(requestUrl, updatedResponse);

        Optional<String> cachedResponse = requestCache.getCachedResponse(requestUrl);
        assertTrue(cachedResponse.isPresent(), "Cache should contain the updated response.");
        assertEquals(updatedResponse, cachedResponse.get(), "Cached response should match the updated value.");
    }
}
