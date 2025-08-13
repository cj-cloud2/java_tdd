import logging

class TextFileLoader:
    def load_file(self, fname, line_processor):
        """
        Loads a text file line by line and processes each line using a provided function.

        Parameters:
        - fname (str): The path to the text file to load.
        - line_processor: A function or lambda that accepts two arguments:
          (collection, line) and processes each line, typically by appending it to the collection.

        Returns:
        - list[str]: The processed list of lines.

        This approach allows custom behavior for each line without tightly coupling to file I/O,
        mimicking the flexibility of interfaces in Java using Python's first-class functions.
        """
        lines = []
        try:
            with open(fname, 'r', encoding='utf-8') as file:
                for line in file:
                    # Call the passed-in function to process and add the line
                    line_processer(lines, line.strip())
        except IOError as ex:
            logging.error("Error reading file %s: %s", fname, ex)

        return lines

