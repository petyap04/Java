package bg.sofia.uni.fmi.mjt.newsfeed.client;

public class RequestBuilder {

    private String keywords;
    private String category;
    private String country;
    private int page;
    private int pageSize;

    public RequestBuilder withKeywords(String keywords) {
        if (keywords == null || keywords.isEmpty()) {
            throw new IllegalArgumentException("Keywords must not be null or empty");
        }
        this.keywords = keywords;
        return this;
    }

    public RequestBuilder withCategory(String category) {
        if (category == null || category.isEmpty()) {
            throw new IllegalArgumentException("Category must not be null or empty");
        }
        this.category = category;
        return this;
    }

    public RequestBuilder withCountry(String country) {
        if (country == null || country.isEmpty()) {
            throw new IllegalArgumentException("Country must not be null or empty");
        }
        this.country = country;
        return this;
    }

    public RequestBuilder withPage(int page) {
        if (page <= 0) {
            throw new IllegalArgumentException("Page number must be greater than 0");
        }
        this.page = page;
        return this;
    }

    public RequestBuilder withPageSize(int pageSize) {
        if (pageSize <= 0) {
            throw new IllegalArgumentException("Page size must be greater than 0");
        }
        this.pageSize = pageSize;
        return this;
    }

    public String getKeywords() {
        return keywords;
    }

    public String getCategory() {
        return category;
    }

    public String getCountry() {
        return country;
    }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }
}
