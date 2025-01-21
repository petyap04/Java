package bg.sofia.uni.fmi.mjt.newsfeed.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RequestCache {

    private final Map<String, String> cache;

    public RequestCache() {
        this.cache = new HashMap<>();
    }

    public Optional<String> getCachedResponse(String requestUrl) {
        if (requestUrl == null) {
            throw new IllegalArgumentException("Request URL must not be null");
        }
        return Optional.ofNullable(cache.get(requestUrl));
    }

    public void cacheResponse(String requestUrl, String response) {
        if (requestUrl == null) {
            throw new IllegalArgumentException("Request URL must not be null");
        }
        if (response == null) {
            throw new IllegalArgumentException("Response must not be null");
        }
        cache.put(requestUrl, response);
    }

    public void clearCache() {
        cache.clear();
    }

    public void clearCacheEntry(String requestUrl) {
        if (requestUrl == null) {
            throw new IllegalArgumentException("Request URL must not be null");
        }
        cache.remove(requestUrl);
    }
}
