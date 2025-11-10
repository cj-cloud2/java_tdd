package com.celestial.mocking_explored;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Stack;


class TextFileSourceTest
{
    @Test
    void howto_use_loadData_method_with_single_param()
    {
        TextFileSource<ArrayList<String>> tfl = new TextFileSource<>();

        String fname = "C:\\tmp\\KeyboardHandler.java.txt";
        ArrayList<String> lines = tfl.loadData(fname);

        lines.forEach((element) ->{
            System.out.println(">> " + element);
        });
    }

    @Test
    void howto_use_LoadData_method_with_3_params()
    {
        TextFileSource<ArrayList<String>> tfl = new TextFileSource<>();

        // We create a lambda expression to do the work in the TextFileLoader
        ICollectionLoader<ArrayList<String>> functor = (c, l) -> {
            c.add(l);
            return c;
        };

        String fname = "C:\\tmp\\KeyboardHandler.java.txt";
        ArrayList<String> data = new ArrayList<>();

        ArrayList<String> lines = tfl.loadData(fname, data, functor);

        lines.forEach((element) ->{
            System.out.println(">> " + element);
        });
    }

    @Test
    void howto_use_LoadData_method_with_3_params_using_queue()
    {
        TextFileSource<ArrayList<String>> tfl = new TextFileSource<>();

        // We create a lambda expression to do the work in the TextFileLoader
        ICollectionLoader<PriorityQueue<String>> functor = (c, l) -> {
            c.add(l);
            return c;
        };

        String fname = "C:\\tmp\\KeyboardHandler.java.txt";
        PriorityQueue<String> data = new PriorityQueue<>();

        PriorityQueue<String> lines = tfl.loadData(fname, data, functor);

        lines.forEach((element) ->{
            System.out.println(">> " + element);
        });
    }

    @Test
    void howto_use_LoadData_method_with_3_params_using_Stack()
    {
        TextFileSource<ArrayList<String>> tfl = new TextFileSource<>();

        // We create a lambda expression to do the work in the TextFileLoader
        ICollectionLoader<Stack<String>> functor = (c, l) -> {
            c.push(l);
            return c;
        };

        String fname = "C:\\tmp\\KeyboardHandler.java.txt";
        Stack<String> data = new Stack<>();

        Stack<String> lines = tfl.loadData(fname, data, functor);

        lines.forEach((element) ->{
            System.out.println(">> " + element);
        });
    }
}