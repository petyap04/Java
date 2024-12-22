package bg.sofia.uni.fmi.mjt.goodreads.book;

import java.util.ArrayList;
import java.util.List;

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

    private static String getBegin(String begin) {
        if (begin == null) {
            throw new IllegalArgumentException("The string can't be null!");
        }
        if (begin.startsWith("\"[")) {
            StringBuilder result = new StringBuilder(begin);
            result.delete(0,2);
            return result.toString();
        }
        throw new IllegalArgumentException("Invalid beginning!");
    }

    private static String getEnd(String end) {
        if (end == null) {
            throw new IllegalArgumentException("The string can't be null!");
        }
        if (end.endsWith("]\"")) {
            StringBuilder result = new StringBuilder(end);
            result.delete(result.length() - 2, result.length());
            return result.toString();
        }
        throw new IllegalArgumentException("Invalid ending!");
    }

    private static List<String> getGenres(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Genres can't be null!");
        }

        if (!str.startsWith("\"[") && !str.endsWith("]\"")) {
            throw new IllegalArgumentException("The string is not in the right format");
        }
        String[] tokensOfGenres = str.split(",");
        tokensOfGenres[0] = getBegin(tokensOfGenres[0]);
        tokensOfGenres[tokensOfGenres.length - 1] = getEnd(tokensOfGenres[tokensOfGenres.length - 1]);
        List<String> genres = new ArrayList<>();
        for (String curr : tokensOfGenres) {
            String trimmedGenre = curr.replace("'", "").trim();
            if (!trimmedGenre.isEmpty()) {
                genres.add(trimmedGenre);
            }
        }
        return genres;
    }

    private static String getDescription(String string) {
        if (string == null) {
            throw new IllegalArgumentException("The string can't be null!");
        }
        if (!string.startsWith("\"") || !string.endsWith("\"")) {
            throw new IllegalArgumentException("The string is not in the right format!");
        }
        StringBuilder result = new StringBuilder(string);
        result.deleteCharAt(0);
        result.deleteCharAt(result.length() - 1);

        int index = result.indexOf("\"\"\"");
        while (index != -1) {
            result.replace(index, index + 3, "\"");
            index = result.indexOf("\"\"\"", index + 1);
        }
        return result.toString();
    }

    public static Book of(String[] tokens) {

        if (tokens == null) {
            throw new IllegalArgumentException("The tokens are null!");
        }

        try {
            String id = tokens[0].trim();
            String title = tokens[1].trim();
            String author = tokens[2].trim();
            String description = getDescription(tokens[3].trim());
            List<String> genres = getGenres(tokens[4].trim());
            double rating = Double.parseDouble(tokens[5].trim());
            int ratingCount = Integer.parseInt(tokens[6].trim());
            String URL = tokens[7].trim();
            return new Book(id, title, author, description, genres, rating, ratingCount, URL);
        } catch (Throwable e) {
            throw new IllegalArgumentException("Failed to parsing for Book", e);
        }
    }
}