import unittest
from app.string_tokeniser import StringTokeniser

#How to run
# cd to string-tokeniser
#!python -m unittest tests/test_string_tokeniser.py

class TestStringTokeniser(unittest.TestCase):

    def test_empty_string_result_empty_array(self):
        # If this was the only requirement, 
        # then the CUT as coded would be sufficient
        # arrange
        input_val = ""
        cut = StringTokeniser()
        expected_result = []

        # act
        actual_result = cut.tokenise(input_val)

        # assert
        self.assertEqual(expected_result, actual_result)


    def test_string_of_one_result_list_of_one(self):
        # arrange
        input_val = "csharp"
        cut = StringTokeniser()
        expected_result = ["csharp"]

        # act
        actual_result = cut.tokenise(input_val)

        # assert
        self.assertEqual(expected_result, actual_result)


    def test_string_of_two_items_result_list_of_two_strings(self):
        # arrange
        input_val = "csharp,python"
        cut = StringTokeniser()
        expected_result = ["csharp", "python"]

        # act
        actual_result = cut.tokenise(input_val)

        # assert
        self.assertEqual(expected_result, actual_result)


    def test_string_of_many_items_no_spaces_result_list_of_many_strings(self):
        # arrange
        input_val = "java,C#,python"
        cut = StringTokeniser()
        expected_result = ["java", "C#", "python"]

        # act
        actual_result = cut.tokenise(input_val)

        # assert
        self.assertEqual(expected_result, actual_result)

    def test_string_of_compound_items_no_spaces_result_list_of_many_strings(self):
        # arrange
        input_val = "java byte code,C#,python"
        cut = StringTokeniser()
        expected_result = ["java byte code", "C#", "python"]

        # act
        actual_result = cut.tokenise(input_val)

        # assert
        self.assertEqual(expected_result, actual_result)


    def test_string_of_one_result_array_of_one_spaces_removed(self):
        # arrange
        input_val = " csharp "
        cut = StringTokeniser()
        expected_result = ["csharp"]

        # act
        actual_result = cut.tokenise(input_val)

        # assert
        self.assertEqual(expected_result, actual_result)


    def test_string_of_one_with_preceding_comma_result_list_of_one_spaces_removed(self):
        # arrange
        input_val = ",csharp"
        cut = StringTokeniser()
        expected_result = ["csharp"]

        # act
        actual_result = cut.tokenise(input_val)

        # assert
        self.assertEqual(actual_result, expected_result)


if __name__ == "__main__":
    unittest.main()

