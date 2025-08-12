/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.p005_qa;

public class TopicScores
{
   private String topicName;
   private int[] scores;
   
   public TopicScores(String topicName, int[] scores)
   {
      this.topicName = topicName;
      this.scores = scores;
   }
   
   public String   getTopicName()
   {
      return topicName;
   }
   
   public   int[] getScores()
   {
      return scores;
   }
}
