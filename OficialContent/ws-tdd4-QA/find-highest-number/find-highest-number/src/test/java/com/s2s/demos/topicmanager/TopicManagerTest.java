/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.s2s.demos.topicmanager
    ;
import com.s2s.demos.findhighestnumber.IHighestNumberFinder;
import com.s2s.demos.findhighestnumber.fin.HighestNumberFinder;
import java.util.ArrayList;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 *
 * @author selvy
 */
public class TopicManagerTest
{
   @Test
   public   void  find_heighest_score_in_empty_array_return_empty_array()
   {
      // Arrange
      ArrayList<TopicScores> array = new ArrayList<>();
      TopicManager cut = new TopicManager();
      ArrayList<TopicTopScore> expectedResult = new ArrayList<>();

      // Act
      ArrayList<TopicTopScore> result = cut.findTopicHighScores( array );

      // Assert
      assertEquals( expectedResult, result );
   }
   
   @Test
   public void find_heighest_score_with_array_of_one_return_array_of_one()
   {
       // Arrange
       int[] scores = { 56, 67, 45, 89 };
       String topicName = "Physics";
       ArrayList<TopicScores> topicScores = new ArrayList<>();
       topicScores.add(new TopicScores(topicName, scores));
   
       HighestNumberFinder hnf = new HighestNumberFinder();
       TopicManager cut = new TopicManager(hnf);
       ArrayList<TopicTopScore> expectedResult = new ArrayList<>();
       expectedResult.add(new TopicTopScore(topicName, 89));

       // Act
       ArrayList<TopicTopScore> result = cut.findTopicHighScores(topicScores);

       
      assertEquals(expectedResult.get(0).getTopicName(), result.get(0).getTopicName());
      assertEquals(expectedResult.get(0).getTopScore(), result.get(0).getTopScore() );
   }
   
   @Test
   public void find_heighest_score_with_array_of_one_return_array_of_one_using_stub()
   {
       // Arrange
       int[] scores = { 56, 67, 45, 89 };
       String topicName = "Physics";
       ArrayList<TopicScores> topicScores = new ArrayList<>();
       topicScores.add(new TopicScores(topicName, scores));
   
       // Use a stub version of HighestNumberFinder
       com.s2s.demos.topicmanager.HighestNumberFinder hnf = 
                        new com.s2s.demos.topicmanager.HighestNumberFinder();
       TopicManager cut = new TopicManager(hnf);
       ArrayList<TopicTopScore> expectedResult = new ArrayList<>();
       expectedResult.add(new TopicTopScore(topicName, 89));

       // Act
       ArrayList<TopicTopScore> result = cut.findTopicHighScores(topicScores);

       
      assertEquals(expectedResult.get(0).getTopicName(), result.get(0).getTopicName());
      assertEquals(expectedResult.get(0).getTopScore(), result.get(0).getTopScore() );
   }
   
   @Test
   public  void find_heighest_score_with_array_of_many_return_array_of_many_using_stub()
   {
      //[{“Physics”, { 56, 67, 45, 89} }, {“Art”, { 87, 66, 78} }, {“Comp Sci”, { 45, 88, 97, 56} }]
      // Arrange
      int[] physics_scores = { 56, 67, 45, 89 };
      String physics = "Physics";
      int[] art_scores = { 87, 66, 78 };
      String art = "Art";
      int[] compSci_scores = { 45, 88, 97, 56 };
      String compSci = "Comp Sci";
      ArrayList<TopicScores> topicScores = new ArrayList<>();
      topicScores.add(new TopicScores(physics, physics_scores));
      topicScores.add(new TopicScores(art, art_scores));
      topicScores.add(new TopicScores(compSci, compSci_scores));
         // Use a stub version of HighestNumberFinder
      com.s2s.demos.topicmanager.HighestNumberFinder hnf = 
                          new com.s2s.demos.topicmanager.HighestNumberFinder();
      TopicManager cut = new TopicManager(hnf);
      ArrayList<TopicTopScore> expectedResult = new ArrayList<>();
      expectedResult.add( new TopicTopScore(physics, 89)); 
      expectedResult.add( new TopicTopScore(art, 87)); 
      expectedResult.add( new TopicTopScore(compSci, 97)); 

      // Act
      ArrayList<TopicTopScore> result = cut.findTopicHighScores(topicScores);

      // Assert
      assertEquals(expectedResult.get(0).getTopicName(), result.get(0).getTopicName());
      assertEquals(expectedResult.get(0).getTopScore(), result.get(0).getTopScore() );
      assertEquals(expectedResult.get(1).getTopicName(), result.get(1).getTopicName());
      assertEquals(expectedResult.get(1).getTopScore(), result.get(1).getTopScore() );
      assertEquals(expectedResult.get(2).getTopicName(), result.get(2).getTopicName());
      assertEquals(expectedResult.get(2).getTopScore(), result.get(2).getTopScore() );
   }

   @Test
   public  void find_heighest_score_with_array_of_many_return_array_of_many_using_mocks()
   {
      //[{“Physics”, { 56, 67, 45, 89} }, {“Art”, { 87, 66, 78} }, {“Comp Sci”, { 45, 88, 97, 56} }]
      // Arrange
      int[] physics_scores = { 56, 67, 45, 89 };
      String physics = "Physics";
      int[] art_scores = { 87, 66, 78 };
      String art = "Art";
      int[] compSci_scores = { 45, 88, 97, 56 };
      String compSci = "Comp Sci";
      ArrayList<TopicScores> topicScores = new ArrayList<>();
      topicScores.add(new TopicScores(physics, physics_scores));
      topicScores.add(new TopicScores(art, art_scores));
      topicScores.add(new TopicScores(compSci, compSci_scores));
      
      // Use a mock version of HighestNumberFinder
      IHighestNumberFinder hnf = mock( com.s2s.demos.findhighestnumber.fin.HighestNumberFinder.class );
      // Setup the expectations
      when(hnf.findHighestNumber(physics_scores)).thenReturn(89);
      when(hnf.findHighestNumber(art_scores)).thenReturn(87);
      when(hnf.findHighestNumber(compSci_scores)).thenReturn(97);
      
      TopicManager cut = new TopicManager(hnf);
      ArrayList<TopicTopScore> expectedResult = new ArrayList<>();
      expectedResult.add( new TopicTopScore(physics, 89)); 
      expectedResult.add( new TopicTopScore(art, 87)); 
      expectedResult.add( new TopicTopScore(compSci, 97)); 

      // Act
      ArrayList<TopicTopScore> result = cut.findTopicHighScores(topicScores);

      // Assert
      assertThat(expectedResult, is(result));
   }
}
