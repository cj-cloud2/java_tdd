/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.p004_qa;


/**
 *
 * @author selvy
 */
public  class HighestNumberFinder implements IHighestNumberFinder
{
   @Override
    public int findHighestNumber(int[] array)
    {
        int highestSoFar = Integer.MIN_VALUE;
        
        for( int val : array )
        {
            if( val > highestSoFar )
                highestSoFar = val;
        }
        return highestSoFar;
    }    
}
