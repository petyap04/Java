package bg.sofia.uni.fmi.mjt.goodreads.finder;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BookFinder implements BookFinderAPI {

    private Set<Book> books;
    private TextTokenizer tokenizer;

    public BookFinder(Set<Book> books, TextTokenizer tokenizer) {
        if (books == null || tokenizer == null) {
            throw new IllegalArgumentException("The books or the tokenizer are null");
        }
        this.books = books;
        this.tokenizer = tokenizer;
    }

    public Set<Book> allBooks() {
        return books;
    }

    @Override
    public List<Book> searchByAuthor(String authorName) {
        if (authorName == null) {
            throw new IllegalArgumentException("The author can't be null!");
        }
        List<Book> result = new ArrayList<>();
        for (Book curr : books) {
            if (curr.author().equals(authorName)) {
                result.add(curr);
            }
        }
        return result;
    }

    @Override
    public Set<String> allGenres() {
        Set<String> result = new HashSet<>();
        for (Book curr : books) {
            List<String> currGenres = curr.genres();
            result.addAll(currGenres);
        }
        return result;
    }

    @Override
    public List<Book> searchByGenres(Set<String> genres, MatchOption option) {
        if (genres == null || option == null) {
            throw new IllegalArgumentException("The genres or the option is null!");
        }

        Set<Book> result = new HashSet<>();
        if (option == MatchOption.MATCH_ALL) {
            for (Book curr : books) {
                if (curr.genres().containsAll(genres)) {
                    result.add(curr);
                }
            }
        } else if (option == MatchOption.MATCH_ANY) {
            for (Book curr : books) {
                for (String currGenre : genres) {
                    if (curr.genres().contains(currGenre)) {
                        result.add(curr);
                        break;
                    }
                }
            }
        }
        return new ArrayList<>(result);
    }

    @Override
    public List<Book> searchByKeywords(Set<String> keywords, MatchOption option) {
        if (keywords == null || option == null) {
            throw new IllegalArgumentException("The keywords or the option is null!");
        }

        Set<Book> result = new HashSet<>();

        for (Book book : books) {
            Set<String> titleTokens = new HashSet<>(tokenizer.tokenize(book.title()));
            Set<String> descriptionTokens = new HashSet<>(tokenizer.tokenize(book.description()));

            if (option == MatchOption.MATCH_ALL) {
                if (titleTokens.containsAll(keywords) || descriptionTokens.containsAll(keywords)) {
                    result.add(book);
                }
            } else if (option == MatchOption.MATCH_ANY) {
                for (String keyword : keywords) {
                    if (titleTokens.contains(keyword) || descriptionTokens.contains(keyword)) {
                        result.add(book);
                        break;
                    }
                }
            }
        }
        return new ArrayList<>(result);
    }

}