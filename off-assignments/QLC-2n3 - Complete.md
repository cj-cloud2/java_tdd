*** ================================================================================================================= 
### QLC 2.1 ###
*** ================================================================================================================= 



# 🚦 PyTDD Workshop: Topic Manager — Red → Green → Refactor (with All Code)

## Directory Structure

```text
topic-manager/
  app/
    __init__.py
    topic_manager.py
    topic_top_score.py
    topic_scores.py
    topic_score_writer.py
    file_writer.py
  tests/
    __init__.py
    test_topic_manager.py
    test_topic_score_writer.py
main.py
```


***

## **Part 1 — `tests/test_topic_manager.py`**

### **Step 1: Test for Empty List**

#### 1.1 **File Setup**

Create the files as above (all empty except as coded below).

#### 1.2 **Write First Test Only**

_Copy this entire content as `tests/test_topic_manager.py`:_

```python
import unittest
from app.topic_manager import TopicManager

class TopicManagerTest(unittest.TestCase):
    def test_find_highest_score_in_empty_array_returns_empty_list(self):
        # Arrange
        scores = []  # List[TopicScores]
        cut = TopicManager()
        expected_result = []

        # Act
        result = cut.find_topic_high_scores(scores)

        # Assert
        self.assertEqual(result, expected_result)

if __name__ == "__main__":
    unittest.main()
```


#### 1.3 **Initial Implementation**

Create `app/topic_manager.py`:

```python
class TopicManager:
    def find_topic_high_scores(self, topic_scores_list):
        return []
```


#### 1.4 **Run the Test**

```bash
python -m unittest tests/test_topic_manager.py
```

**Result:** ✅ Green! All good for one test.

***

### **Step 2: Test for One TopicScores Item**

#### 2.1 **Add Test to Existing File**

Replace `tests/test_topic_manager.py` with:

```python
import unittest
from app.topic_manager import TopicManager
from app.topic_top_score import TopicTopScore
from app.topic_scores import TopicScores

class TopicManagerTest(unittest.TestCase):
    def test_find_highest_score_in_empty_array_returns_empty_list(self):
        # Arrange
        scores = []  # List[TopicScores]
        cut = TopicManager()
        expected_result = []

        # Act
        result = cut.find_topic_high_scores(scores)

        # Assert
        self.assertEqual(result, expected_result)

    def test_find_highest_score_with_list_of_one_returns_list_of_one(self):
        # Arrange
        scores = [56, 67, 45, 89]
        topic_name = "Physics"
        topic_scores = [TopicScores(topic_name, scores)]

        cut = TopicManager()
        expected_result = [TopicTopScore(topic_name, 89)]

        # Act
        result = cut.find_topic_high_scores(topic_scores)

        # Assert
        self.assertEqual(result[^0].get_topic_name(), expected_result[^0].get_topic_name())
        self.assertEqual(result[^0].get_top_score(), expected_result[^0].get_top_score())

if __name__ == "__main__":
    unittest.main()
```


#### 2.2 **Implement Classes and Update Logic**

_Update/add all of these files:_

**`app/topic_scores.py`**

```python
class TopicScores:
    def __init__(self, topic_name, scores):
        self._topic_name = topic_name
        self._scores = scores

    def get_topic_name(self):
        return self._topic_name

    def get_scores(self):
        return self._scores
```

**`app/topic_top_score.py`**

```python
class TopicTopScore:
    def __init__(self, topic_name, score):
        self._topic_name = topic_name
        self._top_score = score

    def get_topic_name(self):
        return self._topic_name

    def get_top_score(self):
        return self._top_score

    def __eq__(self, other):
        if not isinstance(other, TopicTopScore):
            return False
        return (self._topic_name.lower() == other._topic_name.lower()
                and self._top_score == other._top_score)

    def __repr__(self):
        return f"TopicTopScore(topic_name='{self._topic_name}', top_score={self._top_score})"
```

**Update `app/topic_manager.py` as follows:**

```python
from app.topic_top_score import TopicTopScore

class TopicManager:
    def find_topic_high_scores(self, topic_scores_list):
        if not topic_scores_list:
            return []
        ts = topic_scores_list[^0]
        highest = max(ts.get_scores())  # finds the highest score in that topic
        return [TopicTopScore(ts.get_topic_name(), highest)]
```

*(Was: `return []`) now finds and returns correct TopicTopScore.*

#### 2.3 **Run Tests**

```bash
python -m unittest tests/test_topic_manager.py
```

**Result:** ✅ Both first and second tests pass.

*** ================================================================================================================= 
### QLC 2.2 ###
*** ================================================================================================================= 


### **Step 3: Test Dependency Injection With Stub**

#### 3.1 **Add Third Test**

Replace/add to `tests/test_topic_manager.py` (add import and class at top!):

