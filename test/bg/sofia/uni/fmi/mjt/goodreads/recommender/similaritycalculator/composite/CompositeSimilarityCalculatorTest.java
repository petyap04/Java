package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.composite;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.descriptions.TFIDFSimilarityCalculator;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.genres.GenresOverlapSimilarityCalculator;
import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CompositeSimilarityCalculatorTest {

    private CompositeSimilarityCalculator compositeCalculator;
    private Book book1;
    private Book book2;
    private Book book3;
    private Set<Book> books;
    private TextTokenizer tokenizer;

    @BeforeEach
    void setUp() {
        books = new HashSet<>();
        tokenizer = new TextTokenizer(new java.io.StringReader(""));

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

        Map<SimilarityCalculator, Double> similarityCalculators = new HashMap<>();
        similarityCalculators.put(new TFIDFSimilarityCalculator(books, tokenizer), 0.5);
        similarityCalculators.put(new GenresOverlapSimilarityCalculator(), 0.5);

        compositeCalculator = new CompositeSimilarityCalculator(similarityCalculators);
    }

    @Test
    void testCalculateSimilarity() {
        double similarity = compositeCalculator.calculateSimilarity(book1, book2);
        assertTrue(similarity >= 0 && similarity <= 1);
    }

    @Test
    void testCalculateSimilarityWithIdenticalBooks() {
        double similarity = compositeCalculator.calculateSimilarity(book1, book1);
        assertEquals(1.0, similarity);
    }

    @Test
    void testCalculateSimilarityBooksWithNoCommonTerms() {
        double similarity = compositeCalculator.calculateSimilarity(book1, book3);
        assertTrue(similarity >= 0 && similarity <= 1);
    }

    @Test
    void testCalculateSimilarityBooksWithNull() {
        assertThrows(IllegalArgumentException.class, () -> compositeCalculator.calculateSimilarity(book1, null));
        assertThrows(IllegalArgumentException.class, () -> compositeCalculator.calculateSimilarity(null, book2));
    }

    @Test
    void testCalculateSimilarityWithNullSimilarityCalculatorMap() {
        assertThrows(IllegalArgumentException.class, () -> new CompositeSimilarityCalculator(null));
    }

    @Test
    void testCalculateSimilarityWithEmptySimilarityCalculatorMap() {
        Map<SimilarityCalculator, Double> emptyMap = new HashMap<>();
        CompositeSimilarityCalculator emptyCompositeCalculator = new CompositeSimilarityCalculator(emptyMap);
        double similarity = emptyCompositeCalculator.calculateSimilarity(book1, book2);
        assertEquals(0.0, similarity);
    }

    @Test
    void testCalculateSimilarityWithNegativeWeight() {
        Map<SimilarityCalculator, Double> similarityCalculators = new HashMap<>();
        similarityCalculators.put(new TFIDFSimilarityCalculator(books, tokenizer), -0.5);
        similarityCalculators.put(new GenresOverlapSimilarityCalculator(), 0.5);

        CompositeSimilarityCalculator negativeWeightCalculator =
            new CompositeSimilarityCalculator(similarityCalculators);
        double similarity = negativeWeightCalculator.calculateSimilarity(book1, book2);
        assertTrue(similarity >= 0 && similarity <= 1);
    }
}
