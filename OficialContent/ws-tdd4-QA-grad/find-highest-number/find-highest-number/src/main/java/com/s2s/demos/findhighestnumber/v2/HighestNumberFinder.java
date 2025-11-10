/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.s2s.demos.findhighestnumber.v2;

/**
 *
 * @author selvy
 */
class HighestNumberFinder
{
    int findHighestNumber(int[] array)
    {
        int highestSoFar = array[0];
         
        if( array.length > 1 && array[1] > highestSoFar )
            highestSoFar = array[1];
         
        return highestSoFar;
    }
    
}
