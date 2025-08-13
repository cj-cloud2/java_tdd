class BasicDataProcessor:
    """
    BasicDataProcessor processes data from a provided data source
    and calculates the total number of characters across all lines.
    """

    def __init__(self, data_source):
        self.data_source = data_source

    def load_data(self, fname):
        data = self.data_source.load_data(fname)
        count = sum(len(line) for line in data)
        return count

