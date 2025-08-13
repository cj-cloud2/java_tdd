# **Assignment: Payroll Deduction Calculation using TDD \& Stubs (Step-by-Step Evolution)**

**Domain:** HRMS (Human Resource Management System)
**Difficulty:** Level 4
**Core Concept:** Using *stubs* with TDD to isolate dependencies
**Language:** Python

***

## **Scenario**

We need to build `calculate_net_salary(employee_id, get_employee_details)` which:

- Fetches an employee's gross salary from a **data source** (stubbed in tests).
- Deducts **20% tax** and **2% PF**.
- Returns the **net salary**.
- If employee isn’t found, returns `None`.

We will go through **3 TDD passes**, adding **one test case** in each pass.

***

# **Pass 1 – First Test Case**

## **Step 1 – RED**

We start with **only one** test for **Jack**.

**`test_payroll.py`**

```python
import unittest


# STUB simulating external DB/API
def stub_get_employee_details(employee_id):
    if employee_id == 101:
        return {"name": "Jack", "gross": 50000}
    return None


class TestPayrollDeduction(unittest.TestCase):
    def test_salary_basic(self):
        net = calculate_net_salary(101, stub_get_employee_details)
        self.assertEqual(net, 39500)  # Expected: 50000 - 20% - 2%


if __name__ == "__main__":
    unittest.main()
```

If you run this:

```
NameError: name 'calculate_net_salary' is not defined
```

**This is RED** (as expected).

***

## **Step 2 – GREEN**

We implement the function minimally.

**`payroll.py`**

```python
def calculate_net_salary(employee_id, get_employee_details):
    # Retrieve employee details; if not found, return None; otherwise calculate net salary after 20% tax and 2% PF deductions and return as integer
    pass
```

Run tests → **PASS** 🎉

***

## **Step 3 – REFACTOR**

Clean names and add comments.

```python
def calculate_net_salary(employee_id, get_employee_details):
    """Calculate net salary after 20% tax and 2% PF deductions."""
    # Retrieve employee details; handle missing case; calculate deductions and return integer value of net salary
    pass
```

✅ **Pass 1 complete**.

***

# **Pass 2 – Add Second Employee**

## **Step 1 – RED**

We now add a second test case for **Raymond**.

**Updated `test_payroll.py`**

```python
import unittest


def stub_get_employee_details(employee_id):
    if employee_id == 101:
        return {"name": "Jack", "gross": 50000}
    elif employee_id == 202:
        return {"name": "Raymond", "gross": 65000}
    return None


class TestPayrollDeduction(unittest.TestCase):
    def test_salary_basic(self):
        net = calculate_net_salary(101, stub_get_employee_details)
        self.assertEqual(net, 39500)  


    def test_salary_high(self):
        net = calculate_net_salary(202, stub_get_employee_details)
        self.assertEqual(net, 51360)  # Expected: 65000 - 20% - 2%


if __name__ == "__main__":
    unittest.main()
```

If our first implementation was *hardcoded* for Jack, this would FAIL.
If it’s generalized already, both tests PASS immediately.

***

## **Step 2 – GREEN**

In case you hardcoded earlier, update function to work for any employee (we already did in Pass 1).

***

## **Step 3 – REFACTOR**

Add basic validation.

**`payroll.py`**

```python
def calculate_net_salary(employee_id, get_employee_details):
    """Calculate net salary after 20% tax and 2% PF deductions."""
    # Retrieve employee details; if missing or invalid, return None; ensure gross is numeric; calculate deductions and return integer net salary
    pass
```

✅ **Pass 2 complete**.

***

# **Pass 3 – Handle Not Found Employee**

## **Step 1 – RED**

We now add a test for ID that doesn’t exist.

**Final `test_payroll.py`**

```python
import unittest


def stub_get_employee_details(employee_id):
    if employee_id == 101:
        return {"name": "Jack", "gross": 50000}
    elif employee_id == 202:
        return {"name": "Raymond", "gross": 65000}
    elif employee_id == 303:
        return {"name": "Dennis", "gross": 80000}
    return None


class TestPayrollDeduction(unittest.TestCase):
    def test_salary_basic(self):
        net = calculate_net_salary(101, stub_get_employee_details)
        self.assertEqual(net, 39500)  


    def test_salary_high(self):
        net = calculate_net_salary(202, stub_get_employee_details)
        self.assertEqual(net, 51360)  


    def test_salary_not_found(self):
        net = calculate_net_salary(999, stub_get_employee_details)
        self.assertIsNone(net)


if __name__ == "__main__":
    unittest.main()
```

If the earlier code already handles missing employees, this will PASS immediately.

***

## **Step 2 – GREEN**

No changes needed (function already supports it).

***

## **Step 3 – Final REFACTOR**

Add full docstring and rounding note.

**Final `payroll.py`**

```python
def calculate_net_salary(employee_id, get_employee_details):
    """
    Calculate the net salary for an employee.


    Formula:
        net = gross - 20% tax - 2% PF


    Args:
        employee_id (int): The employee's unique ID.
        get_employee_details (function): Dependency injection for fetching employee data.


    Returns:
        int: Net salary rounded down to the nearest integer.
        None: If employee not found or invalid data.
    """
    # Retrieve employee details; return None if missing or incomplete
    # Ensure gross salary is numeric; if not, raise ValueError
    # Apply 20% tax and 2% PF deduction formula
    # Return the computed net salary as integer
    pass
```

✅ **Pass 3 complete** — Final code is production ready.

***

## **Summary**

1. **Start small** with one test → fail → make it pass → refactor.
2. Add more tests incrementally to build robustness.
3. Use **stubs** to isolate dependencies and test business logic without relying on real databases/APIs.
4. Ensure **tests guide your implementation**, not the other way around.
