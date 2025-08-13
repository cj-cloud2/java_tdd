class HighestNumberFinder:
    def find_highest_number(self, numbers):
        """
        Finds the highest number in a list.

        Args:
            numbers (list): A list of integers.

        Returns:
            int: The highest number, or None if the list is empty.
        """
        if not numbers:
            return None  # Handles empty list gracefully

        highest_so_far = numbers[0]

        for val in numbers:
            if val > highest_so_far:
                highest_so_far = val

        return highest_so_far

