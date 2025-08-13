from app.highest_number_finder import HighestNumberFinder

def main():
    numbers = [10]
    finder = HighestNumberFinder()
    result = finder.find_highest_number(numbers)
    print(f"The highest number is: {result}")

if __name__ == "__main__":
    main()

