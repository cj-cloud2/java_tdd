/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.p005_qa;

import org.p004_qa.IHighestNumberFinder;

import java.util.ArrayList;

/**
 *
 * @author selvy
 */
class TopicManager
{
   private final IHighestNumberFinder highestNumberFinder;

   public  TopicManager()
   {
      this.highestNumberFinder = null;
   }
   
   public  TopicManager( IHighestNumberFinder hnf )
   {
       highestNumberFinder = hnf;
   }
    
   ArrayList<TopicTopScore> findTopicHighScores(ArrayList<TopicScores> array)
   {
      ArrayList<TopicTopScore> topScores = new ArrayList<>();

      for(TopicScores ts : array)
      {
          int topScore = highestNumberFinder.findHighestNumber(ts.getScores());

          topScores.add(new TopicTopScore(ts.getTopicName(), topScore));
      }
      return topScores;
   }
}
