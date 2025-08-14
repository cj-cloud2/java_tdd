*** ================================================================================================================= 
### QL 4.1 ###
*** ================================================================================================================= 


# 🧑💻 Guided TDD Assignment — Step-by-Step with Full Code Copies

Build your FileLoader module "the TDD way":

- Start with an almost empty implementation
- Run one test
- Make it pass
- Move to the next test
- Copy all relevant code as described
- **Every file you need is printed in each step!**

***

## 📋 **Project Structure** (for reference)

```
tdd_file_loader/
├── app/
│   ├── file_loader.py
│   └── __init__.py
├── tests/
│   ├── test_file_loader.py
│   ├── test_live_file_loader.py
│   └── __init__.py
├── main.py
```


***

## 🚦 STEP 0: Initial Scaffold

**Copy and create these files in your IDE as shown below. Start with a minimal skeleton for `app/file_loader.py`, and the exact test files as provided.**

### app/file_loader.py

```python
# --------------------------------------
# app/file_loader.py (EMPTY SKELETON)
# --------------------------------------

class FileLoader:
    pass  # We'll build this step by step!
```


### app/__init__.py

```python
# empty file
```


### tests/test_file_loader.py

```python
import unittest
from app.file_loader import FileLoader

class TestFileLoader(unittest.TestCase):
    print("Empty class")

if __name__ == '__main__':
    unittest.main()
```


### tests/__init__.py

```python
# empty file
```




### main.py

```python
def main():
    print("Hello world")

if __name__ == "__main__":
    main()
```


***

## 🚦 **STEP 1:** Make the first test fail, then pass



### Add first test to **tests/test_file_loader.py

### tests/test_file_loader.py

```python
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

   

if __name__ == '__main__':
    unittest.main()
```




**Run the tests now:**

```sh
python -m unittest tests/test_file_loader.py
```

