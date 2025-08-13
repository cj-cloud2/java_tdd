from app.highest_number_finder import HighestNumberFinder

def main():
    # Example list
    numbers = [10, 10]

    # Create an instance of the class
    finder = HighestNumberFinder()

    # Find the highest number
    highest = finder.find_highest_number(numbers)

    # Output the result
    print(f"The highest number is: {highest}")

if __name__ == "__main__":
    main()

