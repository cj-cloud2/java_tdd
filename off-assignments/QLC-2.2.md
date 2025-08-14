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
### END ###
*** ================================================================================================================= 

