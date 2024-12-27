package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.descriptions;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TFIDFSimilarityCalculatorTest {

    private TFIDFSimilarityCalculator calculator;
    private Book book1;
    private Book book2;
    private Book book3;

    @BeforeEach
    void setUp() {
        Set<Book> books = new HashSet<>();

        TextTokenizer tokenizer = new TextTokenizer(new java.io.StringReader(""));

        book1 = Book.of(new String[] {"0", "To Kill a Mockingbird", "Harper Lee",
            "The unforgettable novel of a childhood in a sleepy Southern town",
            "['Classics', 'Fiction', 'Historical Fiction', 'School', 'Literature', 'Young Adult', 'Historical']",
            "4.27", "5691311", "https://www.goodreads.com/book/show/2657.To_Kill_a_Mockingbird"});

        book2 = Book.of(new String[] {"1", "Harry Potter and the Philosopher’s Stone", "J.K. Rowling",
            "Harry Potter thinks he is an ordinary boy",
            "['Fantasy', 'Fiction', 'Young Adult', 'Magic', 'Children\'s', 'Middle Grade', 'Classics']", "4.47",
            "9278135", "https://www.goodreads.com/book/show/72193.Harry_Potter_and_the_Philosopher_s_Stone"});

        book3 = Book.of(new String[] {"3", "The Diary of a Young Girl", "Anne Frank",
            "Discovered in the attic in which she spent the last years of her life",
            "['Classics', 'Nonfiction', 'History', 'Biography', 'Memoir', 'Historical', 'Holocaust']", "4.18",
            "3488438", "https://www.goodreads.com/book/show/48855.The_Diary_of_a_Young_Girl"});

        books.add(book1);
        books.add(book2);
        books.add(book3);

        calculator = new TFIDFSimilarityCalculator(books, tokenizer);
    }

    @Test
    void testCalculateSimilarity() {
        double similarity = calculator.calculateSimilarity(book1, book2);
        assertTrue(similarity >= 0 && similarity <= 1);
    }

    @Test
    void testCalculateSimilarityWithIdenticalBooks() {
        double similarity = calculator.calculateSimilarity(book1, book1);
        assertEquals(1.0, similarity);
    }

    @Test
    void testCalculateSimilarityBooksWithNoCommonTerms() {
        double similarity = calculator.calculateSimilarity(book1, book3);
        assertTrue(similarity >= 0 && similarity <= 1);
    }

    @Test
    void testComputeTFIDF() {
        Map<String, Double> tfidfScores = calculator.computeTFIDF(book1);
        assertNotNull(tfidfScores);
        assertFalse(tfidfScores.isEmpty());
    }

    @Test
    void testComputeTF() {
        Map<String, Double> tfScores = calculator.computeTF(book1);
        assertNotNull(tfScores);
        assertFalse(tfScores.isEmpty());
    }

    @Test
    void testComputeIDF() {
        Map<String, Double> idfScores = calculator.computeIDF();
        assertNotNull(idfScores);
        assertFalse(idfScores.isEmpty());
    }

    @Test
    void testCosineSimilarity() {
        Map<String, Double> tfidfScoresFirst = calculator.computeTFIDF(book1);
        Map<String, Double> tfidfScoresSecond = calculator.computeTFIDF(book2);
        double similarity = calculator.cosineSimilarity(tfidfScoresFirst, tfidfScoresSecond);
        assertTrue(similarity >= 0 && similarity <= 1);
    }
}
