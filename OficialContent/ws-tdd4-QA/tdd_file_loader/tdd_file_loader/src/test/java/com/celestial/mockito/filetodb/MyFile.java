/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.celestial.mockito.filetodb;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.List;

/**
 *
 * @author selvy
 */
public interface MyFile
{
   public List<String> readAllLines(Path path, Charset cs); 
}
