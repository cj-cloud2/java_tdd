/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.s2s.demos.findhighestnumber.v3;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author selvy
 */
public class HighestNumberFinderTest
{
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void find_highest_in_array_of_one_expect_single_item() 
    {
        // Arrange
        int array[] = 
        {
            10
        };
        HighestNumberFinder cut = new HighestNumberFinder();
        int expectedResult = 10;
        
        // Act
        int result = cut.findHighestNumber( array );
        
        // Assert
        assertEquals( expectedResult, result );
    }
    
    @Test
    public  void    find_highest_in_array_of_two_descending_expect_first_element()
    {
        // Arrange
        int array[] = {20, 10};
        int expectedResult = 20;
        HighestNumberFinder cut = new HighestNumberFinder();
        
        // Act
        int result = cut.findHighestNumber(array);
        
        // Assert
        assertEquals(expectedResult, result);
    }
    
    @Test
    public  void    find_highest_in_array_of_two_ascending_expect_second_element()
    {
        // Arrange
        int array[] = {10, 20};
        int expectedResult = 20;
        HighestNumberFinder cut = new HighestNumberFinder();
        
        // Act
        int result = cut.findHighestNumber(array);
        
        // Assert
        assertEquals(expectedResult, result);
    }
}
