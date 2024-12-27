package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.genres;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GenresOverlapSimilarityCalculatorTest {

    private GenresOverlapSimilarityCalculator calculator;
    private Book book1;
    private Book book2;
    private Book book3;
    private Book book4;

    @BeforeEach
    void setUp() {
        calculator = new GenresOverlapSimilarityCalculator();

        book1 = Book.of(new String[] {"0", "To Kill a Mockingbird", "Harper Lee",
            "The unforgettable novel of a childhood in a sleepy Southern town",
            "['Classics', 'Fiction', 'Historical Fiction', 'School', 'Literature', 'Young Adult', 'Historical']",
            "4.27", "5691311", "https://www.goodreads.com/book/show/2657.To_Kill_a_Mockingbird"});

        book2 = Book.of(new String[] {"1", "Harry Potter and the Philosopher’s Stone", "J.K. Rowling",
            "Harry Potter thinks he is an ordinary boy",
            "['Fantasy', 'Fiction', 'Young Adult', 'Magic', 'Children\'s', 'Middle Grade', 'Classics']", "4.47",
            "9278135", "https://www.goodreads.com/book/show/72193.Harry_Potter_and_the_Philosopher_s_Stone"});

        book3 = Book.of(new String[] {"2", "Pride and Prejudice", "Jane Austen",
            "Since its immediate success in 1813, Pride and Prejudice has remained one of the most popular novels",
            "['Classics', 'Fiction', 'Romance', 'Historical Fiction', 'Literature', 'Historical', 'Audiobook']", "4.28",
            "3944155", "https://www.goodreads.com/book/show/1885.Pride_and_Prejudice"});

        book4 = Book.of(new String[] {"3", "The Diary of a Young Girl", "Anne Frank",
            "Discovered in the attic in which she spent the last years of her life",
            "['Classics', 'Nonfiction', 'History', 'Biography', 'Memoir', 'Historical', 'Holocaust']", "4.18",
            "3488438", "https://www.goodreads.com/book/show/48855.The_Diary_of_a_Young_Girl"});
    }

    @Test
    void testCalculateSimilarityBooksWithCommonGenres() {
        double similarity = calculator.calculateSimilarity(book1, book2);
        assertEquals(0.42857142857142855, similarity);
    }

    @Test
    void testCalculateSimilarityBooksWithNoCommonGenres() {
        double similarity = calculator.calculateSimilarity(book2, book3);
        assertEquals(0.2857142857142857, similarity);
    }

    @Test
    void testCalculateSimilarityBooksWithIdenticalGenres() {
        double similarity = calculator.calculateSimilarity(book1, book3);
        assertEquals(0.7142857142857143, similarity);
    }

    @Test
    void testCalculateSimilarityBooksWithOneEmptyGenreList() {
        Book bookWithEmptyGenres = Book.of(new String[] {"4", "Empty Genres", "Author", "Description", "[]", "0.0", "0",
            "https://www.goodreads.com/book/show/0000.Empty_Genres"});
        double similarity = calculator.calculateSimilarity(book1, bookWithEmptyGenres);
        assertEquals(0, similarity);
    }

    @Test
    void testCalculateSimilarityBooksWithNull() {
        assertThrows(IllegalArgumentException.class, () -> calculator.calculateSimilarity(book1, null));
        assertThrows(IllegalArgumentException.class, () -> calculator.calculateSimilarity(null, book2));
    }

    @Test
    void testCalculateSimilarityBooksWithDifferentGenres() {
        double similarity = calculator.calculateSimilarity(book1, book4);
        assertEquals(0.2857142857142857, similarity);
    }
}
