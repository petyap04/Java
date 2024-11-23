package bg.sofia.uni.fmi.mjt.olimpics;

import bg.sofia.uni.fmi.mjt.olympics.competitor.Athlete;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Medal;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AthleteTest {

    @Test
    void testAddMedalAddsMedalToAthlete() {
        Athlete athlete = new Athlete("1", "John Doe", "USA");
        athlete.addMedal(Medal.GOLD);

        assertTrue(athlete.getMedals().contains(Medal.GOLD), "Medal should be added to the athlete's collection");
    }

    @Test
    void testAddMedalThrowsExceptionForNullMedal() {
        Athlete athlete = new Athlete("1", "John Doe", "USA");

        assertThrows(IllegalArgumentException.class, () -> athlete.addMedal(null),
                "Adding a null medal should throw an exception");
    }

    @Test
    void testGetMedalsReturnsUnmodifiableCollection() {
        Athlete athlete = new Athlete("1", "John Doe", "USA");
        athlete.addMedal(Medal.GOLD);

        assertThrows(UnsupportedOperationException.class, () -> athlete.getMedals().add(Medal.SILVER),
                "The medals collection should be unmodifiable");
    }
}
