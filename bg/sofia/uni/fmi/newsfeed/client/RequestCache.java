package bg.sofia.uni.fmi.newsfeed.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RequestCache {

    private final Map<String, String> cache;

    public RequestCache() {
        this.cache = new HashMap<>();
    }

    public Optional<String> getCachedResponse(String requestUrl) {
        return Optional.ofNullable(cache.get(requestUrl));
    }

    public void cacheResponse(String requestUrl, String response) {
        cache.put(requestUrl, response);
    }

    public void clearCache() {
        cache.clear();
    }

    public void clearCacheEntry(String requestUrl) {
        cache.remove(requestUrl);
    }
}
