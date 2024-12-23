package bg.sofia.uni.fmi.mjt.goodreads.tokenizer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TextTokenizerTest {

    private TextTokenizer tokenizer;

    private static final String STOPWORDS = "the\nis\nin\nand\nof\non\nfor\nto\nwith\n";

    private static final String BOOK_DESCRIPTION_1 = "The unforgettable novel of a childhood in a sleepy" +
        " Southern town and the crisis of conscience that rocked it. \"To Kill A Mockingbird\" became both " +
        "an instant bestseller and a critical success when it was first published in 1960. It went on to win " +
        "the Pulitzer Prize in 1961 and was later made into an Academy Award-winning film, also a classic. " +
        "Compassionate, dramatic, and deeply moving, \"To Kill A Mockingbird\" takes readers to the roots of " +
        "human behavior - to innocence and experience, kindness and cruelty, love and hatred, humor and pathos.";
    private static final String BOOK_DESCRIPTION_2 = "Harry Potter thinks he is an ordinary boy - until he is " +
        "rescued by an owl, taken to Hogwarts School of Witchcraft and Wizardry, learns to play Quidditch and does " +
        "battle in a deadly duel. The Reason ... HARRY POTTER IS A WIZARD!";

    @BeforeEach
    void setUp() {
        StringReader stopwordsReader = new StringReader(STOPWORDS);
        tokenizer = new TextTokenizer(stopwordsReader);
    }

    @Test
    void testStopwordsLoadedCorrectly() {
        Set<String> stopwords = tokenizer.stopwords();
        assertTrue(stopwords.contains("the"));
        assertTrue(stopwords.contains("is"));
        assertTrue(stopwords.contains("in"));
        assertFalse(stopwords.contains("hello"));
    }

    @Test
    void testTokenizeDescriptionWithStopwords() {
        List<String> tokens = tokenizer.tokenize(BOOK_DESCRIPTION_1);
        assertFalse(tokens.contains("the"));
        assertFalse(tokens.contains("and"));
        assertFalse(tokens.contains("in"));
        assertTrue(tokens.contains("unforgettable"));
        assertTrue(tokens.contains("novel"));
        assertTrue(tokens.contains("childhood"));
        assertTrue(tokens.contains("southern"));
        assertTrue(tokens.contains("town"));
    }

    @Test
    void testTokenizeDescriptionWithPunctuation() {
        List<String> tokens = tokenizer.tokenize(BOOK_DESCRIPTION_2);
        assertTrue(tokens.contains("harry"));
        assertTrue(tokens.contains("potter"));
        assertTrue(tokens.contains("thinks"));
        assertTrue(tokens.contains("ordinary"));
        assertTrue(tokens.contains("boy"));
        assertFalse(tokens.contains("!"));
    }

    @Test
    void testTokenizeStringWithSpecialCharacters() {
        String input = "This is an example sentence with special characters: @, #, $, %.!";
        List<String> tokens = tokenizer.tokenize(input);
        assertTrue(tokens.contains("example"));
        assertTrue(tokens.contains("sentence"));
        assertTrue(tokens.contains("special"));
        assertTrue(tokens.contains("characters"));
        assertFalse(tokens.contains("@"));
        assertFalse(tokens.contains("$"));
    }

    @Test
    void testTokenizeStringWithMultipleSpaces() {
        String input = "    Hello   world    how   are   you?   ";
        List<String> tokens = tokenizer.tokenize(input);
        assertEquals(5, tokens.size());
        assertTrue(tokens.contains("hello"));
        assertTrue(tokens.contains("world"));
        assertTrue(tokens.contains("how"));
        assertTrue(tokens.contains("are"));
        assertTrue(tokens.contains("you"));
    }

    @Test
    void testTokenizeEmptyString() {
        String input = "";
        List<String> tokens = tokenizer.tokenize(input);
        assertTrue(tokens.isEmpty());
    }

    @Test
    void testTokenizeStringWithOnlyStopwords() {
        String input = "the is in and of";
        List<String> tokens = tokenizer.tokenize(input);
        assertTrue(tokens.isEmpty());
    }

    @Test
    void testTokenizeStringWithCaseSensitivity() {
        String input = "To Kill A Mockingbird is a classic!";
        List<String> tokens = tokenizer.tokenize(input);
        assertTrue(tokens.contains("kill"));
        assertTrue(tokens.contains("mockingbird"));
        assertTrue(tokens.contains("classic"));
        assertFalse(tokens.contains("is"));
    }

    @Test
    void testTokenizeMultipleDescriptions() {
        List<String> tokens1 = tokenizer.tokenize(BOOK_DESCRIPTION_1);
        List<String> tokens2 = tokenizer.tokenize(BOOK_DESCRIPTION_2);
        assertTrue(tokens1.contains("novel"));
        assertTrue(tokens2.contains("wizard"));
        assertTrue(tokens2.contains("duel"));
    }

    @Test
    void testTokenizeStringWithExcessivePunctuation() {
        String input = "!!!, ... !!! How does this work?!!";
        List<String> tokens = tokenizer.tokenize(input);
        assertTrue(tokens.contains("how"));
        assertTrue(tokens.contains("does"));
        assertTrue(tokens.contains("work"));
        assertFalse(tokens.contains("!!!"));
    }
}