*(All tests will fail — that's "Red")*

### Now, **add just enough code to pass the FIRST test** ("test_load_all_of_file_using_inbuilt_files_type_as_lambda")

**Replace your `app/file_loader.py` with:**

```python
# --------------------------------------
# app/file_loader.py (First test: make it pass)
# --------------------------------------

class FileLoader:
    def __init__(self, file_to_load):
        self.file_to_load = file_to_load
        self.lines = []

    def load_file_with_func(self, func):
        self.lines = func(self.file_to_load)
        return sum(len(line) for line in self.lines)
```

**Now run again:**

```sh
python -m unittest tests/test_file_loader.py
```

- The **first test will pass, the rest will fail.**

***
*** ================================================================================================================= 
### END ###
*** ================================================================================================================= 




*** ================================================================================================================= 
### QL 4.2 ###
*** ================================================================================================================= 

## 🚦 **STEP 2:** Make the second test pass

### Now, **add second test to *tests/test_file_loader.py

### tests/test_file_loader.py

```python
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

if __name__ == '__main__':
    unittest.main()
```

**The next test ("test_load_all_of_file_via_stub") uses the same FileLoader API. Run again:**

```sh
python -m unittest tests/test_file_loader.py
```

- This test should **also pass, no changes needed!**

***
*** ================================================================================================================= 
### END ###
*** ================================================================================================================= 




*** ================================================================================================================= 
### QL 4.3 ###
*** ================================================================================================================= 

## 🚦 **STEP 3:** Make the third test pass

### Now, **add second test to *tests/test_file_loader.py


### tests/test_file_loader.py

```python
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
```


**The third test ("test_load_all_of_file_using_mock") uses a lambda with a fake/mock object. Run yet again:**

```sh
python -m unittest tests/test_file_loader.py
```

- This test should **also pass; your method is flexible enough.**

***

## ✨ Refactor (if desired)

All tests are passing. For clarity and maintainability, you can refactor with a private `calculate_file_size` helper. Here’s what the **final version** from your supplied code looks like (nothing to edit as it's already clean):

### app/file_loader.py

```python
"""
FileLoader Module

This module defines the FileLoader class which is responsible for reading a text file
and calculating the total size (in characters) of its contents. It also supports
dependency injection through the `load_file_with_func` method, allowing testability
without relying on actual file I/O operations.

This is useful in unit testing scenarios where file system access should be avoided.
"""

class FileLoader:
    def __init__(self, file_to_load):
        self.file_to_load = file_to_load
        self.lines = []

    def load_file(self, fname):
        """
        Loads a file from disk and reads its contents line by line.
        Falls back to an empty list if the file cannot be read.
        """
        try:
            with open(fname, encoding='utf-8') as f:
                self.lines = f.readlines()
        except IOError:
            self.lines = []
        return self._calculate_file_size()

    def get_lines(self):
        """Returns the list of lines read from the file."""
        return self.lines

    def load_file_with_func(self, func):
        """
        Accepts a file loading function to inject lines, used primarily for testing.

        This avoids direct I/O operations and makes the method more testable by passing
        a mock or simulated version of file loading logic.
        """
        self.lines = func(self.file_to_load)
        return self._calculate_file_size()

    def _calculate_file_size(self):
        return sum(len(line) for line in self.lines)
```


***
*** ================================================================================================================= 
### END ###
*** ================================================================================================================= 




*** ================================================================================================================= 
### QL 4.4 ###
*** ================================================================================================================= 

## 🚦 **STEP 4:** Add the "Live FileLoader" test and implementation

Now, add `tests/test_live_file_loader.py` exactly as supplied:

### tests/test_live_file_loader.py

```python
import unittest
from app.file_loader import FileLoader

class TestLiveFileLoader(unittest.TestCase):
    """
    The initial design is described in this test.

    The weakness should be obvious — the file to be loaded and its location.
    I use a shared network drive to run this code from different machines, normally
    developing from a PC. When I ran the code on the laptop from a cafe it
    immediately failed because the C: on the laptop was completely different
    to the PC, so the original file C:/tmp/KeyboardHandler.txt did not exist.

    THIS IS A GREAT EXAMPLE OF WHY THE UNIT TEST AND CUT SHOULD NOT BE STRONGLY
    LINKED TO ANY IO — NETWORK, DB, AND FILE SYSTEM.
    """

    def test_load_all_of_file_using_inbuilt_files_type(self):
        # Arrange
        file_to_load = "c:/tmp/KeyboardHandler-py.txt"
        expected_bytes_read = 1383
        cut = FileLoader(file_to_load)

        # Act
        bytes_read = cut.load_file(file_to_load)

        # Assert
        self.assertEqual(expected_bytes_read, bytes_read)

if __name__ == "__main__":
    unittest.main()
```

Run:

```sh
python -m unittest tests/test_live_file_loader.py
```

- This will pass **if the file exists** at the specified location and matches the expected content length!

***

## Final Files — What you should have in your IDE

**Here is the FULL content for every file, all at once. Copy and paste for final check:**

### app/file_loader.py

*(See above: the finished version with all methods as per supplied context)*

### tests/test_file_loader.py

*(See above: three tests for lambda, stub, and mock file loading)*

### tests/test_live_file_loader.py

*(See above: IO-dependent test for the real file)*

### app/__init__.py

```python
# (empty)
```


### tests/__init__.py

```python
# (empty)
```


### main.py

```python
def main():
    print("Hello world")

if __name__ == "__main__":
    main()
```


***

## 🧪 To run all tests:

```sh
python -m unittest discover -s tests
```

or in IPython:

```python
!python -m unittest discover -s tests
```


***

### 📚 Reference and Documentation Files

If you want, add these to your IDE for extra learning/explaining:

- README.md (overview, project structure, design notes)
- app/README_File_Loader.md (explains dependency injection for tests)
- README_Test_Doubles.md (explains stubs/mocks/fakes)

***

# 🎉 Done!

You have built a fully TDD FileLoader module and test suite using only the exact code from the context, presented step-by-step and ready for copy/pasting at each stage.
**Every code block above matches the original context exactly and shows you the right order to paste when following TDD.**

Happy TDDing! 🚦🐍

*** ================================================================================================================= 
### END ###
*** ================================================================================================================= 