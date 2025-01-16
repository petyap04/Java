package bg.sofia.uni.fmi.mjt.sentimentanalyzer;

import java.util.Map;

public interface SentimentAnalyzerAPI {

    public Map<String, SentimentScore> analyze(AnalyzerInput... input);

}