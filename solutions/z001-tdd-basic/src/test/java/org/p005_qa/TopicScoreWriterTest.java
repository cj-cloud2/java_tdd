/*
 * OpenSource
 *
 *
 */
package org.p005_qa;


import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

/**
 *
 * @author selvy
 */
public class TopicScoreWriterTest {

   /**
    * Requirement: The system must write the details of a single topic's top score to the output.
    * This test ensures that when the TopicScoreWriter is given a list with one TopicTopScore object,
    * it correctly formats the topic name and score into a string and calls the `writeLine` method
    * on the dependency (IFileWriter) exactly once with that specific formatted string.
    */
   @Test
   void verifyTopicScoreDetailsWrittenOutOnce() {
      // arrange
      String physics = "Physics";
      String expectedResult = "Physics, 89";

      ArrayList<TopicTopScore> topTopScores = new ArrayList<>();
      topTopScores.add(new TopicTopScore(physics, 89));

      IFileWriter fileWriter = mock(IFileWriter.class);

      TopicScoreWriter cut = new TopicScoreWriter(fileWriter);

      // act
      cut.writeScores(topTopScores);

      // assert
      verify(fileWriter, times(1)).writeLine(expectedResult);
   }

   /**
    * Requirement: The system should not write anything if the list of topic scores is empty.
    * This test verifies that if an empty list of TopicTopScores is provided, the `writeLine`
    * method of the IFileWriter dependency is never called. This prevents the creation of
    * unnecessary output or errors when there is no data to write.
    */
   @Test
   void verifyTopicScoreDetailsNotWritten() {
      // arrange
      ArrayList<TopicTopScore> topTopScores = new ArrayList<>();

      IFileWriter fileWriter = mock(IFileWriter.class);

      TopicScoreWriter cut = new TopicScoreWriter(fileWriter);

      // act
      cut.writeScores(topTopScores);

      // assert
      verify(fileWriter, times(0)).writeLine(Mockito.any());
   }

   /**
    * Requirement: The system must be able to handle multiple topics and write each one to a new line.
    * This test confirms that for a list containing multiple TopicTopScore objects, the `writeScores`
    * method iterates through the list, formats each score's details, and calls the `writeLine` method
    * on the IFileWriter dependency once for each topic with the correct formatted string.
    */
   @Test
   void verifyTopicScoreDetailsWrittenOutMultipleTimes() {
      // arrange
      String physics = "Physics";
      String art = "Art";
      String compSci = "Comp Sci";
      String physicsResult = "Physics, 89";
      String artsResult = "Art, 87";
      String comSciResult = "Comp Sci, 97";

      ArrayList<TopicTopScore> topTopScores = new ArrayList<>();
      topTopScores.add(new TopicTopScore(physics, 89));
      topTopScores.add(new TopicTopScore(art, 87));
      topTopScores.add(new TopicTopScore(compSci, 97));

      IFileWriter fileWriter = mock(IFileWriter.class);

      TopicScoreWriter cut = new TopicScoreWriter(fileWriter);

      // act
      cut.writeScores(topTopScores);

      // assert
      verify(fileWriter, times(1)).writeLine(physicsResult);
      verify(fileWriter, times(1)).writeLine(artsResult);
      verify(fileWriter, times(1)).writeLine(comSciResult);
   }
}