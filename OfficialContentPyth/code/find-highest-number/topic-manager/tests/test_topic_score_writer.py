import unittest
from unittest.mock import Mock
from app.topic_score_writer import TopicScoreWriter
from app.topic_top_score import TopicTopScore

class TopicScoreWriterTest(unittest.TestCase):
    def test_verify_topic_score_details_written_out_once(self):
        # Arrange
        physics = "Physics"
        expected_result = "Physics, 89"
        top_scores = [TopicTopScore(physics, 89)]

        mock_file_writer = Mock()
        mock_file_writer.filename = "testfile.txt"
        cut = TopicScoreWriter(mock_file_writer)

        # Act
        cut.write_scores(top_scores)

        # Assert
        mock_file_writer.write_line.assert_called_once_with(expected_result)

if __name__ == '__main__':
    unittest.main()

