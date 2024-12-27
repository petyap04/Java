package bg.sofia.uni.fmi.mjt.goodreads.book;

public enum BookParseIndex {
    ID(0), TITLE(1), AUTHOR(2), DESCRIPTION(3), GENRES(4), RATING(5), RATING_COUNT(6), URL(7);

    private final int id;

    BookParseIndex(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
}