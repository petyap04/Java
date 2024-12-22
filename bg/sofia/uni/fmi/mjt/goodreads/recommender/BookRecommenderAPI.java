package bg.sofia.uni.fmi.mjt.goodreads.recommender;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;

import java.util.SortedMap;

public interface BookRecommenderAPI {
    SortedMap<Book, Double> recommendBooks(Book originBook, int maxN);
}