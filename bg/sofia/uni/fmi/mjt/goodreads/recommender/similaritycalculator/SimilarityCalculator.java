package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;

public interface SimilarityCalculator {

    double calculateSimilarity(Book first, Book second);

}