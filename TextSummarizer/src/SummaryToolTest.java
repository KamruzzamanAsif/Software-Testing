import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Collections;

class SummaryToolTest {
    private SummaryTool summaryTool;

    @BeforeEach
    void setUp() {
        summaryTool = new SummaryTool();
        summaryTool.init();
    }

    /********************  GroupSentencesIntoParagraphs() ***********************/
    @Test
    @DisplayName("Empty Sentence List. Should return a paragraph which is empty.")
    void testGroupSentencesIntoParagraphs_EmptySentencesList() {
        // Test when sentences list is empty
        // Call the method with an empty sentences list
        summaryTool.groupSentencesIntoParagraphs();

        // Assert that one empty paragraphs were created
        assertTrue(summaryTool.paragraphs.size() == 1 &&
                summaryTool.paragraphs.get(0).sentences.isEmpty());
    }

    @Test
    @DisplayName("Every Sentence will belong to one single paragraph.")
    void testGroupSentencesIntoParagraphs_SingleParagraph() {
        // Test when all sentences belong to the same paragraph
        // Create sentences with the same paragraph number
        Sentence sentence1 = new Sentence(1, "Sentence 1", 0, 0);
        Sentence sentence2 = new Sentence(2, "Sentence 2", 0, 0);
        Sentence sentence3 = new Sentence(3, "Sentence 3", 0, 0);

        // Add sentences to the SummaryTool instance
        summaryTool.sentences = new ArrayList<>();
        summaryTool.sentences.add(sentence1);
        summaryTool.sentences.add(sentence2);
        summaryTool.sentences.add(sentence3);

        // Update the noOfSentences
        summaryTool.noOfSentences = 3;

        // Call the method
        summaryTool.groupSentencesIntoParagraphs();

        // Assert that only one paragraph was created
        assertEquals(1, summaryTool.paragraphs.size());
        // Assert that all sentences are in the same paragraph
        assertEquals(3, summaryTool.paragraphs.get(0).sentences.size());
    }

    @Test
    @DisplayName("Sentences will belong to different paragraphs.")
    void testGroupSentencesIntoParagraphs_MultipleParagraphs() {
        // Test when sentences belong to different paragraphs
        // Create sentences with different paragraph numbers
        Sentence sentence1 = new Sentence(1, "Sentence 1", 0, 0);
        Sentence sentence2 = new Sentence(2, "Sentence 2", 0, 1);
        Sentence sentence3 = new Sentence(3, "Sentence 3", 0, 1);
        Sentence sentence4 = new Sentence(4, "Sentence 4", 0, 2);

        // Add sentences to the SummaryTool instance
        summaryTool.sentences = new ArrayList<>();
        summaryTool.sentences.add(sentence1);
        summaryTool.sentences.add(sentence2);
        summaryTool.sentences.add(sentence3);
        summaryTool.sentences.add(sentence4);

        // Update the noOfSentences
        summaryTool.noOfSentences = 4;

        // Call the method
        summaryTool.groupSentencesIntoParagraphs();

        // Assert the correct number of paragraphs and sentences in each paragraph
        assertEquals(3, summaryTool.paragraphs.size());
        assertEquals(1, summaryTool.paragraphs.get(0).sentences.size());
        assertEquals(2, summaryTool.paragraphs.get(1).sentences.size());
        assertEquals(1, summaryTool.paragraphs.get(2).sentences.size());
    }


    /********************  noOfCommonWords() ***********************/
    @Test
    @DisplayName("No words in both sentences. Should Return common words = 0")
    void testNoOfCommonWords_NoWordsInBothSentences() {
        // Test when there are no words in both sentences
        Sentence sentence1 = new Sentence(1, " ", 0, 0);
        Sentence sentence2 = new Sentence(2, " ", 0, 0);

        double commonCount = summaryTool.noOfCommonWords(sentence1, sentence2);

        // Assert that commonCount is 0
        assertEquals(0.0, commonCount);
    }

