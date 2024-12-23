package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.descriptions;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;
import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TFIDFSimilarityCalculator implements SimilarityCalculator {
    private final Set<Book> books;
    private final TextTokenizer tokenizer;

    public TFIDFSimilarityCalculator(Set<Book> books, TextTokenizer tokenizer) {
        if (books == null || tokenizer == null) {
            throw new IllegalArgumentException("The arguments can't be null!");
        }
        this.books = books;
        this.tokenizer = tokenizer;
    }

    @Override
    public double calculateSimilarity(Book first, Book second) {
        Map<String, Double> tfIdfScoresFirst = computeTFIDF(first);
        Map<String, Double> tfIdfScoresSecond = computeTFIDF(second);

        return cosineSimilarity(tfIdfScoresFirst, tfIdfScoresSecond);
    }

    public Map<String, Double> computeTFIDF(Book book) {
        Map<String, Double> tfScores = computeTF(book);
        Map<String, Double> idfScores = computeIDF();

        Map<String, Double> tfIdfScores = new HashMap<>();
        for (Map.Entry<String, Double> entry : tfScores.entrySet()) {
            String word = entry.getKey();
            Double tfScore = entry.getValue();
            Double idfScore = idfScores.get(word);
            tfIdfScores.put(word, tfScore * idfScore);
        }

        return tfIdfScores;
    }

    public Map<String, Double> computeTF(Book book) {
        String description = book.description();
        List<String> words = tokenizer.tokenize(description);

        Map<String, Integer> wordCount = new HashMap<>();
        for (String word : words) {
            wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
        }

        Map<String, Double> tfScores = new HashMap<>();
        int totalWords = words.size();
        for (Map.Entry<String, Integer> entry : wordCount.entrySet()) {
            tfScores.put(entry.getKey(), (double) entry.getValue() / totalWords);
        }

        return tfScores;
    }

    public Map<String, Double> computeIDF() {
        Map<String, Double> idfScores = new HashMap<>();
        int totalBooks = books.size();

        Map<String, Integer> wordDocCount = new HashMap<>();
        for (Book book : books) {
            Set<String> uniqueWordsInBook = new HashSet<>(tokenizer.tokenize(book.description()));
            for (String word : uniqueWordsInBook) {
                wordDocCount.put(word, wordDocCount.getOrDefault(word, 0) + 1);
            }
        }

        for (Map.Entry<String, Integer> entry : wordDocCount.entrySet()) {
            String word = entry.getKey();
            int docCount = entry.getValue();
            double idf = Math.log10((double) totalBooks / docCount);
            idfScores.put(word, idf);
        }

        return idfScores;
    }

    private double cosineSimilarity(Map<String, Double> first, Map<String, Double> second) {
        double magnitudeFirst = magnitude(first.values());
        double magnitudeSecond = magnitude(second.values());

        return dotProduct(first, second) / (magnitudeFirst * magnitudeSecond);
    }

    private double dotProduct(Map<String, Double> first, Map<String, Double> second) {
        Set<String> commonKeys = new HashSet<>(first.keySet());
        commonKeys.retainAll(second.keySet());

        return commonKeys.stream()
            .mapToDouble(word -> first.get(word) * second.get(word))
            .sum();
    }

    private double magnitude(Collection<Double> input) {
        double squaredMagnitude = input.stream()
            .map(v -> v * v)
            .reduce(0.0, Double::sum);

        return Math.sqrt(squaredMagnitude);
    }
}