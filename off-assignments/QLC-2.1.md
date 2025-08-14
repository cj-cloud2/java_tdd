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
### END ###
*** ================================================================================================================= 
