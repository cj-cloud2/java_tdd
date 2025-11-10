package com.celestial.tokeniser;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class StringTokeniserTest {
    @Test
    public void empty_string_result_empty_array() {
        // If this was the only requirement,
        // then the CUT as coded would be sufficient
        // arrange
        String inputVal = "";
        StringTokeniser cut = new StringTokeniser();
        String[] expectedResult = {};

        // act
        String[] actualResult = cut.tokenise(inputVal);

        // assert
        assertArrayEquals(expectedResult, actualResult);
    }


    @Test
    public void string_of_one_result_array_of_one() {
        // arrange
        String inputVal = "csharp";
        StringTokeniser cut = new StringTokeniser();
        String[] expectedResult = {"csharp"};

        // act
        String[] actualResult = cut.tokenise(inputVal);

        // assert
        assertArrayEquals(expectedResult, actualResult);
    }

    @Test
    public void string_of_two_items_result_array_of_two_strings() {
        // arrange
        String inputVal = "csharp,python";
        StringTokeniser cut = new StringTokeniser();
        String[] expectedResult = {"csharp", "python"};

        // act
        String[] actualResult = cut.tokenise(inputVal);

        // assert
        assertArrayEquals(expectedResult, actualResult);
    }

    @Test
    public void string_of_many_items_no_spaces_result_array_of_many_strings() {
        // With this test the CUT still works
        // arrange
        String inputVal = "java,C#,python";
        StringTokeniser cut = new StringTokeniser();
        String[] expectedResult = {"java", "C#", "python"};

        // act
        String[] actualResult = cut.tokenise(inputVal);

        // assert
        assertArrayEquals(expectedResult, actualResult);
    }

    @Test
    public void string_of_compound_items_no_spaces_result_array_of_many_strings() {
        // With this test the CUT still works
        // arrange
        String inputVal = "java byte code,C#,python";
        StringTokeniser cut = new StringTokeniser();
        String[] expectedResult = {"java byte code", "C#", "python"};

        // act
        String[] actualResult = cut.tokenise(inputVal);

        // assert
        assertArrayEquals(expectedResult, actualResult);
    }

    @Test
    public void string_of_one_result_array_of_one_spaces_removed() {
        // Initially this test will fail, the CUT needs a trim() method to be called
        // arrange
        String inputVal = " csharp ";
        StringTokeniser cut = new StringTokeniser();
        String[] expectedResult = {"csharp"};

        // act
        String[] actualResult = cut.tokenise(inputVal);

        // assert
        assertArrayEquals(expectedResult, actualResult);
    }

    @Test
    public void string_of_one_with_preceding_comma_result_array_of_one_spaces_removed() {
        // CUT still works as required
        // arrange
        String inputVal = ",csharp";
        StringTokeniser cut = new StringTokeniser();
        String[] expectedResult = {"csharp"};

        // act
        String[] actualResult = cut.tokenise(inputVal);

        // assert
        assertArrayEquals(expectedResult, actualResult);
    }

    //=====================================================================

    @Test
    public void empty_string_result_empty_array_todel()
    {
        // If this was the only requirement,
        // then the CUT as coded would be sufficient
        // arrange
        String inputVal = "";
        StringTokeniser1 cut = new StringTokeniser1();
        String[] expectedResult = {};

        // act
        String[] actualResult = cut.tokenise( inputVal );

        // assert
        assertArrayEquals( expectedResult, actualResult );
    }
/*
    @Test
    public void string_of_one_result_array_of_one_todel() {
        // arrange
        String inputVal = "csharp";
        StringTokeniser1 cut = new StringTokeniser1();
        String[] expectedResult = {"csharp"};

        // act
        String[] actualResult = cut.tokenise(inputVal);

        // assert
        assertArrayEquals(expectedResult, actualResult);
    }





    @Test
    public void string_of_two_items_result_array_of_two_strings() {
        // arrange
        String inputVal = "csharp,python";
        StringTokeniser cut = new StringTokeniser();
        String[] expectedResult = {"csharp", "python"};

        // act
        String[] actualResult = cut.tokenise(inputVal);

        // assert
        assertArrayEquals(expectedResult, actualResult);
    }

    @Test
    public void string_of_many_items_no_spaces_result_array_of_many_strings() {
        // With this test the CUT still works
        // arrange
        String inputVal = "java,C#,python";
        StringTokeniser cut = new StringTokeniser();
        String[] expectedResult = {"java", "C#", "python"};

        // act
        String[] actualResult = cut.tokenise(inputVal);

        // assert
        assertArrayEquals(expectedResult, actualResult);
    }

    @Test
    public void string_of_compound_items_no_spaces_result_array_of_many_strings() {
        // With this test the CUT still works
        // arrange
        String inputVal = "java byte code,C#,python";
        StringTokeniser cut = new StringTokeniser();
        String[] expectedResult = {"java byte code", "C#", "python"};

        // act
        String[] actualResult = cut.tokenise(inputVal);

        // assert
        assertArrayEquals(expectedResult, actualResult);
    }

    @Test
    public void string_of_one_result_array_of_one_spaces_removed() {
        // Initially this test will fail, the CUT needs a trim() method to be called
        // arrange
        String inputVal = " csharp ";
        StringTokeniser cut = new StringTokeniser();
        String[] expectedResult = {"csharp"};

        // act
        String[] actualResult = cut.tokenise(inputVal);

        // assert
        assertArrayEquals(expectedResult, actualResult);
    }

    @Test
    public void string_of_one_with_preceding_comma_result_array_of_one_spaces_removed() {
        // CUT still works as required
        // arrange
        String inputVal = ",csharp";
        StringTokeniser cut = new StringTokeniser();
        String[] expectedResult = {"csharp"};

        // act
        String[] actualResult = cut.tokenise(inputVal);

        // assert
        assertArrayEquals(expectedResult, actualResult);
    }
     */
}