```python
import unittest
from app.topic_manager import TopicManager
from app.topic_top_score import TopicTopScore
from app.topic_scores import TopicScores

class HighestNumberFinderStub:
    def find_highest_number(self, numbers):
        return 89  # Always returns the expected max for testing

class TopicManagerTest(unittest.TestCase):
    def test_find_highest_score_in_empty_array_returns_empty_list(self):
        # ...as above...

    def test_find_highest_score_with_list_of_one_returns_list_of_one(self):
        # ...as above...

    def test_find_highest_score_with_one_topic_using_stub(self):
        # Arrange
        scores = [56, 67, 45, 89]
        topic_name = "Physics"
        topic_scores_list = [TopicScores(topic_name, scores)]

        hnf_stub = HighestNumberFinderStub()
        cut = TopicManager(hnf_stub)

        expected_result = [TopicTopScore(topic_name, 89)]

        # Act
        result = cut.find_topic_high_scores(topic_scores_list)

        # Assert
        self.assertEqual(result[^0].get_topic_name(), expected_result[^0].get_topic_name())
        self.assertEqual(result[^0].get_top_score(), expected_result[^0].get_top_score())

if __name__ == "__main__":
    unittest.main()
```


#### 3.2 **Update `app/topic_manager.py` to Allow Dependency Injection**

Old:

```python
from app.topic_top_score import TopicTopScore

class TopicManager:
    def find_topic_high_scores(self, topic_scores_list):
        if not topic_scores_list:
            return []
        ts = topic_scores_list[^0]
        highest = max(ts.get_scores())  # finds the highest score in that topic
        return [TopicTopScore(ts.get_topic_name(), highest)]
```

New (**replace with this version**):

```python
from app.topic_top_score import TopicTopScore

class TopicManager:
    def __init__(self, highest_number_finder=None):
        self._highest_number_finder = highest_number_finder

    def find_topic_high_scores(self, topic_scores_list):
        if not topic_scores_list:
            return []
        ts = topic_scores_list[^0]
        if self._highest_number_finder:
            highest = self._highest_number_finder.find_highest_number(ts.get_scores())
        else:
            highest = max(ts.get_scores())
        return [TopicTopScore(ts.get_topic_name(), highest)]
```

- **New:** Constructor accepts an injected dependency (stub, mock, or real).
- **Change:** Now uses the stub if provided!


#### 3.3 **Run Tests**

```bash
python -m unittest tests/test_topic_manager.py
```

**Result:** ✅ Three tests pass!

***

*** ================================================================================================================= 
### QLC 2.3 ###
*** ================================================================================================================= 



### **Step 4: Test Multiple Topics With Stub**

#### 4.1 **Add Fourth Test**

Replace (or extend, if appending) `tests/test_topic_manager.py`:

```python
# ...previous code...
class HighestNumberFinderStub:
    def find_highest_number(self, numbers):
        return 89  # Always returns 89

class TopicManagerTest(unittest.TestCase):
    # ...previous tests...

    def test_find_highest_score_with_list_of_many_returns_list_of_many_using_stub(self):
        # Arrange
        physics_scores = [56, 67, 45, 89]
        art_scores = [87, 66, 78]
        compsci_scores = [45, 88, 97, 56]

        topic_scores = [
            TopicScores("Physics", physics_scores),
            TopicScores("Art", art_scores),
            TopicScores("Comp Sci", compsci_scores)
        ]

        expected_result = [
            TopicTopScore("Physics", 89),
            TopicTopScore("Art", 89),
            TopicTopScore("Comp Sci", 89)
        ]

        cut = TopicManager(HighestNumberFinderStub())

        # Act
        result = cut.find_topic_high_scores(topic_scores)

        # Assert
        for i in range(len(expected_result)):
            self.assertEqual(result[i].get_topic_name(), expected_result[i].get_topic_name())
            self.assertEqual(result[i].get_top_score(), expected_result[i].get_top_score())
```


#### 4.2 **Update `app/topic_manager.py` to Loop Over All Topics**

Old:

```python
class TopicManager:
    def __init__(self, highest_number_finder=None):
        self._highest_number_finder = highest_number_finder

    def find_topic_high_scores(self, topic_scores_list):
        if not topic_scores_list:
            return []
        ts = topic_scores_list[^0]
        if self._highest_number_finder:
            highest = self._highest_number_finder.find_highest_number(ts.get_scores())
        else:
            highest = max(ts.get_scores())
        return [TopicTopScore(ts.get_topic_name(), highest)]
```

New (**replace entire code**):

```python
from app.topic_top_score import TopicTopScore

class TopicManager:
    def __init__(self, highest_number_finder=None):
        self._highest_number_finder = highest_number_finder

    def find_topic_high_scores(self, topic_scores_list):
        if not topic_scores_list:
            return []
        top_scores = []
        for ts in topic_scores_list:
            if self._highest_number_finder:
                highest = self._highest_number_finder.find_highest_number(ts.get_scores())
            else:
                highest = max(ts.get_scores())
            top_scores.append(TopicTopScore(ts.get_topic_name(), highest))
        return top_scores
```


