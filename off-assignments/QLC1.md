**Assignment 1 for v5-final** is a **full TDD walkthrough** that follows the **Red → Green → Refactor** cycle.

The idea is:

1. We start with **only the test file** (all tests fail).
2. We **implement the app class step-by-step** until all tests pass.
3. At each stage, we run tests, see failures, and fix them incrementally.

***

## **Extended Assignment 1 — v5-final with Full TDD Cycle**


***

### **Step 0 — Project Setup**

- Project structure:

```
app/
  highest_number_finder.py
tests/
  test_highest_number_finder.py
```

- Python version: **3.x or higher**.

***

## **Step 1 — RED Phase (Write failing tests)**

In `tests/test_highest_number_finder.py`
💡 At the start, **we have no code for HighestNumberFinder** in `app` yet.

```python
import unittest
from app.highest_number_finder import HighestNumberFinder

class TestHighestNumberFinder(unittest.TestCase):
    def test_find_highest_in_list_of_one_expect_single_item(self):
        numbers = [10]
        cut = HighestNumberFinder()
        expected_result = 10
        result = cut.find_highest_number(numbers)
        self.assertEqual(expected_result, result)

    def test_find_highest_in_list_of_two_descending_expect_first_element(self):
        numbers = [20, 10]
        expected_result = 20
        cut = HighestNumberFinder()
        result = cut.find_highest_number(numbers)
        self.assertEqual(result, expected_result)

    def test_find_highest_in_list_of_two_ascending_expect_second_element(self):
        numbers = [10, 20]
        expected_result = 20
        cut = HighestNumberFinder()
        result = cut.find_highest_number(numbers)
        self.assertEqual(result, expected_result)

    def test_find_highest_in_list_of_two_equals_expect_first_element(self):
        numbers = [10, 10]
        expected_result = 10
        cut = HighestNumberFinder()
        result = cut.find_highest_number(numbers)
        self.assertEqual(result, expected_result)

    def test_find_highest_in_list_of_several_items_expect_highest_element(self):
        numbers = [10, 20, 5, 17, 37, 14]
        expected_result = 37
        cut = HighestNumberFinder()
        result = cut.find_highest_number(numbers)
        self.assertEqual(result, expected_result)

if __name__ == '__main__':
    unittest.main()
```

Now, **run the tests**:

```bash
python -m unittest tests/test_highest_number_finder.py
```

🔥 **Result:**

```
ModuleNotFoundError: No module named 'app.highest_number_finder'
```

That’s expected — we don’t have the class yet.

***

## **Step 2 — GREEN Phase (Create Minimum Code to Pass First Test)**

Create `app/highest_number_finder.py`:

```python
class HighestNumberFinder:
    def find_highest_number(self, numbers):
        return 10
```

Run tests again:

```bash
python -m unittest tests/test_highest_number_finder.py
```

Result:

- **First test passes** ✅
- Others fail ❌ (wrong logic for multiple numbers)

***

## **Step 3 — Implement Logic for 2 Numbers Only**

Update `app/highest_number_finder.py`:

```python
class HighestNumberFinder:
    def find_highest_number(self, numbers):
        if not numbers:
            return None
        if len(numbers) == 1:
            return numbers[0]
        return numbers[0] if numbers[0] >= numbers[1] else numbers[1]
```

Run tests — expected:

- **First 4 tests** should pass
- **Last test (list of several items)** still fails ❌

***

## **Step 4 — Implement Logic for Any Number of Elements**

Now we write the **final version** (as given in v5-final):

```python
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
```


***

## **Step 5 — Final Test Run (GREEN)**

```bash
python -m unittest tests/test_highest_number_finder.py
```

✅ Expected Output:

```
.....
----------------------------------------------------------------------
Ran 5 tests in 0.000s

OK
```


***

## **Step 6 — REFACTOR (Optional)**

- Code already looks clean.
- Could improve small details like removing redundant loop condition — but functionality remains the same.

This completes the **TDD cycle**:

1. **RED:** Wrote failing tests first
2. **GREEN:** Wrote minimum working code step-by-step until all tests passed
3. **REFACTOR:** Optional improvements keeping tests green

***

### **What Students Learn**

- Writing tests before production code
- Running tests to see failures
- Incremental code improvement until all pass
- Avoiding over-engineering too early

***
