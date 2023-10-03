import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SentenceComparatorTest {

    private static SentenceComparator sentenceComparator;
    @BeforeAll
    static void setUp() {
        sentenceComparator = new SentenceComparator();
    }

    @Test
    @DisplayName("Test Compare with Sentence 1 has higher score than Sentence 2")
    void testCompare_Score1HigherThanScore2() {
        Sentence sentence1 = new Sentence(1, "Sentence 1", 0, 0);
        Sentence sentence2 = new Sentence(2, "Sentence 2", 0, 0);
        sentence1.score = 10; // Adjust scores as needed
        sentence2.score = 5;

        int result = sentenceComparator.compare(sentence1, sentence2);

        assertTrue(result < 0, "Expected sentence1 to have a higher score.");
    }

    @Test
    @DisplayName("Test Compare with Sentence 2 has higher score than Sentence 1")
    void testCompare_Score2HigherThanScore1() {
        Sentence sentence1 = new Sentence(1, "Sentence 1", 0, 0);
        Sentence sentence2 = new Sentence(2, "Sentence 2", 0, 0);
        sentence1.score = 5;
        sentence2.score = 10; // Adjust scores as needed

        int result = sentenceComparator.compare(sentence1, sentence2);

        assertTrue(result > 0, "Expected sentence2 to have a higher score.");
    }

    @Test
    @DisplayName("Test Compare with Sentence 1 has equal score as Sentence 2")
    void testCompare_ScoresEqual() {
        Sentence sentence1 = new Sentence(1, "Sentence 1", 0, 0);
        Sentence sentence2 = new Sentence(2, "Sentence 2", 0, 0);
        sentence1.score = 8; // Adjust scores as needed
        sentence2.score = 8;

        int result = sentenceComparator.compare(sentence1, sentence2);

        assertEquals(0, result, "Expected equal scores to return 0.");
    }
}
