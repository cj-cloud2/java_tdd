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
### END ###
*** ================================================================================================================= 
