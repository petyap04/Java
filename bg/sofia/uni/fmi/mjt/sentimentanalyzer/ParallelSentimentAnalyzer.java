package bg.sofia.uni.fmi.mjt.sentimentanalyzer;

import bg.sofia.uni.fmi.mjt.sentimentanalyzer.exceptions.SentimentAnalysisException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ParallelSentimentAnalyzer implements SentimentAnalyzerAPI {
    private final int workersCount;
    private final Set<String> stopWords;
    private final Map<String, SentimentScore> sentimentLexicon;
    private final BlockingQueue<AnalyzerInput> taskQueue;
    private final Map<String, SentimentScore> results;

    private static final int QUEUE_POLL_TIMEOUT_MS = 100;
    private static final int SENTIMENT_SCORE_MIN = -5;
    private static final int SENTIMENT_SCORE_MAX = 5;
    private static final long AWAIT_TERMINATION_TIMEOUT = Long.MAX_VALUE;

    public ParallelSentimentAnalyzer(int workersCount, Set<String> stopWords,
                                     Map<String, SentimentScore> sentimentLexicon) {
        this.workersCount = workersCount;
        this.stopWords = stopWords;
        this.sentimentLexicon = sentimentLexicon;
        this.taskQueue = new LinkedBlockingQueue<>();
        this.results = new ConcurrentHashMap<>();
    }

    @Override
    public Map<String, SentimentScore> analyze(AnalyzerInput... input) throws SentimentAnalysisException {
        ExecutorService producerExecutor = Executors.newFixedThreadPool(input.length);
        ExecutorService consumerExecutor = Executors.newFixedThreadPool(workersCount);

        submitProducers(input, producerExecutor);
        submitConsumers(consumerExecutor);

        awaitTermination(producerExecutor, consumerExecutor);

        return results;
    }

    private void submitProducers(AnalyzerInput[] input, ExecutorService producerExecutor) {
        for (AnalyzerInput analyzerInput : input) {
            producerExecutor.submit(() -> addToQueue(analyzerInput));
        }
    }

    private void addToQueue(AnalyzerInput analyzerInput) {
        try {
            taskQueue.put(analyzerInput);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new SentimentAnalysisException("Producer thread was interrupted while adding to the queue.", e);
        }
    }

    private void submitConsumers(ExecutorService consumerExecutor) {
        for (int i = 0; i < workersCount; i++) {
            consumerExecutor.submit(this::consumeTasks);
        }
    }

    private void consumeTasks() {
        try {
            while (!Thread.currentThread().isInterrupted() && !taskQueue.isEmpty()) {
                AnalyzerInput task = taskQueue.poll(QUEUE_POLL_TIMEOUT_MS, TimeUnit.MILLISECONDS);
                if (task != null) {
                    processTask(task);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new SentimentAnalysisException("Consumer thread was interrupted during processing.", e);
        }
    }

    private void awaitTermination(ExecutorService producerExecutor, ExecutorService consumerExecutor) {
        producerExecutor.shutdown();
        consumerExecutor.shutdown();

        try {
            producerExecutor.awaitTermination(AWAIT_TERMINATION_TIMEOUT, TimeUnit.MILLISECONDS);
            consumerExecutor.awaitTermination(AWAIT_TERMINATION_TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new SentimentAnalysisException("Execution was interrupted while awaiting termination.", e);
        }
    }

    private void processTask(AnalyzerInput task) throws SentimentAnalysisException {
        String text = readText(task);
        int score = calculateScore(text);
        results.put(task.inputID(), mapScoreToSentiment(score));
    }

    private String readText(AnalyzerInput task) {
        try (BufferedReader reader = new BufferedReader(task.inputReader())) {
            return reader.lines()
                .collect(Collectors.joining(" "))
                .toLowerCase()
                .replaceAll("[^a-zA-Z\\s]", "");
        } catch (IOException e) {
            throw new SentimentAnalysisException("Error occurred while reading the input for task: " + task.inputID(),
                e);
        }
    }

    private int calculateScore(String text) {
        return Arrays.stream(text.split("\\s+"))
            .filter(word -> !stopWords.contains(word))
            .mapToInt(word -> sentimentLexicon.getOrDefault(word, SentimentScore.NEUTRAL).getScore())
            .sum();
    }

    private SentimentScore mapScoreToSentiment(int score) {
        return SentimentScore.fromScore(Math.max(SENTIMENT_SCORE_MIN, Math.min(SENTIMENT_SCORE_MAX, score)));
    }
}
