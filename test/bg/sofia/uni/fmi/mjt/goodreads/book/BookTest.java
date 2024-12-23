package bg.sofia.uni.fmi.mjt.goodreads.book;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BookTest {

    @Test
    void testOfValidToKillAMockingbird() {
        String[] tokens = {"0", "To Kill a Mockingbird", "Harper Lee",
            "The unforgettable novel of a childhood in a sleepy Southern town and the crisis of conscience that rocked it." +
                " \"To Kill A Mockingbird\" became both an instant bestseller and a critical success " +
                "when it was first published in 1960. It went on to win the Pulitzer Prize in 1961 and was later" +
                " made into an Academy Award-winning film, also a classic. Compassionate, dramatic, and deeply moving," +
                " \"To Kill A Mockingbird\" takes readers to the roots of human behavior - to innocence and experience," +
                " kindness and cruelty, love and hatred, humor and pathos.",
            "['Classics', 'Fiction', 'Historical Fiction', 'School', 'Literature', 'Young Adult', 'Historical']",
            "4.27", "5,691,311", "https://www.goodreads.com/book/show/2657.To_Kill_a_Mockingbird"};

        Book book = Book.of(tokens);
        assertEquals("0", book.ID());
        assertEquals("To Kill a Mockingbird", book.title());
        assertEquals("Harper Lee", book.author());
        assertTrue(book.description().startsWith("The unforgettable novel of a childhood"));
        assertEquals(
            List.of("Classics", "Fiction", "Historical Fiction", "School", "Literature", "Young Adult", "Historical"),
            book.genres());
        assertEquals(4.27, book.rating());
        assertEquals(5691311, book.ratingCount());
        assertEquals("https://www.goodreads.com/book/show/2657.To_Kill_a_Mockingbird", book.URL());
    }

    @Test
    void testInvalidGenresFormat() {
        String[] tokens =
            {"4", "Book Title", "Author", "Description", "Classics, Fiction", "4.5", "1000", "https://example.com"};
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Book.of(tokens));
        assertEquals("The genre string is not in the right format", exception.getMessage());
    }

    @Test
    void testInvalidRatingCount() {
        String[] tokens = {"5", "Book Title", "Author", "Description", "['Classics']", "4.5", "invalid_number",
            "https://example.com"};
        NumberFormatException exception = assertThrows(NumberFormatException.class, () -> Book.of(tokens));
        assertEquals("For input string: \"invalid_number\"", exception.getMessage());
    }

    @Test
    void testInvalidTokenLength() {
        String[] tokens = {"1", "Book Title", "Author", "Description", "['Classics']", "4.5", "1000"};
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Book.of(tokens));
        assertEquals("Incorrect book tokens count!", exception.getMessage());
    }

    @Test
    void testNullTokens() {
        String[] tokens = null;
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Book.of(tokens));
        assertEquals("Cannot parse null tokens!", exception.getMessage());
    }

    @Test
    void testNullTokenFields() {
        String[] tokens =
            {"1", "Book Title", null, "Description", "['Classics']", "4.5", "1000", "https://example.com"};
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Book.of(tokens));
        assertEquals("Cannot parse null tokens!", exception.getMessage());
    }

    @Test
    void testEmptyDescription() {
        String[] tokens = {"6", "Book Title", "Author", "", "['Classics']", "4.5", "1000", "https://example.com"};
        Book book = Book.of(tokens);
        assertEquals("", book.description());
    }

    @Test
    void testValidHarryPotter() {
        String[] tokens = {"1", "Harry Potter and the Philosopher’s Stone (Harry Potter, #1)", "J.K. Rowling",
            "Harry Potter thinks he is an ordinary boy - until he is rescued by an owl, taken to Hogwarts School" +
                " of Witchcraft and Wizardry, learns to play Quidditch and does battle in a deadly duel.",
            "['Fantasy', 'Fiction', 'Young Adult', 'Magic', 'Childrens', 'Middle Grade', 'Classics']", "4.47",
            "9,278,135", "https://www.goodreads.com/book/show/72193.Harry_Potter_and_the_Philosopher_s_Stone"};

        Book book = Book.of(tokens);
        assertEquals("1", book.ID());
        assertEquals("Harry Potter and the Philosopher’s Stone (Harry Potter, #1)", book.title());
        assertEquals("J.K. Rowling", book.author());
        assertTrue(book.description().startsWith("Harry Potter thinks he is an ordinary boy"));
        assertEquals(List.of("Fantasy", "Fiction", "Young Adult", "Magic", "Childrens", "Middle Grade", "Classics"),
            book.genres());
        assertEquals(4.47, book.rating());
        assertEquals(9278135, book.ratingCount());
        assertEquals("https://www.goodreads.com/book/show/72193.Harry_Potter_and_the_Philosopher_s_Stone", book.URL());
    }

    @Test
    void testMalformedUrl() {
        String[] tokens = {"7", "Book Title", "Author", "Description", "['Classics']", "4.5", "1000", "invalid_url"};
        Book book = Book.of(tokens);
        assertEquals("invalid_url", book.URL());
    }

    @Test
    void testEmptyGenres() {
        String[] tokens = {"8", "Book Title", "Author", "Description", "[]", "4.5", "1000", "https://example.com"};
        Book book = Book.of(tokens);
        assertTrue(book.genres().isEmpty());
    }

    @Test
    void testExtremeRating() {
        String[] tokens =
            {"9", "Book Title", "Author", "Description", "['Classics']", "10.0", "1000", "https://example.com"};
        Book book = Book.of(tokens);
        assertEquals(10.0, book.rating());
    }

    @Test
    void testNegativeRating() {
        String[] tokens =
            {"10", "Book Title", "Author", "Description", "['Classics']", "-1.0", "1000", "https://example.com"};
        Book book = Book.of(tokens);
        assertEquals(-1.0, book.rating());
    }

    @Test
    void testEmptyBook() {
        String[] tokens = {"11", "", "", "", "[]", "0.0", "0", "https://example.com"};
        Book book = Book.of(tokens);
        assertEquals("", book.title());
        assertEquals("", book.author());
        assertEquals("", book.description());
        assertEquals(0.0, book.rating());
        assertEquals(0, book.ratingCount());
    }

}
