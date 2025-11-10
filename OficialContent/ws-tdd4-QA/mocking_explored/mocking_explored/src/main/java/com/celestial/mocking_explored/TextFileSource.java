/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.celestial.mocking_explored;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @param <T>
 * @author selvy
 */
public class TextFileSource<T> implements IDataSource<T> {
    @Override
    public <T> T loadData(String fname) {
        ArrayList<String> lines = new ArrayList<>();

        // We create a lambda expression to do the work in the TextFileLoader
        ICollectionLoader<ArrayList<String>> functor = (c, l) -> {
            c.add(l);
            return c;
        };
        return (T) loadData(fname, lines, functor);
    }

    @Override
    public <T> T loadData(String fname, T lines, ICollectionLoader<T> func) {
        FileReader fr = null;
        try {
            String fileName = fname;
            File file = new File(fileName);
            fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                // process the line
                // we use a lambda here to do the heavy lifting
                func.addElement(lines, line);
            }
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fr.close();
            } catch (IOException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return lines;
    }
}
