import unittest
from app.text_file_source import TextFileSource
from app.basic_data_processor import BasicDataProcessor
from unittest.mock import Mock

class TestDataLoader(unittest.TestCase):
    def test_count_chars_in_basic_data_processor_no_mocking(self):
        # arrange
        tfl = TextFileSource()
        fname = "data.txt" # "C:/tmp/KeyboardHandler.txt"
        dl = BasicDataProcessor(tfl)
        expected = 20 #1383

        # act
        result = dl.load_data(fname)

        # assert
        self.assertEqual(expected, result)


    def test_count_chars_in_basic_data_processor_with_mocking(self):
        # arrange
        mock_data_source = Mock()
        mock_lines = ["line 1", "line 2", "line 3"]
        mock_data_source.load_data.return_value = mock_lines

        cut = BasicDataProcessor(mock_data_source)
        expected = 18  # 6 + 6 + 6

        # act
        result = cut.load_data("")

        # assert
        self.assertEqual(result, expected)



if __name__ == '__main__':
    unittest.main()

