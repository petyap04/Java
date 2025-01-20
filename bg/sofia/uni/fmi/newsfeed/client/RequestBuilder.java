package bg.sofia.uni.fmi.newsfeed.client;

public class RequestBuilder {

    private String keywords;
    private String category;
    private String country;
    private int page;
    private int pageSize;

    public RequestBuilder withKeywords(String keywords) {
        this.keywords = keywords;
        return this;
    }

    public RequestBuilder withCategory(String category) {
        this.category = category;
        return this;
    }

    public RequestBuilder withCountry(String country) {
        this.country = country;
        return this;
    }

    public RequestBuilder withPage(int page) {
        this.page = page;
        return this;
    }

    public RequestBuilder withPageSize(int pageSize) {
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
