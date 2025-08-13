class HighestNumberFinder:
    def find_highest_number(self, numbers):
        """
        Returns the highest number from the list of at most 2 elements.
        """
        if not numbers:
            return None 

        highest_so_far = numbers[0]

        if len(numbers) > 1 and numbers[1] > highest_so_far:
            highest_so_far = numbers[1]

        return highest_so_far

