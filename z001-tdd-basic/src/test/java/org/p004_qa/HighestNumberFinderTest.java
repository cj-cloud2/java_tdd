package org.p004_qa;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author selvy
 */
public class HighestNumberFinderTest {

    // Test case for an array with only one element. It should return that single element.
    @Test
    void findHighestInArrayOfOneExpectSingleItem() {
        // Arrange
        int[] array = {10};
        HighestNumberFinder cut = new HighestNumberFinder();
        int expectedResult = 10;

        // Act
        int result = cut.findHighestNumber(array);

        // Assert
        Assertions.assertEquals(expectedResult, result);
    }

    // Test case for an array with two elements in descending order. It should return the first element.
    @Test
    void findHighestInArrayOfTwoDescendingExpectFirstElement() {
        // Arrange
        int[] array = {20, 10};
        int expectedResult = 20;
        HighestNumberFinder cut = new HighestNumberFinder();

        // Act
        int result = cut.findHighestNumber(array);

        // Assert
        Assertions.assertEquals(expectedResult, result);
    }

    // Test case for an array with two elements in ascending order. It should return the second element.
    @Test
    void findHighestInArrayOfTwoAscendingExpectSecondElement() {
        // Arrange
        int[] array = {10, 20};
        int expectedResult = 20;
        HighestNumberFinder cut = new HighestNumberFinder();

        // Act
        int result = cut.findHighestNumber(array);

        // Assert
        Assertions.assertEquals(expectedResult, result);
    }

    // Test case for an array with two equal elements. It should return either element.
    @Test
    void findHighestInArrayOfTwoEqualsExpectFirstElement() {
        // Arrange
        int[] array = {10, 10};
        int expectedResult = 10;
        HighestNumberFinder cut = new HighestNumberFinder();

        // Act
        int result = cut.findHighestNumber(array);

        // Assert
        Assertions.assertEquals(expectedResult, result);
    }

    // Test case for an array with several items, where the highest number is not at the beginning or end.
    @Test
    void findHighestInArrayOfSeveralItemsExpectHighestElement() {
        // Arrange
        int[] array = {10, 20, 5, 17, 37, 14};
        int expectedResult = 37;
        HighestNumberFinder cut = new HighestNumberFinder();

        // Act
        int result = cut.findHighestNumber(array);

        // Assert
        Assertions.assertEquals(expectedResult, result);
    }
}