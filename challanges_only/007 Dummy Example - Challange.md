# Assignment: Dummy Design Pattern using TDD \& Stubs (Step-by-Step Evolution)

**Domain:** Finance
**Difficulty:** Level 3
**Core Concept:** Using *dummy objects* to satisfy parameter requirements in tests
**Language:** Python

***

## Scenario

Build `process_bank_transaction(transaction_id, get_transaction_data, audit_log)` to:

- Fetch transaction details from a data source (**stubbed** in tests).
- Process transaction amount (e.g., return the amount for now).
- Use a dummy audit log object to fulfill the logging dependency in tests without actual logging.
- Return the transaction amount.
- If transaction isn’t found, return `None`.

We'll evolve our solution through **3 TDD passes**, adding *one test case* per pass.

***

# Pass 1 – First Test Case

## Step 1 – RED

Start with a single test for transaction of **Sandra**.

**`test_banking.py`**

```python
import unittest


class DummyAuditLog:
    def log(self, message):
        pass  # Dummy method does nothing


# STUB simulating external DB/API
def stub_get_transaction_data(transaction_id):
    if transaction_id == 1001:
        return {"account_holder": "Sandra", "amount": 1500}
    return None


class TestBanking(unittest.TestCase):
    def test_transaction_basic(self):
        # Create dummy log, call process_bank_transaction with transaction id 1001 and assert the returned amount is 1500
        pass


if __name__ == "__main__":
    unittest.main()
```

If you run this:

```
NameError: name 'process_bank_transaction' is not defined
```

**RED!**

***

## Step 2 – GREEN

Implement the function.

**`banking.py`**

```python
def process_bank_transaction(transaction_id, get_transaction_data, audit_log):
    # Fetch transaction details using provided function, log the transaction, and return the amount if available, otherwise return None
    pass
```

Run tests → **PASS** 🎉

***

## Step 3 – REFACTOR

Clean up and add descriptive comments.

```python
def process_bank_transaction(transaction_id, get_transaction_data, audit_log):
    """
    Process a bank transaction by fetching its details and logging the event.


    Args:
        transaction_id (int): Unique identifier for the transaction.
        get_transaction_data (function): Function to fetch transaction details.
        audit_log (object): Object with a .log(msg) method for auditing.


    Returns:
        int or None: Transaction amount if found; None otherwise.
    """
    # Fetch transaction details, log the outcome, and return the amount if found, else return None
    pass
```

✅ **Pass 1 done**.

***

# Pass 2 – Add Second Transaction

## Step 1 – RED

Add a second test case for transaction of **Matthew**.

**Updated `test_banking.py`**

```python
import unittest


class DummyAuditLog:
    def log(self, message):
        pass


def stub_get_transaction_data(transaction_id):
    if transaction_id == 1001:
        return {"account_holder": "Sandra", "amount": 1500}
    elif transaction_id == 1002:
        return {"account_holder": "Matthew", "amount": 3200}
    return None


class TestBanking(unittest.TestCase):
    def test_transaction_basic(self):
        # Create dummy log and verify that transaction id 1001 returns amount 1500
        pass


    def test_transaction_second(self):
        # Create dummy log and verify that transaction id 1002 returns amount 3200
        pass


if __name__ == "__main__":
    unittest.main()
```

If function was hardcoded earlier, this will fail. Otherwise, passes.

***

## Step 2 – GREEN

Function remains as implemented—already generalized.

***

## Step 3 – REFACTOR

Add validation for transaction data and types.

```python
def process_bank_transaction(transaction_id, get_transaction_data, audit_log):
    # Fetch transaction; if missing or amount not present, return None
    # Check if amount is numeric; if not, raise ValueError
    # Log processing message and return amount
    pass
```

✅ **Pass 2 done**.

***

# Pass 3 – Handle Not Found Transaction

## Step 1 – RED

Add a test for transaction ID that doesn’t exist.

**Final `test_banking.py`**

```python
import unittest


class DummyAuditLog:
    def log(self, message):
        pass


def stub_get_transaction_data(transaction_id):
    if transaction_id == 1001:
        return {"account_holder": "Sandra", "amount": 1500}
    elif transaction_id == 1002:
        return {"account_holder": "Matthew", "amount": 3200}
    elif transaction_id == 1003:
        return {"account_holder": "Ashley", "amount": 2700}
    return None


class TestBanking(unittest.TestCase):
    def test_transaction_basic(self):
        # Create dummy log and check that transaction id 1001 returns amount 1500
        pass


    def test_transaction_second(self):
        # Create dummy log and check that transaction id 1002 returns amount 3200
        pass


    def test_transaction_not_found(self):
        # Create dummy log and check that a non-existent transaction id returns None
        pass


if __name__ == "__main__":
    unittest.main()
```

Will pass if missing transaction handled properly.

***

## Step 2 – GREEN

No change needed if already handling missing transactions.

***

## Step 3 – Final REFACTOR

Add complete docstring and validations.

**Final `banking.py`**

```python
def process_bank_transaction(transaction_id, get_transaction_data, audit_log):
    """
    Process a bank transaction by fetching its details and logging the event.


    Args:
        transaction_id (int): Unique ID of the transaction.
        get_transaction_data (function): Function returning dict:
                                         "amount" (float/int).
        audit_log (object): Object with a .log(msg) method.


    Returns:
        int: Transaction amount rounded down if numeric.
        None: If transaction not found or missing amount.
    """
    # Fetch transaction data, handle missing case, validate numeric amount, log the processing, and return integer amount
    pass
```

✅ **Pass 3 done — production ready.**

***

## Summary

1. Start with one test case (RED) → implement code (GREEN) → clean up (REFACTOR).
2. Introduce dummy objects for dependencies not under test (like audit logs).
3. Gradually add more test cases and refactor code.
4. Handle missing data gracefully and validate types strictly.

