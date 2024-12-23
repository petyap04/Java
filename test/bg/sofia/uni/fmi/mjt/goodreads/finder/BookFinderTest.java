package bg.sofia.uni.fmi.mjt.goodreads.finder;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BookFinderTest {

    private BookFinder bookFinder;
    private TextTokenizer tokenizer;

    private final Book book1 = Book.of(new String[] {"0", "To Kill a Mockingbird", "Harper Lee",
        "The unforgettable novel of a childhood in a sleepy Southern town and the crisis of conscience that rocked it. " +
            "\"\"To Kill A Mockingbird\"\" became both an instant bestseller and a critical success " +
            "when it was first published in 1960. It went on to win the Pulitzer Prize in 1961 and " +
            "was later made into an Academy Award-winning film, also a classic.Compassionate, dramatic," +
            " and deeply moving, \"\"To Kill A Mockingbird\"\" takes readers to the roots of human behavior" +
            " - to innocence and experience, kindness and cruelty, love and hatred, humor and pathos. " +
            "Now with over 18 million copies in print and translated into forty languages, this regional story" +
            " by a young Alabama woman claims universal appeal. Harper Lee always considered her book to be a simple " +
            "love story. Today it is regarded as a masterpiece of American literature.",
        "['Classics', 'Fiction', 'Historical Fiction']", "4.27", "5,691,311",
        "https://www.goodreads.com/book/show/2657.To_Kill_a_Mockingbird"});

    private final Book book2 = Book.of(new String[] {"1", "Harry Potter and the Philosopher’s Stone", "J.K. Rowling",
        "Harry Potter thinks he is an ordinary boy - until he is rescued by an owl, " +
            "taken to Hogwarts School of Witchcraft and Wizardry, learns to play Quidditch and " +
            "does battle in a deadly duel. The Reason ... HARRY POTTER IS A WIZARD!",
        "['Fantasy', 'Fiction', 'Young Adult']", "4.47", "9,278,135",
        "https://www.goodreads.com/book/show/72193.Harry_Potter_and_the_Philosopher_s_Stone"});

    private final Book book3 = Book.of(new String[] {"2", "Pride and Prejudice", "Jane Austen",
        "Since its immediate success in 1813, Pride and Prejudice has remained one of the most popular novels" +
            " in the English language. Jane Austen called this brilliant work \"\"her own darling child\"\" and" +
            " its vivacious heroine, Elizabeth Bennet, \"\"as delightful a creature as ever appeared in print.\"\"" +
            " The romantic clash between the opinionated Elizabeth and her proud beau, Mr. Darcy, is a splendid " +
            "performance of civilized sparring. And Jane Austen's radiant wit sparkles as her characters dance a " +
            "delicate quadrille of flirtation and intrigue, making this book the most superb comedy of manners of " +
            "Regency England.Alternate cover edition of ISBN 9780679783268", "['Classics', 'Fiction', 'Romance']",
        "4.28", "3,944,155", "https://www.goodreads.com/book/show/1885.Pride_and_Prejudice"});

    @BeforeEach
    void setUp() {
        tokenizer = mock(TextTokenizer.class);
        Set<Book> books = Set.of(book1, book2, book3);
        bookFinder = new BookFinder(books, tokenizer);
    }

    @Test
    void testAllBooks() {
        Set<Book> allBooks = bookFinder.allBooks();

        assertEquals(3, allBooks.size());
        assertTrue(allBooks.contains(book1));
        assertTrue(allBooks.contains(book2));
        assertTrue(allBooks.contains(book3));
    }

    @Test
    void testAllGenres() {
        Set<String> genres = bookFinder.allGenres();

        assertEquals(Set.of("Classics", "Fiction", "Historical Fiction", "Fantasy", "Young Adult", "Romance"), genres);
    }

    @Test
    void testSearchByAuthorValid() {
        List<Book> booksByHarperLee = bookFinder.searchByAuthor("Harper Lee");

        assertEquals(1, booksByHarperLee.size());
        assertEquals("Harper Lee", booksByHarperLee.getFirst().author());
    }

    @Test
    void testSearchByAuthorInvalid() {
        List<Book> booksByUnknown = bookFinder.searchByAuthor("Unknown Author");

        assertTrue(booksByUnknown.isEmpty());
    }

    @Test
    void testSearchByGenresMatchAll() {
        Set<String> genres = Set.of("Classics", "Fiction");
        List<Book> matchingBooks = bookFinder.searchByGenres(genres, MatchOption.MATCH_ALL);

        assertEquals(2, matchingBooks.size());
        assertTrue(matchingBooks.contains(book1));
        assertTrue(matchingBooks.contains(book3));
    }

    @Test
    void testSearchByGenresMatchAny() {
        Set<String> genres = Set.of("Fantasy");
        List<Book> matchingBooks = bookFinder.searchByGenres(genres, MatchOption.MATCH_ANY);

        assertEquals(1, matchingBooks.size());
        assertTrue(matchingBooks.contains(book2));
    }

    @Test
    void testSearchByKeywordsMatchAll() {
        when(tokenizer.tokenize("To Kill a Mockingbird")).thenReturn(List.of("Kill", "Mockingbird"));
        when(tokenizer.tokenize(book1.description())).thenReturn(List.of("childhood", "town"));

        Set<String> keywords = Set.of("Kill", "Mockingbird");
        List<Book> matchingBooks = bookFinder.searchByKeywords(keywords, MatchOption.MATCH_ALL);

        assertEquals(1, matchingBooks.size());
        assertTrue(matchingBooks.contains(book1));
    }

    @Test
    void testSearchByKeywordsMatchAny() {
        when(tokenizer.tokenize("Pride and Prejudice")).thenReturn(List.of("Pride", "Prejudice"));
        when(tokenizer.tokenize(book3.description())).thenReturn(List.of("success", "1813"));

        Set<String> keywords = Set.of("Pride");
        List<Book> matchingBooks = bookFinder.searchByKeywords(keywords, MatchOption.MATCH_ANY);

        assertEquals(1, matchingBooks.size());
        assertTrue(matchingBooks.contains(book3));
    }

    @Test
    void testSearchByKeywordsNoMatch() {
        when(tokenizer.tokenize("Harry Potter and the Philosopher’s Stone")).thenReturn(List.of("Harry", "Stone"));
        when(tokenizer.tokenize(book2.description())).thenReturn(List.of("boy", "wizard"));

        Set<String> keywords = Set.of("NonexistentKeyword");
        List<Book> matchingBooks = bookFinder.searchByKeywords(keywords, MatchOption.MATCH_ALL);

        assertTrue(matchingBooks.isEmpty());
    }

    @Test
    void testConstructorWithNullBooks() {
        assertThrows(IllegalArgumentException.class, () -> new BookFinder(null, tokenizer));
    }

    @Test
    void testConstructorWithNullTokenizer() {
        assertThrows(IllegalArgumentException.class, () -> new BookFinder(Set.of(book1), null));
    }

    @Test
    void testSearchByAuthorWithNull() {
        assertThrows(IllegalArgumentException.class, () -> bookFinder.searchByAuthor(null));
    }

    @Test
    void testSearchByGenresWithNull() {
        assertThrows(IllegalArgumentException.class, () -> bookFinder.searchByGenres(null, MatchOption.MATCH_ANY));
    }

    @Test
    void testSearchByKeywordsWithNull() {
        assertThrows(IllegalArgumentException.class, () -> bookFinder.searchByKeywords(null, MatchOption.MATCH_ANY));
    }
}