    @Test
    @DisplayName("No words first sentences. Should Return common words = 0")
    void testNoOfCommonWords_WordsInStr1Only() {
        // Test when there are words in str1 but not in str2
        Sentence sentence1 = new Sentence(1, "apple banana", 0, 0);
        Sentence sentence2 = new Sentence(2, " ", 0, 0);

        double commonCount = summaryTool.noOfCommonWords(sentence1, sentence2);

        // Assert that commonCount is 0 because there are no common words
        assertEquals(0.0, commonCount);
    }

    @Test
    @DisplayName("No words in first sentence. Should Return common words = 0")
    void testNoOfCommonWords_WordsInStr2Only() {
        // Test when there are words in str2 but not in str1

        Sentence sentence1 = new Sentence(1, "", 0, 0);
        Sentence sentence2 = new Sentence(2, "apple banana", 0, 0);

        double commonCount = summaryTool.noOfCommonWords(sentence1, sentence2);

        // Assert that commonCount is 0 because there are no common words
        assertEquals(0.0, commonCount);
    }

    @Test
    @DisplayName("some words in both sentences. Should Return common words > 0")
    void testNoOfCommonWords_SomeCommonWords() {
        // Test when there are some common words between str1 and str2

        Sentence sentence1 = new Sentence(1, "apple banana", 0, 0);
        Sentence sentence2 = new Sentence(2, "banana cherry", 0, 0);

        double commonCount = summaryTool.noOfCommonWords(sentence1, sentence2);

        // Assert that commonCount is the number of common words (1 in this case)
        assertEquals(1.0, commonCount);
    }

    @Test
    @DisplayName("All common words in both sentences.")
    void testNoOfCommonWords_AllCommonWords() {
        // Test when all words in str1 and str2 are common

        Sentence sentence1 = new Sentence(1, "apple banana cherry", 0, 0);
        Sentence sentence2 = new Sentence(2, "apple banana cherry", 0, 0);

        double commonCount = summaryTool.noOfCommonWords(sentence1, sentence2);

        // Assert that commonCount is the total number of words (3 in this case)
        assertEquals(3.0, commonCount);
    }



    /********************  createIntersectionMatrix() ***********************/
    @Test
    @DisplayName("Testing Method IntersectionMatrix for empty sentence list")
    void testCreateIntersectionMatrix_NoSentences() {
        // Test when there are no sentences
        summaryTool.sentences = new ArrayList<>(); // Empty sentences list
        summaryTool.noOfSentences = 0;

        // Call the method
        summaryTool.createIntersectionMatrix();

        // Assert that intersectionMatrix is also empty (no dimensions)
        assertEquals(0, summaryTool.intersectionMatrix.length);
    }

    @Test
    @DisplayName("Testing Method IntersectionMatrix for one sentence in the list")
    void testCreateIntersectionMatrix_SingleSentence() {
        // Test when there is only one sentence
        Sentence sentence1 = new Sentence(1, "This is a test sentence.", 0, 0);

        summaryTool.sentences = new ArrayList<>();
        summaryTool.sentences.add(sentence1);
        summaryTool.noOfSentences = 1;

        // Call the method
        summaryTool.createIntersectionMatrix();

        // Assert that intersectionMatrix has one dimension
        assertNotNull(summaryTool.intersectionMatrix);
        assertEquals(1, summaryTool.intersectionMatrix.length);
        assertEquals(1, summaryTool.intersectionMatrix[0].length);
    }

    @Test
    @DisplayName("Testing Method IntersectionMatrix for multiple sentence in the list")
    void testCreateIntersectionMatrix_MultipleSentences() {
        // Test when there are multiple sentences

        Sentence sentence1 = new Sentence(1, "This is a test sentence.", 0, 0);
        Sentence sentence2 = new Sentence(2, "Another example sentence.", 0, 0);

        summaryTool.sentences = new ArrayList<>();
        summaryTool.sentences.add(sentence1);
        summaryTool.sentences.add(sentence2);
        summaryTool.noOfSentences = 2;

        // Call the method
        summaryTool.createIntersectionMatrix();

        // Assert that intersectionMatrix has dimensions for two sentences
        assertNotNull(summaryTool.intersectionMatrix);
        assertEquals(2, summaryTool.intersectionMatrix.length);
        assertEquals(2, summaryTool.intersectionMatrix[0].length);
    }


