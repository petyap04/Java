package bg.sofia.uni.fmi.mjt.olimpics;

import bg.sofia.uni.fmi.mjt.olympics.MJTOlympics;
import bg.sofia.uni.fmi.mjt.olympics.competition.Competition;
import bg.sofia.uni.fmi.mjt.olympics.competition.CompetitionResultFetcher;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Athlete;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Competitor;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Medal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MJTOlympicsTest {

    private MJTOlympics mjtOlympics;
    private CompetitionResultFetcher mockResultFetcher;
    private Set<Competitor> registeredCompetitors;

    @BeforeEach
    void setUp() {
        mockResultFetcher = mock(CompetitionResultFetcher.class);

        Competitor athlete1 = new Athlete("1", "John Doe", "USA");
        Competitor athlete2 = new Athlete("2", "Jane Smith", "Canada");
        Competitor athlete3 = new Athlete("3", "Max Brown", "UK");

        registeredCompetitors = new HashSet<>(Arrays.asList(athlete1, athlete2, athlete3));
        mjtOlympics = new MJTOlympics(registeredCompetitors, mockResultFetcher);
    }

    @Test
    void testUpdateMedalStatisticsAssignsMedalsCorrectly() {
        Competitor athlete1 = registeredCompetitors.iterator().next();
        TreeSet<Competitor> competitionRanking = new TreeSet<>(Comparator.comparing(Competitor::getName));
        competitionRanking.add(athlete1);

        Competition mockCompetition = mock(Competition.class);
        when(mockCompetition.competitors()).thenReturn(registeredCompetitors);
        when(mockResultFetcher.getResult(mockCompetition)).thenReturn(competitionRanking);
        mjtOlympics.updateMedalStatistics(mockCompetition);
        assertEquals(1, athlete1.getMedals().size(), "Athlete should receive one medal");
        assertTrue(athlete1.getMedals().contains(Medal.GOLD), "Athlete should receive a GOLD medal");
    }

    @Test
    void testUpdateMedalStatisticsThrowsExceptionForUnregisteredCompetitors() {
        Competitor unregisteredAthlete = new Athlete("4", "Unknown", "Germany");
        Competition mockCompetition = mock(Competition.class);
        when(mockCompetition.competitors()).thenReturn(Set.of(unregisteredAthlete));
        assertThrows(IllegalArgumentException.class, () -> mjtOlympics.updateMedalStatistics(mockCompetition),
                "Should throw an exception if competitors are not registered");
    }

    @Test
    void testGetTotalMedalsReturnsCorrectCount() {
        Competitor athlete = new Athlete("1", "John Doe", "USA");
        athlete.addMedal(Medal.GOLD);
        athlete.addMedal(Medal.SILVER);
        mjtOlympics.getNationsMedalTable()
                .put("USA", new EnumMap<>(Map.of(Medal.GOLD, 1, Medal.SILVER, 1)));
        int totalMedals = mjtOlympics.getTotalMedals("USA");
        assertEquals(2, totalMedals, "Total medals count should be correct");
    }

    @Test
    void testGetTotalMedalsThrowsExceptionForInvalidNationality() {
        String invalidNationality = "Germany";
        assertThrows(IllegalArgumentException.class, () -> mjtOlympics.getTotalMedals(invalidNationality),
                "Should throw exception for non-existent nationality");
    }

    @Test
    void testGetNationsRankListOrdersCorrectly() {
        mjtOlympics.getNationsMedalTable()
                .put("USA", new EnumMap<>(Map.of(Medal.GOLD, 3, Medal.SILVER, 2)));
        mjtOlympics.getNationsMedalTable()
                .put("Canada", new EnumMap<>(Map.of(Medal.GOLD, 2, Medal.BRONZE, 1)));
        mjtOlympics.getNationsMedalTable()
                .put("UK", new EnumMap<>(Map.of(Medal.SILVER, 1)));
        TreeSet<String> rankList = mjtOlympics.getNationsRankList();
        Iterator<String> iterator = rankList.iterator();
        assertEquals("USA", iterator.next(), "USA should rank first");
        assertEquals("Canada", iterator.next(), "Canada should rank second");
        assertEquals("UK", iterator.next(), "UK should rank third");
    }

    @Test
    void testGetRegisteredCompetitorsReturnsAllRegistered() {
        Set<Competitor> competitors = mjtOlympics.getRegisteredCompetitors();
        assertEquals(registeredCompetitors, competitors, "Returned competitors should match the registered ones");
    }

    @Test
    void testUpdateMedalStatisticsWithEmptyCompetitionDoesNothing() {
        Competition mockCompetition = mock(Competition.class);
        when(mockCompetition.competitors()).thenReturn(Set.of());
        when(mockResultFetcher.getResult(mockCompetition)).thenReturn(new TreeSet<>());
        mjtOlympics.updateMedalStatistics(mockCompetition);
        assertTrue(mjtOlympics.getNationsMedalTable().isEmpty(), "No updates should be made for an empty competition");
    }

    @Test
    void testUpdateMedalTableUpdatesMedalsCorrectly() {
        Competitor athlete = new Athlete("1", "John Doe", "USA");
        mjtOlympics.getNationsMedalTable()
                .put("USA", new EnumMap<>(Map.of(Medal.GOLD, 1, Medal.SILVER, 0)));
        mjtOlympics.updateMedalTable(athlete, Medal.SILVER);
        EnumMap<Medal, Integer> usaMedals = mjtOlympics.getNationsMedalTable().get("USA");
        assertEquals(1, usaMedals.get(Medal.SILVER), "USA should have 1 silver medal");
    }
}
