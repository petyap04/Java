package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.composite;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;

import java.util.Map;

public class CompositeSimilarityCalculator implements SimilarityCalculator {

    private final Map<SimilarityCalculator, Double> similarityCalculatorMap;
    public CompositeSimilarityCalculator(Map<SimilarityCalculator, Double> similarityCalculatorMap) {
        if (similarityCalculatorMap == null) {
            throw new IllegalArgumentException("SimilarityCalculatorMap can't be null!");
        }
        this.similarityCalculatorMap = similarityCalculatorMap;
    }

    @Override
    public double calculateSimilarity(Book first, Book second) {
        if (first == null || second == null) {
            throw new IllegalArgumentException("The books can't be null!");
        }
        double result = 0.0;
        for (SimilarityCalculator curr : similarityCalculatorMap.keySet()) {
            result += curr.calculateSimilarity(first, second) * similarityCalculatorMap.get(curr);
        }
        return result;
    }

}