    /********************  createDictionary() ***********************/
    @Test
    @DisplayName("Testing Method CreateDictionary when there are no sentences")
    void testCreateDictionary_NoSentences() {
        // Test when there are no sentences

        summaryTool.sentences = new ArrayList<>(); // Empty sentences list
        summaryTool.noOfSentences = 0;

        // Call the method
        summaryTool.createDictionary();

        // Assert that the dictionary is empty
        assertTrue(summaryTool.dictionary.isEmpty());
    }

    @Test
    @DisplayName("Testing Method CreateDictionary when there are one sentence")
    void testCreateDictionary_SingleSentence() {
        // Test when there is only one sentence

        Sentence sentence1 = new Sentence(1, "This is a test sentence.", 0, 0);

        summaryTool.sentences = new ArrayList<>();
        summaryTool.sentences.add(sentence1);
        summaryTool.noOfSentences = 1;
        summaryTool.intersectionMatrix = new double[][]{{0.0}}; // Dummy intersection matrix

        // Call the method
        summaryTool.createDictionary();

        // Assert that the dictionary contains one entry with the expected score
        assertFalse(summaryTool.dictionary.isEmpty());
        assertEquals(1, summaryTool.dictionary.size());
        assertEquals(0.0, summaryTool.dictionary.get(sentence1));
        assertEquals(0.0, sentence1.score);
    }

    @Test
    @DisplayName("Testing Method CreateDictionary when there are multiple sentences")
    void testCreateDictionary_MultipleSentences() {
        // Test when there are multiple sentences

        Sentence sentence1 = new Sentence(1, "This is a test sentence.", 0, 0);
        Sentence sentence2 = new Sentence(2, "Another example sentence.", 0, 0);

        summaryTool.sentences = new ArrayList<>();
        summaryTool.sentences.add(sentence1);
        summaryTool.sentences.add(sentence2);
        summaryTool.noOfSentences = 2;
        summaryTool.intersectionMatrix = new double[][]{{1.0, 0.0}, {0.0, 1.0}}; // Dummy intersection matrix

        // Call the method
        summaryTool.createDictionary();

        // Assert that the dictionary contains two entries with the expected scores
        assertFalse(summaryTool.dictionary.isEmpty());
        assertEquals(2, summaryTool.dictionary.size());
        assertEquals(1.0, summaryTool.dictionary.get(sentence1));
        assertEquals(1.0, sentence1.score);
        assertEquals(1.0, summaryTool.dictionary.get(sentence2));
        assertEquals(1.0, sentence2.score);
    }


    /********************  createSummary() ***********************/
    @Test
    @DisplayName("Testing Method CreateSummary when there are no paragraphs")
    void testCreateSummary_NoParagraphs() {
        // Test when there are no paragraphs

        summaryTool.paragraphs = new ArrayList<>(); // Empty paragraphs list
        summaryTool.noOfParagraphs = 0;

        // Call the method
        summaryTool.createSummary();

        // Assert that contentSummary is also empty
        assertTrue(summaryTool.contentSummary.isEmpty());
    }

