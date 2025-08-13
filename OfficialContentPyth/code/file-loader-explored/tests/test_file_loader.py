import unittest
from app.file_loader import FileLoader

class TestFileLoader(unittest.TestCase):

    def test_load_all_of_file_using_inbuilt_files_type_as_lambda(self):
        # Arrange
        file_to_load = "sample.txt"
        cut = FileLoader(file_to_load)
        expected_bytes_read = 10

        # Prepare fake file contents
        pretend_file_content = ["Hello", "world"]

        # Act
        bytes_read = cut.load_file_with_func(lambda fname: pretend_file_content)

        # Assert
        self.assertEqual(expected_bytes_read, bytes_read)

    def test_load_all_of_file_via_stub(self):
        """ Use a hardcoded stub to simulate reading two lines of text
            Benefit - no dependencyon actual files or filesystem
                    - portable test
                    - FileLoader is more flexible and decoupled allowing
                      file loading mechanism to be injected
        """
        # arrange
        file_to_load = ""
        cut = FileLoader(file_to_load)
        expected_bytes_read = 10

        # act
        bytes_read = cut.load_file_with_func(lambda fname: ["Hello", "world"])

        # assert
        self.assertEqual(expected_bytes_read, bytes_read)


    def test_load_all_of_file_using_mock(self):
        # Arrange
        file_to_load = "c:/tmp/KeyboardHandler.txt"
        cut = FileLoader(file_to_load)

        # Simulate file content
        pretend_file_content = ["Hello", "world"]
        expected_bytes_read = 10

        # Define a fake file interface with a mocked method
        class FakeFile:
            def read_all_lines(self, path, encoding):
                return pretend_file_content

        fake_file = FakeFile()

        # Act
        bytes_read = cut.load_file_with_func(
            lambda fname: fake_file.read_all_lines(fname, "utf-8")
        )

        # Assert
        self.assertEqual(expected_bytes_read, bytes_read)



if __name__ == '__main__':
    unittest.main()

