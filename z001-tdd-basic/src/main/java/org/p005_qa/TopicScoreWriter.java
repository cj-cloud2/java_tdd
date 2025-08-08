/*
 * OpenSource
 * 
 * 
 */
package org.p005_qa;

import java.util.ArrayList;

/**
 *
 * @author selvy
 */
public class TopicScoreWriter
{
   private IFileWriter itsFileWriter;
   
   public TopicScoreWriter(IFileWriter fileWriter )
   {
      this.itsFileWriter = fileWriter;
   }

   void writeScores(ArrayList<TopicTopScore> topTopScores)
   {
      for(TopicTopScore tts : topTopScores)
      {
         String dataToWrite = tts.getTopicName() + ", " + tts.getTopScore();
         itsFileWriter.writeLine(dataToWrite);
      }
   }
}