    @Test
    @DisplayName("Testing Method CreateSummary when there are one paragraph with one sentence")
    void testCreateSummary_SingleParagraph() {
        // Test when there is only one paragraph with sentences

        Sentence sentence1 = new Sentence(1, "This is a test sentence.", 0, 0);

        Paragraph paragraph1 = new Paragraph(1);
        paragraph1.sentences = new ArrayList<>();
        paragraph1.sentences.add(sentence1);

        summaryTool.paragraphs = new ArrayList<>();
        summaryTool.paragraphs.add(paragraph1);
        summaryTool.noOfParagraphs = 1;

        // Mock the SentenceComparator to sort sentences in ascending order
        Collections.sort(paragraph1.sentences, new SentenceComparator());

        // Call the method
        summaryTool.createSummary();

        // Assert that contentSummary contains the sentence from the single paragraph
        assertFalse(summaryTool.contentSummary.isEmpty());
        assertEquals(1, summaryTool.contentSummary.size());
        assertEquals(sentence1, summaryTool.contentSummary.get(0));
    }

    @Test
    @DisplayName("Testing Method CreateSummary when there are multiple paragraphs")
    void testCreateSummary_MultipleParagraphs() {
        // Test when there are multiple paragraphs with sentences

        Sentence sentence1 = new Sentence(1, "This is a test sentence.", 0, 0);
        Sentence sentence2 = new Sentence(2, "Another example sentence.", 0, 0);

        Paragraph paragraph1 = new Paragraph(1);
        paragraph1.sentences = new ArrayList<>();
        paragraph1.sentences.add(sentence1);

        Paragraph paragraph2 = new Paragraph(2);
        paragraph2.sentences = new ArrayList<>();
        paragraph2.sentences.add(sentence2);

        summaryTool.paragraphs = new ArrayList<>();
        summaryTool.paragraphs.add(paragraph1);
        summaryTool.paragraphs.add(paragraph2);
        summaryTool.noOfParagraphs = 2;

        // Mock the SentenceComparator to sort sentences in ascending order
        Collections.sort(paragraph1.sentences, new SentenceComparator());
        Collections.sort(paragraph2.sentences, new SentenceComparator());

        // Call the method
        summaryTool.createSummary();

        // Assert that contentSummary contains the sentences from both paragraphs
        assertFalse(summaryTool.contentSummary.isEmpty());
        assertEquals(2, summaryTool.contentSummary.size());
        assertEquals(sentence1, summaryTool.contentSummary.get(0));
        assertEquals(sentence2, summaryTool.contentSummary.get(1));
    }


    /********************  Stream setup ***********************/
    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    @BeforeEach
    void streamSetUp() {
        // Redirect System.out to capture printed content
        System.setOut(new PrintStream(outContent));
    }
    @AfterEach
    void streamTearDown() {
        // Restore original System.out
        System.setOut(originalOut);
    }


    /********************  printSentences() ***********************/
    @Test
    @DisplayName("Testing Method PrintSentences when there are no sentences")
    void testPrintSentences_NoSentences() {
        // Test when there are no sentences

        summaryTool.sentences = new ArrayList<>(); // Empty sentences list

        // Call the method
        summaryTool.printSentences();

        // Assert that nothing is printed (output is empty)
        assertEquals("", outContent.toString());
    }

    @Test
    @DisplayName("Testing Method PrintSentences when there are sentences")
    void testPrintSentences_MultipleSentences() {
        // Test when there are multiple sentences

        Sentence sentence1 = new Sentence(1, "This is a test sentence.", 23, 0);
        Sentence sentence2 = new Sentence(2, "Another example sentence.", 25, 1);

        summaryTool.sentences = new ArrayList<>();
        summaryTool.sentences.add(sentence1);
        summaryTool.sentences.add(sentence2);

        // Call the method
        summaryTool.printSentences();

        // Assert that the expected sentence information for both sentences is printed
        String expectedOutput = "1 => This is a test sentence. => 23 => 5 => 0" + System.lineSeparator() +
                "2 => Another example sentence. => 25 => 3 => 1" + System.lineSeparator();
        assertEquals(expectedOutput, outContent.toString());
    }


    /********************  printIntersectionMatrix() ***********************/
    @Test
    @DisplayName("Testing Method PrintIntersectionMatrix when it is empty")
    void testPrintIntersectionMatrix_EmptyMatrix() {
        // Test when the intersection matrix is empty (no sentences)

        summaryTool.intersectionMatrix = new double[0][0]; // Empty matrix

        // Call the method
        summaryTool.printIntersectionMatrix();

        // Assert that nothing is printed (output is empty)
        assertEquals("", outContent.toString());
    }

