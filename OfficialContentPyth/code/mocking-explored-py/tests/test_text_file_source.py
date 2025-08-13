import unittest
from app.text_file_source import TextFileSource
from queue import PriorityQueue

class TestTextFileSource(unittest.TestCase):
    def test_how_to_use_load_data_method_with_single_param(self):
        """
        Demonstrates usage of TextFileSource.load_data() with a single filename parameter.
        """
        tfl = TextFileSource()
        fname = "data.txt" # "C:\\tmp\\KeyboardHandler.java.txt"
        lines = tfl.load_data(fname)

        for element in lines:
            print(f">> {element}")

    def test_how_to_use_load_data_method_with_3_params(self):
        """
        Demonstrates usage of TextFileSource.load_data() with a filename, collection, 
        and line processor function.
        """
        tfl = TextFileSource()
        fname = "data.txt" # "C:\\tmp\\KeyboardHandler.java.txt"
        data = []

        # Define lambda equivalent for line processor
        line_processor = lambda col, line: col + [line]

        lines = tfl.load_data(fname, data, line_processor)

        for element in lines:
            print(f">> {element}")


    def test_how_to_use_load_data_method_with_3_params_using_queue(self):
        # Arrange
        tfl = TextFileSource()
        fname = "data.txt" # "C:\\tmp\\KeyboardHandler.java.txt"
        data = PriorityQueue()

        # Define line processor to enqueue each stripped line
        def line_processor(col, line):
            col.put(line.strip())
            return col

        # Act
        result = tfl.load_data(fname, data, line_processor)

        # Print contents for demonstration
        print(">> Lines from priority queue:")
        while not result.empty():
            print(f">> {result.get()}")


    def test_how_to_use_load_data_method_with_3_params_using_stack_LIST(self):
        # Arrange
        tfl = TextFileSource()
        fname = "data.txt" # "C:\\tmp\\KeyboardHandler.txt"
        stack = []  # using list as a stack

        # Define line processor to push each line onto the stack
        def line_processor(col, line):
            col.append(line.strip())  # equivalent of Java's stack.push()
            return col

        # Act
        result = tfl.load_data(fname, stack, line_processor)

        # Print contents for demonstration (LIFO order)
        print(">> Lines from stack:")
        while result:
            print(f">> {result.pop()}")  # LIFO behavior



if __name__ == "__main__":
    unittest.main()

