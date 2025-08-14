## **Assignment 2 — Credit Score Integration**

### **Service goal:**

Incorporate applicant credit score for eligibility.

### **Test Cases:**

1. Accept application if credit score is above threshold (**mock credit bureau**).
2. Reject application for low credit score.
3. Handle failure to fetch credit score (**mock failure or timeout**).

### **After this step:**

Our service now calls an external credit score provider before persistence.

***

## **📂 Updated Project Structure**

```
loan_app/
│
├── loan_service/
│   ├── __init__.py
│   ├── models.py
│   ├── repository.py
│   ├── credit_service.py   # NEW in Assignment 2
│   └── service.py
│
├── tests/
│   ├── __init__.py
│   ├── test_service.py     # Assignment 1 tests (unchanged)
│   └── test_service_credit.py   # NEW tests for Assignment 2
│
└── main.py
```


***

## **3️⃣ tests/test_service_credit.py** (NEW Unit Tests for Assignment 2)

```python
import unittest
from unittest.mock import Mock
from loan_service.service import LoanProcessingService
from loan_service.models import LoanApplication
from loan_service.repository import LoanRepository
from loan_service.credit_service import CreditScoreService


class LoanProcessingServiceCreditTest(unittest.TestCase):


    def setUp(self):
        # Create mock repository
        # Create mock credit score service
        # Initialize LoanProcessingService with these mocks


    def test_accept_when_good_credit_score(self):
        # Create application with valid fields
        # Configure mock credit service to return high score (> threshold)
        # Call accept_application and store result
        # Assert approval (True) and verify save method called once with app


    def test_reject_when_low_credit_score(self):
        # Create application with valid fields
        # Configure mock credit service to return low score (< threshold)
        # Call accept_application
        # Assert rejection (False) and verify save not called


    def test_handle_credit_service_failure(self):
        # Create valid loan application
        # Configure credit service mock to raise an Exception simulating failure
        # Call accept_application
        # Assert rejection (False) and verify save not called


if __name__ == "__main__":
    # Run the unit tests when script is executed directly
```


***

## **1️⃣ loan_service/credit_service.py** (NEW)

```python
# ========================
# Assignment 2 — New Code
# ========================


class CreditScoreService:
    """Interface to fetch applicant's credit score."""
    def get_credit_score(self, applicant_name: str) -> int:
        # Abstract method to obtain credit score — implemented in real service or mocked in tests
```


***

## **2️⃣ loan_service/service.py** (Updated for Assignment 2)

```python
from .models import LoanApplication
from .repository import LoanRepository
from .credit_service import CreditScoreService  # NEW


class LoanProcessingService:
    """
    Loan processing service — extended for Assignment 2.
    Now includes a credit score eligibility check.
    """


    def __init__(self, repository: LoanRepository, credit_service: CreditScoreService = None):
        # Store provided repository
        # Store provided credit score service (may be None if feature not used)


    def accept_application(self, application: LoanApplication) -> bool:
        """Accept and validate a loan application."""
        # Perform initial basic validation — if fails, return False

        # If credit service is configured:
        #   Try to get the credit score for the given applicant
        #   If fetching fails (exception), reject application (return False)
        #   If score is below minimum threshold (e.g., 600), reject

        # If all validations pass, save application to repository
        # Return True indicating acceptance


    def _is_valid(self, application: LoanApplication) -> bool:
        """Basic field validation from Assignment 1 (unchanged)."""
        # Check applicant name is non-empty
        # Ensure loan amount is positive
        # Ensure term in months is positive
        # Return True if all conditions are met, else False
```


***

## **4️⃣ Running All Tests**

We can run everything from project root:

```bash
python -m unittest discover tests
```

- **Assignment 1 tests** in `test_service.py` will still pass.
- **Assignment 2 tests** in `test_service_credit.py` verify new credit score logic.

***
