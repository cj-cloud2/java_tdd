import unittest
from app.highest_number_finder import HighestNumberFinder

class TestHighestNumberFinder(unittest.TestCase):
    def test_find_highest_in_list_of_one_expect_single_item(self):
        # Arrange
        numbers = [10]
        cut = HighestNumberFinder()
        expected_result = 10

        # Act
        result = cut.find_highest_number(numbers)

        # Assert
        self.assertEqual(expected_result, result)


    def test_find_highest_in_list_of_two_descending_expect_first_element(self):
        # Arrange
        numbers = [20, 10]
        expected_result = 20
        cut = HighestNumberFinder()

        # Act
        result = cut.find_highest_number(numbers)

        # Assert
        self.assertEqual(result, expected_result)

    def test_find_highest_in_list_of_two_ascending_expect_second_element(self):
        # Arrange
        numbers = [10, 20]
        expected_result = 20
        cut = HighestNumberFinder()

        # Act
        result = cut.find_highest_number(numbers)

        # Assert
        self.assertEqual(result, expected_result)


if __name__ == '__main__':
    unittest.main()

