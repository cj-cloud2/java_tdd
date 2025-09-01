/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.celestial.mocking_explored;

/**
 *
 * @author selvy
 * @param <T>
 */
public class BasicDataProcessor<T>
{
    private final IDataSource<T> dataSource;
    
    public BasicDataProcessor(IDataSource<T> ds )
    {
        dataSource = ds;
    }
    
    public  <T> long    loadData(String fname)
    {
        Iterable<String> data = dataSource.loadData(fname);
        long count = 0;
        for( var datum : data )
        {
            count += datum.length();
        }
        
        return count;
    }
}
