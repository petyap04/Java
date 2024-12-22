package bg.sofia.uni.fmi.mjt.goodreads.book;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public record Book(
        String ID,
        String title,
        String author,
        String description,
        List<String> genres,
        double rating,
        int ratingCount,
        String URL
) {
    private static final String GENRES_LIST_START = "[";
    private static final String GENRES_LIST_END = "]";
    private static final String GENRE_CONTENT_MARKER = "'";
    private static final String DATA_SEPARATOR = ",";

    private static String parseGenresStart(String begin) {
        StringBuilder result = new StringBuilder(begin);
        result.delete(0, GENRES_LIST_START.length());
        return result.toString();
    }

    private static String parseGenresEnd(String end) {
        StringBuilder result = new StringBuilder(end);
        result.delete(result.length() - GENRES_LIST_END.length(), result.length());
        return result.toString();
    }

    private static List<String> getGenres(String str) {
        if (!str.startsWith(GENRES_LIST_START) && !str.endsWith(GENRES_LIST_END)) {
            throw new IllegalArgumentException("The genre string is not in the right format");
        }

        String[] tokensOfGenres = str.split(DATA_SEPARATOR);
        tokensOfGenres[0] = parseGenresStart(tokensOfGenres[0]);
        tokensOfGenres[tokensOfGenres.length - 1] = parseGenresEnd(tokensOfGenres[tokensOfGenres.length - 1]);
        List<String> genres = new ArrayList<>();
        for (String curr : tokensOfGenres) {
            String trimmedGenre = curr.replace(GENRE_CONTENT_MARKER, "").trim();
            if (!trimmedGenre.isEmpty()) {
                genres.add(trimmedGenre);
            }
        }
        return genres;
    }

    private static int getRatingCount(String str) {
        return Integer.parseInt(str.replace(DATA_SEPARATOR, ""));
    }

    public static Book of(String[] tokens) {
        if (tokens == null || Arrays.stream(tokens).anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("Cannot parse null tokens!");
        }

        if (tokens.length != BookParseIndex.values().length) {
            throw new IllegalArgumentException("Incorrect book tokens count!");
        }

        tokens = Arrays.stream(tokens).map(String::trim).toArray(String[]::new);

        try {
            String id = tokens[BookParseIndex.ID.getId()];
            String title = tokens[BookParseIndex.TITLE.getId()];
            String author = tokens[BookParseIndex.AUTHOR.getId()];
            String description = tokens[BookParseIndex.DESCRIPTION.getId()];
            List<String> genres = getGenres(tokens[BookParseIndex.GENRES.getId()]);
            double rating = Double.parseDouble(tokens[BookParseIndex.RATING.getId()]);
            int ratingCount = getRatingCount(tokens[BookParseIndex.RATING_COUNT.getId()]);
            String url = tokens[BookParseIndex.URL.getId()];
            return new Book(id, title, author, description, genres, rating, ratingCount, url);
        } catch (Exception e) {
            throw new RuntimeException("Book data parsing failed!", e);
        }
    }
}