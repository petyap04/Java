package bg.sofia.uni.fmi.mjt.goodreads.finder;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;

import java.util.List;
import java.util.Set;

public interface BookFinderAPI {

    Set<Book> allBooks();

    Set<String> allGenres();

    List<Book> searchByAuthor(String authorName);

    List<Book> searchByGenres(Set<String> genres, MatchOption option);

    List<Book> searchByKeywords(Set<String> keywords, MatchOption option);

}