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
### END ###
*** ================================================================================================================= 
