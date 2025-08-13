
# Limitations of Using Stubs in Python Testing

This document explains the limitations of using **stub classes** in unit testing, particularly based on the `HighestNumberFinderStub` used in the Topic Manager example.

---

## ⚠️ Limitations

### 1. Hardcoded Return Values
- The stub always returns a fixed value (e.g., `89`).
- It does not actually calculate anything, so bugs in the real implementation will go unnoticed.

### 2. Lack of Edge Case Handling
- The stub doesn't deal with different kinds of inputs (e.g., empty lists, negative numbers).
- Your tests will pass even if the real logic would fail on those cases.

### 3. Cannot Verify Interactions
- Stubs do not track how they were used.
- You can't confirm whether the method was called, how many times, or with what parameters.
- For this, you need a **mock** object (e.g., from Python's `unittest.mock`).

### 4. Maintenance Overhead
- If the real class interface changes, the stub might need to be updated to stay compatible.
- Otherwise, your test might pass with an invalid assumption.

### 5. Overconfidence in Integration
- Passing stub-based tests can create a false sense of security.
- The integration between classes isn't really tested until the real implementation is used.

---

## ✅ When to Use Stubs

- When you want to isolate the component you're testing from complex dependencies.
- When the real implementation is slow, unreliable, or not yet implemented.
- When you need predictable, fixed behavior during tests.

---

## 🧪 Best Practices

- Use **stubs** for isolated unit tests.
- Use **mocks** to test how your code interacts with its dependencies.
- Use **integration tests** to validate behavior with real components.


