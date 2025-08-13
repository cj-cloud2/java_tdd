# Using Mocks in Python Unit Testing

## Overview

In unit testing, **Mocks** are powerful tools used to simulate and control the behavior of dependencies. Unlike **Stubs**, which return fixed results, **Mocks** can verify interactions and provide different return values depending on input or call sequence.

This project demonstrates the use of mocks to test the `TopicManager` class, which depends on an external `HighestNumberFinder` component to compute the highest score from a list of integers.

## Why Use Mocks?

Mocks provide the following advantages over stubs:

- **Test Decoupling**: Mocks eliminate the need for real implementations, enabling true unit ISOLATION.
- **Control**: Configure mocks to return specific values to simulate edge cases.
- **Verification**: Confirm that methods were called with expected parameters.
- **Speed**: Avoid delays or resource-heavy operations (like DB calls or file access).

## Example in This Project

### File: `test_topic_manager.py`

```python
from unittest.mock import Mock
```

We use `Mock()` from Python’s `unittest.mock` module to simulate the `HighestNumberFinder` dependency.

### Mock Configuration

```python
hnf_mock = Mock()
hnf_mock.find_highest_number.side_effect = [89, 87, 97]
```

This mock is configured to return:
- `89` for the first topic ("Physics"),
- `87` for the second ("Art"),
- `97` for the third ("Comp Sci").

Each return corresponds to a call to `find_highest_number`.

### Injecting the Mock

```python
cut = TopicManager(hnf_mock)
```

The `TopicManager` is initialized with the mock dependency instead of a real `HighestNumberFinder` object.

### Asserting the Output

The test verifies that the `TopicManager` correctly incorporates mock results into its returned `TopicTopScore` list:

```python
self.assertEqual(result[i].get_topic_name(), expected_result[i].get_topic_name())
self.assertEqual(result[i].get_top_score(), expected_result[i].get_top_score())
```

## Comparison to Stubs

| Feature            | Stub                        | Mock                                  |
|--------------------|-----------------------------|---------------------------------------|
| Return Value       | Hardcoded (e.g. always 89)  | Configurable per call (`side_effect`) |
| Behavior Control   | Static                      | Dynamic                               |
| Verifies Calls     | ❌ No                       | ✅ Yes                                |
| Better Isolation   | ❌ Limited                  | ✅ Strong                             |

## Conclusion

Mocks are ideal when:
- You want different behaviors on subsequent calls.
- You need to verify interactions with dependencies.
- You want flexible, isolated unit testing.

This project uses mocks in the `test_find_highest_score_with_list_of_many_returns_list_of_many_using_mocks` test case to demonstrate how mocks lead to more expressive and precise tests than stubs.
