# Assignment: Dummy Design Pattern for Inventory Management using TDD (Step-by-Step Evolution)

**Domain:** Inventory Management
**Difficulty:** Level 3
**Core Concept:** Using *dummy objects* to satisfy dependencies without performing real operations.
**Language:** Python

***

## Scenario

You need to build:

```python
check_item_stock(item_id, get_item_data, audit_log)
```

It should:

1. Fetch **item details** from a data source (**stubbed** in tests).
2. Return **quantity in stock**.
3. Use a **dummy audit log object** to fulfill logging dependency in tests without actually logging.
4. If item not found, return `None`.

We’ll evolve via **3 TDD passes** — one new test case per pass.

***

## Pass 1 – First Test Case

### Step 1 – RED

We start with one item, e.g., **Emily’s Store – Item Laptop**.

**`test_inventory.py`**

```python
import unittest


class DummyAuditLog:
    def log(self, message):
        pass  # Does nothing (dummy)


# STUB simulating database/API
def stub_get_item_data(item_id):
    if item_id == 101:
        return {"item_name": "Laptop", "quantity": 25, "owner": "Emily"}
    return None


class TestInventory(unittest.TestCase):
    def test_item_quantity_basic(self):
        # Create a dummy log object and verify quantity for item ID 101
        # Call check_item_stock and validate the returned quantity matches expected
        
```

➡ Run it and get:

```
NameError: name 'check_item_stock' is not defined
```

**RED!**

***

### Step 2 – GREEN

Implement minimal code:

**`inventory.py`**

```python
def check_item_stock(item_id, get_item_data, audit_log):
    # Fetch the item details, log stock check, and return quantity if found, else return None
    pass
```

✅ Tests pass.

***

### Step 3 – REFACTOR

Add docstring and clarity:

```python
def check_item_stock(item_id, get_item_data, audit_log):
    """
    Retrieves current stock level for an item.


    Args:
        item_id (int): Unique identifier for the item.
        get_item_data (function): Function to fetch item info.
        audit_log (object): Object with a .log(msg) method.


    Returns:
        int or None: Quantity in stock, or None if item not found.
    """
    # Fetch the item details, log stock check, and return quantity if found, else return None
    pass
```

✅ **Pass 1 complete**.

***

## Pass 2 – Second Item

### Step 1 – RED

Add **Daniel’s Store – Item Mouse**.

```python
def stub_get_item_data(item_id):
    if item_id == 101:
        return {"item_name": "Laptop", "quantity": 25, "owner": "Emily"}
    elif item_id == 102:
        return {"item_name": "Mouse", "quantity": 150, "owner": "Daniel"}
    return None


class TestInventory(unittest.TestCase):
    def test_item_quantity_basic(self):
        # Create a dummy log object and verify quantity for item ID 101
        


    def test_item_quantity_second(self):
        # Create a dummy log object and verify quantity for item ID 102
        
```

Passes **only if function is generalised**, fails if hardcoded.

***

### Step 2 – GREEN

Our function already covers it — no change needed.

***

### Step 3 – REFACTOR

Add data validation:

```python
def check_item_stock(item_id, get_item_data, audit_log):
    # Fetch the item details; if missing or invalid, handle accordingly
    # Validate presence and type of quantity
    # Log the audit message and return quantity
    pass
```

✅ **Pass 2 complete**.

***

## Pass 3 – Handle Missing Item

### Step 1 – RED

Add test for item not found:

```python
    def test_item_not_found(self):
        # Create dummy log object and check for non-existing item ID
        # Assert that result is None
        
```


***

### Step 2 – GREEN

Function already handles missing items.

***

### Step 3 – Final REFACTOR

Final `inventory.py`:

```python
def check_item_stock(item_id, get_item_data, audit_log):
    """
    Retrieves current stock level for an item.


    Args:
        item_id (int): Unique item ID.
        get_item_data (function): Stubbed function returning dict with:
                                  "quantity" (int) and other item metadata.
        audit_log (object): Has .log(message) method.


    Returns:
        int: Quantity in stock if found.
        None: If not found or missing 'quantity'.
    """
    # Fetch the item details; if missing or 'quantity' key absent, return None
    # Validate that quantity is of integer type
    # Log the audit action
    # Return the quantity
    pass
```

✅ **Pass 3 done — production ready.**

***

## Summary

- We used **DummyAuditLog** to fulfill the logging dependency without actual logging.
- Tests used **stubs** for inventory data retrieval.
- Followed **TDD Red → Green → Refactor** pattern.
- Gradually added complexity while maintaining test pass status.

***

If you want, I can now give you the **fully runnable test suite and stub classes** so you can actually execute the TDD cycle step-by-step. Would you like me to prepare that?

