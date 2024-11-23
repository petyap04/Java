package bg.sofia.uni.fmi.mjt.olimpics;

import bg.sofia.uni.fmi.mjt.olympics.competition.Competition;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Athlete;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Competitor;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class CompetitionTest {

    @Test
    void testCompetitionConstructorThrowsExceptionForNullName() {
        Set<Competitor> competitors = Set.of(new Athlete("1", "John Doe", "USA"));

        assertThrows(IllegalArgumentException.class, () -> new Competition(null, "Running", competitors),
                "Competition name cannot be null or blank");
    }

    @Test
    void testCompetitionConstructorThrowsExceptionForEmptyCompetitors() {
        assertThrows(IllegalArgumentException.class, () -> new Competition("Olympics", "Running", Set.of()),
                "Competition must have at least one competitor");
    }
}
