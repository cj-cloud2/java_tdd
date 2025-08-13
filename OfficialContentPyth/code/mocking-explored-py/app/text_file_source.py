import logging

class TextFileSource:
    """
    A data source class that loads data from a text file and uses a processing function
    to transform or collect the data. Supports both single and dual parameter variants
    like the Java generic interface.
    """

    def load_data(self, fname, lines=None, line_processor=None):
        """
        Load data from a file using a line processor. Can be called with just the filename,
        or with a pre-initialized collection and processor function.

        Parameters:
        - fname: str - The filename to read.
        - collection: list or custom object - Optional collection to store results.
        - processor: function - Optional function of form (collection, line) -> collection

        Returns:
        - A list or processed object.
        """
        if lines is None:
            lines = []

        if line_processor is None:
            line_processor = lambda col, line: col + [line]  # Default: append line to list

        try:
            with open(fname, "r", encoding="utf-8") as file:
                for line in file:
                    lines = line_processor(lines, line.strip())
        except IOError as ex:
            logging.error("Error reading file %s: %s", fname, ex)

        return lines

