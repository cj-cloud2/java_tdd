package com.celestial.mockito.filetodb;

import java.util.List;

/**
 *
 * @author selvy
 */
@FunctionalInterface
public interface ILoader 
{
    List<String>    loadFile(String fname);
}
