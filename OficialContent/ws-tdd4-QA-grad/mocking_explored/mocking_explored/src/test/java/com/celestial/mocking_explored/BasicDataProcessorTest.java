/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.celestial.mocking_explored;

import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 *
 * @author selvy
 */
public class BasicDataProcessorTest
{
    @Test
    public void test_count_chars_in_BasicDataProcessor_no_mocking()
    {
        // arrange
        TextFileSource<ArrayList<String>> tfl = new TextFileSource<>();
        String fname = "D:\\S3sampleFiles\\info.txt";
        BasicDataProcessor<ArrayList<String>> dl = new BasicDataProcessor<>(tfl);
        long expected = 1766;
        
        // act
        long result = dl.loadData(fname);
        
        // assert
        assertEquals( expected, result );
    }
    
    @Test
    public void test_whats_the_problem_we_are_trying_to_solve()
    {
        class B
        {
            int doSomething(int x, int y){ return x + y;}
        }
        class A
        {
            B b = new B();
            
            int doSomething(int x, int y, int z)
            {
                return b.doSomething(x, y) + z;
            }
        }
        // arrange
        // In order to run this test A must create an instance of B
        // this is tightly coupled code.  If B were accessing the file system
        // to load a file and that file was not there, the test would fail
        A cut = new A();
        int x = 3;
        int y = 6;
        int z = 9;
        int expected = 18;
        
        // act
        int result = cut.doSomething(x, y, z);
        
        // assert
        assertEquals(expected, result);
    }

    @Test
    public void test_whats_the_problem_we_are_trying_to_solve_loosening_the_coupling()
    {
        class B
        {
            int doSomething(int x, int y){ return x + y;}
        }
        class A
        {
            B b;
            
            A( B b )
            {
                this.b = b;
            }
            
            int doSomething(int x, int y, int z)
            {
                return b.doSomething(x, y) + z;
            }
        }
        // arrange
        // A can now run with any instance of B, we've loosened the coupling
        // But we still have the issue of If B were accessing the file system
        // to load a file and that file was not there, the test would fail
        B b = new B();
        A cut = new A(b);
        int x = 3;
        int y = 6;
        int z = 9;
        int expected = 18;
        
        // act
        int result = cut.doSomething(x, y, z);
        
        // assert
        assertEquals(expected, result);
    }

    interface IB
    {
        int doSomething(int x, int y);
    }
    
    @Test
    public void test_whats_the_problem_we_are_trying_to_solve_loosely_coupled_with_interface()
    {
        class B implements IB
        {
            @Override
            public int doSomething(int x, int y){ return x + y;}
        }
        class X implements IB
        {
            @Override
            public int doSomething(int x, int y){ return x*1 + y*1;}
        }
            
        class A
        {
            IB b;
            
            A( IB b )
            {
                this.b = b;
            }
            
            int doSomething(int x, int y, int z)
            {
                return b.doSomething(x, y) + z;
            }
        }
        // arrange
        // A can now run with any instance that implements the IB interface
        // So we have completely decoupled A from B
        // We are now free to provide to A a substitute that does not for example
        // perform an IO
        IB child = new X();
        A cut = new A(child);
        int x = 3;
        int y = 6;
        int z = 9;
        int expected = 18;
        
        // act
        int result = cut.doSomething(x, y, z);
        
        // assert
        assertEquals(expected, result);
    }

    @Test
    public void test_whats_the_problem_we_are_trying_to_solve_introducing_mocking()
    {
        class A
        {
            IB b;
            
            A( IB b )
            {
                this.b = b;
            }
            
            int doSomething(int x, int y, int z)
            {
                return b.doSomething(x, y) + z;
            }
        }
        // arrange
        // A can now run with any instance that implements the IB interface
        // So we have completely decoupled A from B
        // We are now free to provide to A a substitute that does not for example
        // perform an IO
        IB child = mock(IB.class);  // create the mock
        A cut = new A(child);
        int x = 3;
        int y = 6;
        int z = 9;
        int expected = 18;
        
        // setup the expectations
        when(child.doSomething(3, 6)).thenReturn(9);
        
        // act
        int result = cut.doSomething(x, y, z);
        
        // assert
        assertEquals(expected, result);
    }
    
    @Test
    public  void  test_how_does_mockito_work()
    {
       // Standard use of ArrayList is as follows
       ArrayList<Integer> ints = new ArrayList<>(10);
       ints.add(1);
       ints.add(2);
       ints.add(3);
       
       assertEquals(3, ints.size(), "This is should work");
       
       // Now mocking the ArrayList
       
       ArrayList<Integer> m_ints = mock(ArrayList.class);
       when(m_ints.size()).thenReturn(4);
       
       // It doesn't matter how many times you call m_ints.add(), 
       // m_ints.size() will always return 4
       
       // we've added nothing yet
       assertEquals( 4, m_ints.size(), "This si crazy, it shoudn't work but it does");

       // Let's add some numbers
        m_ints.add(3);
        m_ints.add(7);
        assertEquals(2, m_ints.size(), "This should fail");

    }

    @Test
    public  void    test_fun_with_mocks()
    {
        // mocks create scaffold code, no methods, just place holders
        // if you call a method that you haven't specified a when() on it returns the default value based on the method's signature
        // So let's try it again to see how this works - ArrayList.isEmpty() returns true if the collection has no elements
        // We've not added an expectation for isEmpty() so it default to false

        // Now mocking the ArrayList
        ArrayList<Integer> m_ints = mock(ArrayList.class);
        when(m_ints.size()).thenReturn(4);

        // We've added no items so isEmpty() should return true
        boolean result = m_ints.isEmpty();
        assertEquals( true, result, "The collection is empty");
    }
    
    @Test
    public void test_count_chars_in_BasicDataProcessor_with_mocking()
    {
        // arrange
        TextFileSource mock = mock(TextFileSource.class);
        ArrayList<String> items = new ArrayList<>();
        items.add("line 1");
        items.add("line 2");
        items.add("line 3");

        when(mock.loadData(any())).thenReturn(items);

        // Look carefully at how DataLoader.lodData() works, line
        BasicDataProcessor<ArrayList<String>> cut = new BasicDataProcessor<>(mock);
        long expected = 18;
        
        // act
        long result = cut.loadData("");
        
        // assert
        assertEquals( expected, result );
    }    
}
