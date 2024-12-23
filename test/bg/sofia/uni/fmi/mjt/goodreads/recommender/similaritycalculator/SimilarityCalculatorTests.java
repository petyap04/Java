package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.composite.CompositeSimilarityCalculator;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.descriptions.TFIDFSimilarityCalculator;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.genres.GenresOverlapSimilarityCalculator;
import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimilarityCalculatorTests {

    private Book book1;
    private Book book2;
    private Book book3;

    private GenresOverlapSimilarityCalculator genresCalculator;
    private TFIDFSimilarityCalculator tfidfCalculator;
    private CompositeSimilarityCalculator compositeCalculator;

    @BeforeEach
    void setUp() {
        book1 = new Book(
            "0",
            "To Kill a Mockingbird",
            "Harper Lee",
            "The unforgettable novel of a childhood in a sleepy Southern town and the crisis of conscience " +
                "that rocked it. To Kill A Mockingbird became both an instant bestseller and a critical success when it " +
                "was first published in 1960. It went on to win the Pulitzer Prize in 1961 and was later made into" +
                " an Academy Award-winning film, also a classic. Compassionate, dramatic, and deeply moving, " +
                "To Kill A Mockingbird takes readers to the roots of human behavior - to innocence and experience, " +
                "kindness and cruelty, love and hatred, humor and pathos. Now with over 18 million copies in print " +
                "and translated into forty languages, this regional story by a young Alabama woman claims universal " +
                "appeal. Harper Lee always considered her book to be a simple love story. " +
                "Today it is regarded as a masterpiece of American literature.",
            List.of("Classics", "Fiction", "Historical Fiction", "School", "Literature", "Young Adult", "Historical"),
            4.27,
            5691311,
            "https://www.goodreads.com/book/show/2657.To_Kill_a_Mockingbird"
        );

        book2 = new Book(
            "1",
            "Harry Potter and the Philosopher’s Stone",
            "J.K. Rowling",
            "Harry Potter thinks he is an ordinary boy - until he is rescued by an owl, " +
                "taken to Hogwarts School of Witchcraft and Wizardry, learns to play Quidditch and does battle" +
                " in a deadly duel. The Reason ... HARRY POTTER IS A WIZARD!",
            List.of("Fantasy", "Fiction", "Young Adult", "Magic", "Childrens", "Middle Grade", "Classics"),
            4.47,
            9278135,
            "https://www.goodreads.com/book/show/72193.Harry_Potter_and_the_Philosopher_s_Stone"
        );

        book3 = new Book(
            "2",
            "Pride and Prejudice",
            "Jane Austen",
            "Since its immediate success in 1813, Pride and Prejudice has remained one of the most popular" +
                " novels in the English language. Jane Austen called this brilliant work her own darling child and" +
                " its vivacious heroine, Elizabeth Bennet, as delightful a creature as ever appeared in print. " +
                "The romantic clash between the opinionated Elizabeth and her proud beau, Mr. Darcy, is a " +
                "splendid performance of civilized sparring. And Jane Austen's radiant wit sparkles as her characters" +
                " dance a delicate quadrille of flirtation and intrigue, making this book the most superb comedy" +
                " of manners of Regency England.",
            List.of("Classics", "Fiction", "Romance", "Historical Fiction", "Literature", "Historical", "Audiobook"),
            4.28,
            3944155,
            "https://www.goodreads.com/book/show/1885.Pride_and_Prejudice"
        );

        genresCalculator = new GenresOverlapSimilarityCalculator();

        TextTokenizer mockTokenizer = mock(TextTokenizer.class);
        when(mockTokenizer.tokenize(anyString())).thenReturn(List.of("the", "of", "a", "in", "to", "is", "and"));

        Set<Book> books = Set.of(book1, book2, book3);
        tfidfCalculator = new TFIDFSimilarityCalculator(books, mockTokenizer);

        Map<SimilarityCalculator, Double> calculatorMap = Map.of(
            genresCalculator, 0.5,
            tfidfCalculator, 0.5
        );

        compositeCalculator = new CompositeSimilarityCalculator(calculatorMap);
    }

    @Test
    void testGenresOverlapSimilarityCalculator() {
        double similarity = genresCalculator.calculateSimilarity(book1, book3);
        assertEquals(0.857, similarity, 0.001);
    }

    @Test
    void testTFIDFSimilarityCalculator() {
        double similarity = tfidfCalculator.calculateSimilarity(book1, book2);
        assertTrue(similarity >= 0 && similarity <= 1);
    }

    @Test
    void testCompositeSimilarityCalculator() {
        double similarity = compositeCalculator.calculateSimilarity(book1, book2);
        assertTrue(similarity >= 0 && similarity <= 1);
    }

    @Test
    void testNullBookInGenreCalculator() {
        assertThrows(IllegalArgumentException.class, () -> genresCalculator.calculateSimilarity(book1, null));
    }

    @Test
    void testNullBookInTFIDFCalculator() {
        assertThrows(IllegalArgumentException.class, () -> tfidfCalculator.calculateSimilarity(null, book2));
    }

    @Test
    void testNullBookInCompositeCalculator() {
        assertThrows(IllegalArgumentException.class, () -> compositeCalculator.calculateSimilarity(book1, null));
    }
}
