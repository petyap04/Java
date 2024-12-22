package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.genres;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;

import java.util.HashSet;
import java.util.Set;

public class GenresOverlapSimilarityCalculator implements SimilarityCalculator {

    @Override
    public double calculateSimilarity(Book first, Book second) {

        if (first == null || second == null) {
            throw new IllegalArgumentException("The books can't be null!");
        }

        Set<String> genres = new HashSet<>(first.genres());
        int intersection = 0;
        for (String curr : second.genres()) {
            if (genres.contains(curr)) {
                intersection++;
            }
        }
        return (double) Math.abs(intersection) /
                Math.min(Math.abs(first.genres().size()), Math.abs(second.genres().size()));
    }

}