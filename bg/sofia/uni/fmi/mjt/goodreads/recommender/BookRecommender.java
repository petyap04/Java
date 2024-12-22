package bg.sofia.uni.fmi.mjt.goodreads.recommender;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class BookRecommender implements BookRecommenderAPI {

    Set<Book> initialBooks;
    SimilarityCalculator calculator;

    public BookRecommender(Set<Book> initialBooks, SimilarityCalculator calculator) {
        if (initialBooks == null || calculator == null) {
            throw new IllegalArgumentException("Can't be null!");
        }
        this.initialBooks = initialBooks;
        this.calculator = calculator;
    }

    /**
     * Searches for books that are similar to the provided one.
     *
     * @param origin the book we should calculate similarity with.
     * @param maxN       the maximum number of entries returned
     * @return a SortedMap<Book, Double> representing the top maxN closest books
     * with their similarity to originBook ordered by their similarity score
     * @throws IllegalArgumentException if the originBook is null.
     * @throws IllegalArgumentException if maxN is smaller or equal to 0.
     */
    @Override
    public SortedMap<Book, Double> recommendBooks(Book origin, int maxN) {
        if (origin == null) {
            throw new IllegalArgumentException("The book can't be null!");
        }
        if (maxN <= 0) {
            throw new IllegalArgumentException("The number has to be bigger than 0");
        }
        SortedMap<Book, Double> result = new TreeMap<>();
        for ()
    }

}