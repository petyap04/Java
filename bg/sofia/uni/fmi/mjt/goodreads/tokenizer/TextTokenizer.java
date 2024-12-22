package bg.sofia.uni.fmi.mjt.goodreads.tokenizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TextTokenizer {

    private final Set<String> stopwords;

    public TextTokenizer(Reader stopwordsReader) {
        try (var br = new BufferedReader(stopwordsReader)) {
            stopwords = br.lines().collect(Collectors.toSet());
        } catch (IOException ex) {
            throw new IllegalArgumentException("Could not load dataset", ex);
        }
    }

    public List<String> tokenize(String input) {
        String cleanedInput = input
            .replaceAll("\\p{Punct}", "")
            .replaceAll("\\s+", " ")
            .toLowerCase();

        String[] words = cleanedInput.split("\\s+");
        List<String> result = new ArrayList<>();
        for (String word : words) {
            if (!stopwords.contains(word)) {
                result.add(word);
            }
        }
        return result;
    }

    public Set<String> stopwords() {
        return stopwords;
    }
}