#### 4.3 **Run Tests**

```bash
python -m unittest tests/test_topic_manager.py
```

**Result:** ✅ Four tests now pass!

***




*** ================================================================================================================= 
### QLC 2.4 ###
*** ================================================================================================================= 

### **Step 5: Test With Mocks (Different Result Per Topic)**

#### 5.1 **Add Fifth Test**

Add to `tests/test_topic_manager.py` (also import Mock):

```python
from unittest.mock import Mock

class TopicManagerTest(unittest.TestCase):
    # ...previous tests...

    def test_find_highest_score_with_list_of_many_returns_list_of_many_using_mocks(self):
        # Arrange
        physics_scores = [56, 67, 45, 89]
        art_scores = [87, 66, 78]
        compsci_scores = [45, 88, 97, 56]
        topic_scores = [
            TopicScores("Physics", physics_scores),
            TopicScores("Art", art_scores),
            TopicScores("Comp Sci", compsci_scores)
        ]

        # Create mock for HighestNumberFinder
        hnf_mock = Mock()
        hnf_mock.find_highest_number.side_effect = [89, 87, 97]

        # System under test
        cut = TopicManager(hnf_mock)

        # Expected results
        expected_result = [
            TopicTopScore("Physics", 89),
            TopicTopScore("Art", 87),
            TopicTopScore("Comp Sci", 97)
        ]

        # Act
        result = cut.find_topic_high_scores(topic_scores)

        # Assert
        for res, exp in zip(result, expected_result):
            self.assertEqual(res.get_topic_name(), exp.get_topic_name())
            self.assertEqual(res.get_top_score(), exp.get_top_score())
```

**No change needed to `topic_manager.py` if Step 4's full code is present.**

#### 5.2 **(If needed) Final `app/topic_manager.py`:**

```python
from app.topic_top_score import TopicTopScore

class TopicManager:
    def __init__(self, highest_number_finder=None):
        self._highest_number_finder = highest_number_finder

    def find_topic_high_scores(self, topic_scores_list):
        if not topic_scores_list:
            return []
        top_scores = []
        for ts in topic_scores_list:
            if self._highest_number_finder:
                highest = self._highest_number_finder.find_highest_number(ts.get_scores())
            else:
                highest = max(ts.get_scores())
            top_scores.append(TopicTopScore(ts.get_topic_name(), highest))
        return top_scores
```


#### 5.3 **Run Tests**

```bash
python -m unittest tests/test_topic_manager.py
```

**Result:** ✅ Green for all five tests in `test_topic_manager.py`!


*** ================================================================================================================= 
### QLC 3 ###
*** ================================================================================================================= 


## **Part 2 — `tests/test_topic_score_writer.py`**

### **Step 6: Test TopicScoreWriter Writes Correct Line**

#### 6.1 **Create/Add Test File**

**`tests/test_topic_score_writer.py`:**

```python
import unittest
from unittest.mock import Mock
from app.topic_score_writer import TopicScoreWriter
from app.topic_top_score import TopicTopScore

class TopicScoreWriterTest(unittest.TestCase):
    def test_verify_topic_score_details_written_out_once(self):
        # Arrange
        physics = "Physics"
        expected_result = "Physics, 89"
        top_scores = [TopicTopScore(physics, 89)]

        mock_file_writer = Mock()
        cut = TopicScoreWriter(mock_file_writer)

        # Act
        cut.write_scores(top_scores)

        # Assert
        mock_file_writer.write_line.assert_called_once_with(expected_result)

if __name__ == '__main__':
    unittest.main()
```


#### 6.2 **Implement `app/topic_score_writer.py`:**

```python
from app.file_writer import FileWriter

class TopicScoreWriter:
    def __init__(self, file_writer):
        self._file_writer = file_writer

    def write_scores(self, top_scores, filename="output.txt"):
        if top_scores:
            tts = top_scores[^0]
            data_to_write = f"{tts.get_topic_name()}, {tts.get_top_score()}"
            self._file_writer.write_line(data_to_write, filename)
```


#### 6.3 **Make Sure `app/file_writer.py` Exists (used for integration, but not needed for this mock test):**

```python
class FileWriter:
    def write_line(self, line, filename="output.txt"):
        with open(filename, 'w') as file:
            file.write(line)
```


#### 6.4 **Run the Test**

```bash
python -m unittest tests/test_topic_score_writer.py
```

**Result:** ✅ Pass!

***

### 💡 **Now You Can…**

- Run all tests:

```bash
python -m unittest
```

or

```python
!python -m unittest
```

in IPython, always from `topic-manager` directory.

***

## 🎓 **Recap — You Learned:**

- Adding tests one by one (Red)
- Incrementally updating implementation (Green)
- Keeping code logically clean (Refactor)
- Using dependency injection for testability
- Using stubs and mocks for isolated testing

***
