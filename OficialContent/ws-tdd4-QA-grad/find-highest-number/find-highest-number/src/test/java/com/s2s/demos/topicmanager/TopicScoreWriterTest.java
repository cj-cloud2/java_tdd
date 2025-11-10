/*
 * OpenSource
 * 
 * 
 */
package com.s2s.demos.topicmanager;

import java.util.ArrayList;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 *
 * @author selvy
 */
public class TopicScoreWriterTest
{
   @Test
   public   void  verify_topic_score_details_written_out_once()
   {
      // arrange
      String physics = "Physics";
      String art = "Art";
      String compSci = "Comp Sci";
      String expectedResult = "Physics, 89";

      ArrayList<TopicTopScore> topTopScores = new ArrayList<>();
      topTopScores.add( new TopicTopScore(physics, 89)); 
      
      String fileToWrite = "testfile.txt";
      IFileWriter fileWriter = mock(IFileWriter.class);
      
      TopicScoreWriter cut = new TopicScoreWriter( fileWriter );
      
      // act
      cut.writeScores( topTopScores );
      
      // assert
      verify( fileWriter, times(1)).writeLine(expectedResult);
   }
   
   @Test
   public   void  verify_topic_score_details_not_written()
   {
      // arrange

      ArrayList<TopicTopScore> topTopScores = new ArrayList<>();
      
      IFileWriter fileWriter = mock(IFileWriter.class);
      
      TopicScoreWriter cut = new TopicScoreWriter( fileWriter );
      
      // act
      cut.writeScores( topTopScores );
      
      // assert
      verify( fileWriter, times(0)).writeLine(Mockito.any());
   }

   @Test
   public   void  verify_topic_score_details_written_out_multiple_times()
   {
      // arrange
      String physics = "Physics";
      String art = "Art";
      String compSci = "Comp Sci";
      String physicsResult = "Physics, 89";
      String artsResult = "Art, 87";
      String comSciResult = "Comp Sci, 97";

      ArrayList<TopicTopScore> topTopScores = new ArrayList<>();
      topTopScores.add( new TopicTopScore(physics, 89)); 
      topTopScores.add( new TopicTopScore(art, 87)); 
      topTopScores.add( new TopicTopScore(compSci, 97)); 
      
      String fileToWrite = "testfile.txt";
      IFileWriter fileWriter = mock(IFileWriter.class);
      
      TopicScoreWriter cut = new TopicScoreWriter( fileWriter );
      
      // act
      cut.writeScores( topTopScores );
      
      // assert
      verify( fileWriter, times(1)).writeLine(physicsResult);
      verify( fileWriter, times(1)).writeLine(artsResult);
      verify( fileWriter, times(1)).writeLine(comSciResult);
   }
}
