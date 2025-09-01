
package org.p005_qa;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.p004_qa.IHighestNumberFinder;

import java.util.ArrayList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author selvy
 */
public class TopicManagerTest {

    /**
     * Requirement: The system must handle an empty list of topics gracefully.
     * This test ensures that when the TopicManager is given an empty list of TopicScores,
     * it returns an empty list of TopicTopScores, as there are no scores to process.
     */
    @Test
    void findHighestScoreInEmptyArrayReturnEmptyArray() {
        // Arrange
        ArrayList<TopicScores> topicScores = new ArrayList<>();
        TopicManager cut = new TopicManager();
        ArrayList<TopicTopScore> expectedResult = new ArrayList<>();

        // Act
        ArrayList<TopicTopScore> result = cut.findTopicHighScores(topicScores);

        // Assert
        Assertions.assertEquals(expectedResult, result);
    }

    /**
     * Requirement: The system must be able to find the highest score for a single topic.
     * This test verifies that for a list containing just one TopicScores object,
     * the system correctly identifies the highest score within that topic and returns a list
     * with a single TopicTopScore object reflecting that result.
     */
    @Test
    void findHighestScoreWithArrayOfOneReturnArrayOfOne() {
        // Arrange
        int[] scores = {56, 67, 45, 89};
        String topicName = "Physics";
        ArrayList<TopicScores> topicScores = new ArrayList<>();
        topicScores.add(new TopicScores(topicName, scores));

        IHighestNumberFinder hnf = new HighestNumberFinder();
        TopicManager cut = new TopicManager(hnf);
        ArrayList<TopicTopScore> expectedResult = new ArrayList<>();
        expectedResult.add(new TopicTopScore(topicName, 89));

        // Act
        ArrayList<TopicTopScore> result = cut.findTopicHighScores(topicScores);

        // Assert
        Assertions.assertEquals(expectedResult.get(0).getTopicName(), result.get(0).getTopicName());
        Assertions.assertEquals(expectedResult.get(0).getTopScore(), result.get(0).getTopScore());
    }

    /**
     * Requirement: The system must find the highest score for a single topic.
     * This test is similar to the previous one, but explicitly demonstrates the use of a stubbed
     * dependency (HighestNumberFinder). This helps to isolate the test to the logic of the TopicManager
     * and not the behavior of the HighestNumberFinder. In this specific case, the stub is a concrete
     * implementation, but the principle is the same.
     */
    @Test
    void findHighestScoreWithArrayOfOneReturnArrayOfOneUsingStub() {
        // Arrange
        int[] scores = {56, 67, 45, 89};
        String topicName = "Physics";
        ArrayList<TopicScores> topicScores = new ArrayList<>();
        topicScores.add(new TopicScores(topicName, scores));

        // Use a stub version of HighestNumberFinder
        HighestNumberFinder hnf =
                new HighestNumberFinder();
        TopicManager cut = new TopicManager(hnf);
        ArrayList<TopicTopScore> expectedResult = new ArrayList<>();
        expectedResult.add(new TopicTopScore(topicName, 89));

        // Act
        ArrayList<TopicTopScore> result = cut.findTopicHighScores(topicScores);

        // Assert
        Assertions.assertEquals(expectedResult.get(0).getTopicName(), result.get(0).getTopicName());
        Assertions.assertEquals(expectedResult.get(0).getTopScore(), result.get(0).getTopScore());
    }

    /**
     * Requirement: The system must be able to handle multiple topics and find the highest score for each one.
     * This test uses a concrete stub of HighestNumberFinder to demonstrate the TopicManager's ability
     * to iterate through a list of topics, delegate the score finding for each, and correctly aggregate
     * the results into a new list of TopicTopScores.
     */
    @Test
    void findHighestScoreWithArrayOfManyReturnArrayOfManyUsingStub() {
        // Arrange
        int[] physics_scores = {56, 67, 45, 89};
        String physics = "Physics";
        int[] art_scores = {87, 66, 78};
        String art = "Art";
        int[] compSci_scores = {45, 88, 97, 56};
        String compSci = "Comp Sci";
        ArrayList<TopicScores> topicScores = new ArrayList<>();
        topicScores.add(new TopicScores(physics, physics_scores));
        topicScores.add(new TopicScores(art, art_scores));
        topicScores.add(new TopicScores(compSci, compSci_scores));
        // Use a stub version of HighestNumberFinder
        IHighestNumberFinder hnf =
                new HighestNumberFinder();
        TopicManager cut = new TopicManager(hnf);
        ArrayList<TopicTopScore> expectedResult = new ArrayList<>();
        expectedResult.add(new TopicTopScore(physics, 89));
        expectedResult.add(new TopicTopScore(art, 87));
        expectedResult.add(new TopicTopScore(compSci, 97));

        // Act
        ArrayList<TopicTopScore> result = cut.findTopicHighScores(topicScores);

        // Assert
        Assertions.assertEquals(expectedResult.get(0).getTopicName(), result.get(0).getTopicName());
        Assertions.assertEquals(expectedResult.get(0).getTopScore(), result.get(0).getTopScore());
        Assertions.assertEquals(expectedResult.get(1).getTopicName(), result.get(1).getTopicName());
        Assertions.assertEquals(expectedResult.get(1).getTopScore(), result.get(1).getTopScore());
        Assertions.assertEquals(expectedResult.get(2).getTopicName(), result.get(2).getTopicName());
        Assertions.assertEquals(expectedResult.get(2).getTopScore(), result.get(2).getTopScore());
    }

    /**
     * Requirement: The TopicManager's core logic should be independent of the specific implementation
     * of finding the highest number.
     * This test uses a mock object for the IHighestNumberFinder dependency. By setting up expectations
     * on the mock, we can verify that the TopicManager correctly calls the dependency for each topic
     * and uses the returned values to construct the final result. This isolates the test to the
     * TopicManager's logic, making it a true unit test. The use of 'assertThat' from Hamcrest is replaced
     * with Assertions.assertEquals for consistency with JUnit 5.
     */
    @Test
    void findHighestScoreWithArrayOfManyReturnArrayOfManyUsingMocks() {
        // Arrange
        int[] physics_scores = {56, 67, 45, 89};
        String physics = "Physics";
        int[] art_scores = {87, 66, 78};
        String art = "Art";
        int[] compSci_scores = {45, 88, 97, 56};
        String compSci = "Comp Sci";
        ArrayList<TopicScores> topicScores = new ArrayList<>();
        topicScores.add(new TopicScores(physics, physics_scores));
        topicScores.add(new TopicScores(art, art_scores));
        topicScores.add(new TopicScores(compSci, compSci_scores));

        // Use a mock version of HighestNumberFinder
        IHighestNumberFinder hnf = mock(IHighestNumberFinder.class);
        // Setup the expectations for the mock
        when(hnf.findHighestNumber(physics_scores)).thenReturn(89);
        when(hnf.findHighestNumber(art_scores)).thenReturn(87);
        when(hnf.findHighestNumber(compSci_scores)).thenReturn(97);

        TopicManager cut = new TopicManager(hnf);
        ArrayList<TopicTopScore> expectedResult = new ArrayList<>();
        expectedResult.add(new TopicTopScore(physics, 89));
        expectedResult.add(new TopicTopScore(art, 87));
        expectedResult.add(new TopicTopScore(compSci, 97));

        // Act
        ArrayList<TopicTopScore> result = cut.findTopicHighScores(topicScores);

        // Assert
        Assertions.assertEquals(expectedResult, result);
        // The original test used Hamcrest's assertThat, which is replaced here with JUnit 5's Assertions.
    }
}