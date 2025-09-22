/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.celestial.mocking_explored;

/**
 *
 * @author selvy
 */
public interface IDataSource<T>
{
    public  <T> T loadData(String fname );
    public  <T> T loadData(  String fname, T line, ICollectionLoader<T> func );
}
