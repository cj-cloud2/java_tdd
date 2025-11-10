/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.s2s.demos.topicmanager;

/**
 *
 * @author selvy
 */
class Topic
{
   private  String topicName;
   int      score;
   
   public   Topic( String topicName, int score)
   {
      this.topicName = topicName;
      this.score = score;
   }

   public String getTopicName()
   {
      return topicName;
   }

   public int getScore()
   {
      return score;
   }
}
