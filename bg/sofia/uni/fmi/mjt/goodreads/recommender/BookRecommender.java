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

    Map<Book, Double> calculateSimilarityMap(Book origin) {
        Map<Book, Double> similarityMap = new HashMap<>();
        for (Book book : initialBooks) {
            if (!book.equals(origin)) {
                double similarity = calculator.calculateSimilarity(origin, book);
                similarityMap.put(book, similarity);
            }
        }
        return similarityMap;
    }

    private SortedMap<Book, Double> getTopRecommendations(Map<Book, Double> similarityMap, int maxN) {
        SortedMap<Book, Double> sortedResults = new TreeMap<>((book1, book2) -> {
            int compare = Double.compare(similarityMap.get(book2), similarityMap.get(book1));
            return compare != 0 ? compare : book1.title().compareTo(book2.title());
        });
        sortedResults.putAll(similarityMap);
        SortedMap<Book, Double> finalResults = new TreeMap<>(sortedResults.comparator());
        int count = 0;
        for (Map.Entry<Book, Double> entry : sortedResults.entrySet()) {
            if (count >= maxN) {
                break;
            }
            finalResults.put(entry.getKey(), entry.getValue());
            count++;
        }

        return finalResults;
    }

    @Override
    public SortedMap<Book, Double> recommendBooks(Book origin, int maxN) {
        if (origin == null) {
            throw new IllegalArgumentException("The book can't be null!");
        }
        if (maxN <= 0) {
            throw new IllegalArgumentException("The number has to be bigger than 0");
        }
        Map<Book, Double> similarityMap = calculateSimilarityMap(origin);
        return getTopRecommendations(similarityMap, maxN);
    }
}