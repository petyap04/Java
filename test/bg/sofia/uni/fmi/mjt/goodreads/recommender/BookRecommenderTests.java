package bg.sofia.uni.fmi.mjt.goodreads.recommender;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BookRecommenderTests {

    private BookRecommender recommender;
    private SimilarityCalculator mockCalculator;

    private Book book1;
    private Book book2;
    private Book book3;
    private Book book4;

    @BeforeEach
    void setUp() {
        book1 = new Book("0", "To Kill a Mockingbird", "Harper Lee",
            "The unforgettable novel of a childhood in a sleepy Southern town and the crisis of conscience " +
                "that rocked it. \"\"To Kill A Mockingbird\"\" became both an instant bestseller and a critical " +
                "success when it was first published in 1960. It went on to win the Pulitzer Prize in 1961 and" +
                " was later made into an Academy Award-winning film, also a classic.Compassionate, dramatic, " +
                "and deeply moving, \"\"To Kill A Mockingbird\"\" takes readers to the roots of human behavior - " +
                "to innocence and experience, kindness and cruelty, love and hatred, humor and pathos. Now with" +
                " over 18 million copies in print and translated into forty languages, this regional story by a " +
                "young Alabama woman claims universal appeal. Harper Lee always considered her book to be a simple" +
                " love story. Today it is regarded as a masterpiece of American literature.",
            List.of("Classics", "Fiction", "Historical Fiction"), 4.27, 5691311,
            "https://www.goodreads.com/book/show/2657.To_Kill_a_Mockingbird");

        book2 = new Book("1", "Harry Potter and the Philosopher’s Stone", "J.K. Rowling",
            "Harry Potter thinks he is an ordinary boy - until he is rescued by an owl, " +
                "taken to Hogwarts School of Witchcraft and Wizardry, learns to play Quidditch and does battle " +
                "in a deadly duel. The Reason ... HARRY POTTER IS A WIZARD!", List.of("Fantasy", "Fiction", "Magic"),
            4.47, 9278135, "https://www.goodreads.com/book/show/72193.Harry_Potter_and_the_Philosopher_s_Stone");

        book3 = new Book("2", "Pride and Prejudice", "Jane Austen",
            "Since its immediate success in 1813, Pride and Prejudice has remained one of the most " +
                "popular novels in the English language. Jane Austen called this brilliant work \"\"her own " +
                "darling child\"\" and its vivacious heroine, Elizabeth Bennet, \"\"as delightful a creature " +
                "as ever appeared in print.\"\" The romantic clash between the opinionated Elizabeth and her " +
                "proud beau, Mr. Darcy, is a splendid performance of civilized sparring. And Jane Austen's " +
                "radiant wit sparkles as her characters dance a delicate quadrille of flirtation and intrigue, " +
                "making this book the most superb comedy of manners of Regency England.Alternate cover edition " +
                "of ISBN 9780679783268", List.of("Classics", "Fiction", "Romance"), 4.28, 3944155,
            "https://www.goodreads.com/book/show/1885.Pride_and_Prejudice");

        book4 = new Book("3", "The Diary of a Young Girl", "Anne Frank",
            "Discovered in the attic in which she spent the last years of her life, Anne Frank’s " +
                "remarkable diary has become a world classic—a powerful reminder of the horrors of war and an " +
                "eloquent testament to the human spirit. In 1942, with the Nazis occupying Holland, a " +
                "thirteen-year-old Jewish girl and her family fled their home in Amsterdam and went into hiding. " +
                "For the next two years, until their whereabouts were betrayed to the Gestapo, the Franks and " +
                "another family lived cloistered in the “Secret Annexe” of an old office building. Cut off from " +
                "the outside world, they faced hunger, boredom, the constant cruelties of living in confined quarters, " +
                "and the ever-present threat of discovery and death. In her diary Anne Frank recorded vivid " +
                "impressions of her experiences during this period. By turns thoughtful, moving, and surprisingly" +
                " humorous, her account offers a fascinating commentary on human courage and frailty and a compelling " +
                "self-portrait of a sensitive and spirited young woman whose promise was tragically cut short." +
                "--back cover", List.of("Classics", "Nonfiction", "History"), 4.18, 3488438,
            "https://www.goodreads.com/book/show/48855.The_Diary_of_a_Young_Girl");

        mockCalculator = mock(SimilarityCalculator.class);
        Set<Book> books = Set.of(book1, book2, book3, book4);
        recommender = new BookRecommender(books, mockCalculator);
    }

    @Test
    void testRecommendBooksValidCase() {
        when(mockCalculator.calculateSimilarity(book1, book2)).thenReturn(0.9);
        when(mockCalculator.calculateSimilarity(book1, book3)).thenReturn(0.8);
        when(mockCalculator.calculateSimilarity(book1, book4)).thenReturn(0.7);

        SortedMap<Book, Double> recommendations = recommender.recommendBooks(book1, 2);

        assertEquals(2, recommendations.size());
        assertTrue(recommendations.containsKey(book2));
        assertTrue(recommendations.containsKey(book3));
        assertEquals(0.9, recommendations.get(book2));
        assertEquals(0.8, recommendations.get(book3));
    }

    @Test
    void testRecommendBooksWithMaxNExceedingAvailableBooks() {
        when(mockCalculator.calculateSimilarity(book1, book2)).thenReturn(0.9);
        when(mockCalculator.calculateSimilarity(book1, book3)).thenReturn(0.8);
        when(mockCalculator.calculateSimilarity(book1, book4)).thenReturn(0.7);

        SortedMap<Book, Double> recommendations = recommender.recommendBooks(book1, 5);

        assertEquals(3, recommendations.size());
    }

    @Test
    void testRecommendBooksWithZeroMaxN() {
        assertThrows(IllegalArgumentException.class, () -> recommender.recommendBooks(book1, 0));
    }

    @Test
    void testRecommendBooksWithNegativeMaxN() {
        assertThrows(IllegalArgumentException.class, () -> recommender.recommendBooks(book1, -1));
    }

    @Test
    void testRecommendBooksWithNullBook() {
        assertThrows(IllegalArgumentException.class, () -> recommender.recommendBooks(null, 2));
    }

    @Test
    void testConstructorWithNullBooks() {
        assertThrows(IllegalArgumentException.class, () -> new BookRecommender(null, mockCalculator));
    }

    @Test
    void testCalculateSimilarityMap() {
        when(mockCalculator.calculateSimilarity(book1, book2)).thenReturn(0.9);
        when(mockCalculator.calculateSimilarity(book1, book3)).thenReturn(0.8);
        when(mockCalculator.calculateSimilarity(book1, book4)).thenReturn(0.7);

        Map<Book, Double> similarityMap = recommender.calculateSimilarityMap(book1);

        assertEquals(3, similarityMap.size());
        assertEquals(0.9, similarityMap.get(book2));
        assertEquals(0.8, similarityMap.get(book3));
        assertEquals(0.7, similarityMap.get(book4));
    }

    @Test
    void testGetTopRecommendationsOrderingBySimilarityThenTitle() {
        when(mockCalculator.calculateSimilarity(book1, book2)).thenReturn(0.9);
        when(mockCalculator.calculateSimilarity(book1, book3)).thenReturn(0.9);
        when(mockCalculator.calculateSimilarity(book1, book4)).thenReturn(0.8);

        SortedMap<Book, Double> recommendations = recommender.recommendBooks(book1, 3);

        Iterator<Book> iterator = recommendations.keySet().iterator();
        assertEquals(book2, iterator.next());
        assertEquals(book3, iterator.next());
        assertEquals(book4, iterator.next());
    }

    @Test
    void testSimilarityWithEdgeCases() {
        when(mockCalculator.calculateSimilarity(book1, book2)).thenReturn(0.9);
        when(mockCalculator.calculateSimilarity(book1, book3)).thenReturn(0.9);
        when(mockCalculator.calculateSimilarity(book1, book4)).thenReturn(0.0);

        SortedMap<Book, Double> recommendations = recommender.recommendBooks(book1, 3);

        Iterator<Book> iterator = recommendations.keySet().iterator();
        assertEquals(book2, iterator.next());
        assertEquals(book3, iterator.next());
        assertEquals(book4, iterator.next());
    }

    @Test
    void testGetTopRecommendationsWithNoResults() {
        when(mockCalculator.calculateSimilarity(book1, book2)).thenReturn(0.0);
        when(mockCalculator.calculateSimilarity(book1, book3)).thenReturn(0.0);
        when(mockCalculator.calculateSimilarity(book1, book4)).thenReturn(0.0);

        SortedMap<Book, Double> recommendations = recommender.recommendBooks(book1, 3);

        assertTrue(recommendations.isEmpty());
    }

    @Test
    void testSimilarityMapWithAllBooksBeingSimilar() {
        when(mockCalculator.calculateSimilarity(book1, book2)).thenReturn(0.9);
        when(mockCalculator.calculateSimilarity(book1, book3)).thenReturn(0.9);
        when(mockCalculator.calculateSimilarity(book1, book4)).thenReturn(0.9);

        SortedMap<Book, Double> recommendations = recommender.recommendBooks(book1, 3);

        Iterator<Book> iterator = recommendations.keySet().iterator();
        assertEquals(book2, iterator.next());
        assertEquals(book3, iterator.next());
        assertEquals(book4, iterator.next());
    }
}