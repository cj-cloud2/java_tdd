import unittest
from app.data_clerk import DataClerk, FileLog
from unittest.mock import Mock
import datetime

'''
!python -m unittest discover -s tests -p "test_*.py"

python -m unittest: Tells Python to run the unittest module.
discover: This is a subcommand that tells unittest to automatically find all test files
-s tests: Specifies the start directory for test discovery. Here, it's the tests folder.
-p "test_*.py": This is the pattern for finding test files. It tells unittest to only 
consider files that start with test_ and end with .py.
'''

class DataClerkTest(unittest.TestCase):
    def test_process_data(self):
        # Arrange
        fl = FileLog()
        cut = DataClerk(fl)

        # Act
        cut.process_data()

        # Assert
        # No assert, just checking that it runs (like the original Java test)
        # Output must be inspected manually or via logging


    def test_files_are_logged_before_8pm_mocked(self):
        # Arrange
        mock_file_log = Mock()
        cut = DataClerk(mock_file_log)

        # Act
        cut.process_data()

        # Assert
        mock_file_log.clear_the_log.assert_called_once()


    def test_files_are_wrongly_logged_after_8pm_with_mocked_time(self):
        # We can now run this test at any time of the day
        # This test now passes because we are able to set the time in the test
        # Arrange
        mock_file_log = Mock()
        cut = DataClerk(mock_file_log)

        # Patch get_time on the specific instance (like a spy in Java)
        cut.get_time = lambda: datetime.time(21, 0)

        # Act
        cut.process_data()

        # Assert
        mock_file_log.clear_the_log.assert_not_called()

if __name__ == '__main__':
    unittest.main()