    @Test
    @DisplayName("Testing Method PrintIntersectionMatrix when it is not empty")
    void testPrintIntersectionMatrix_MultipleRows() {
        // Test when the intersection matrix has multiple rows and columns (multiple sentences)

        summaryTool.intersectionMatrix = new double[][]{
                {1.0, 0.5, 0.2},
                {0.5, 1.0, 0.3},
                {0.2, 0.3, 1.0}
        };
        summaryTool.noOfSentences = 3;

        // Call the method
        summaryTool.printIntersectionMatrix();

        // Assert that the expected matrix elements are printed
        String expectedOutput = "1.0    0.5    0.2    " + System.lineSeparator() +
                "0.5    1.0    0.3    " + System.lineSeparator() +
                "0.2    0.3    1.0    " + System.lineSeparator();
        assertEquals(expectedOutput, outContent.toString());
    }



    /********************  printDictionary() ***********************/
    @Test
    @DisplayName("Testing Method PrintPrintDicationary when it is empty")
    void testPrintDicationary_EmptyDictionary() {
        // Test when the dictionary is empty

        summaryTool.dictionary = new LinkedHashMap<>(); // Empty dictionary

        // Call the method
        summaryTool.printDicationary();

        // Assert that nothing is printed (output is empty)
        assertEquals("", outContent.toString());
    }

    @Test
    @DisplayName("Testing Method PrintPrintDicationary when it is not empty")
    void testPrintDicationary_MultipleEntries() {
        // Test when there are multiple entries in the dictionary

        Sentence sentence1 = new Sentence(1, "This is a test sentence.", 23, 0);
        Sentence sentence2 = new Sentence(2, "Another example sentence.", 25, 1);

        summaryTool.dictionary = new LinkedHashMap<>();
        summaryTool.dictionary.put(sentence1, 0.75);
        summaryTool.dictionary.put(sentence2, 0.60);

        // Call the method
        summaryTool.printDicationary();

        // Assert that the expected dictionary entries are printed
        String expectedOutput = "This is a test sentence.: 0.75" + System.lineSeparator() +
                "Another example sentence.: 0.6" + System.lineSeparator();
        assertEquals(expectedOutput, outContent.toString());
    }


    /********************  printSummary() ***********************/
    @Test
    @DisplayName("Testing Method PrintPrintSummary when there are no paragraphs")
    void testPrintSummary_NoParagraphs() {
        // Test when there are no paragraphs

        summaryTool.noOfParagraphs = 0;

        // Call the method
        summaryTool.printSummary();

        // Assert that "no of paragraphs = 0" is printed, and there are no content summary sentences
        String expectedOutput = "no of paragraphs = 0" + System.lineSeparator();
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    @DisplayName("Testing Method PrintPrintSummary when there are paragraphs and content summary sentences")
    void testPrintSummary_WithParagraphsAndContentSummary() {
        // Test when there are paragraphs and content summary sentences

        Sentence sentence1 = new Sentence(1, "This is a test sentence.", 23, 0);
        Sentence sentence2 = new Sentence(2, "Another example sentence.", 25, 0);

        summaryTool.noOfParagraphs = 1;
        summaryTool.contentSummary = new ArrayList<>();
        summaryTool.contentSummary.add(sentence1);
        summaryTool.contentSummary.add(sentence2);

        // Call the method
        summaryTool.printSummary();

        // Assert that "no of paragraphs = 1" is printed, and content summary sentences are printed
        String expectedOutput = "no of paragraphs = 1" + System.lineSeparator() +
                "This is a test sentence." + System.lineSeparator() +
                "Another example sentence." + System.lineSeparator();
        assertEquals(expectedOutput, outContent.toString());
    }
}