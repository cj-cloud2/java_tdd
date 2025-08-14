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
*** ================================================================================================================= 
### END ###
*** ================================================================================================================= 

