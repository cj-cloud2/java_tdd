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
│   ├── __init__.py
│   ├── models.py
│   ├── repository.py
│   ├── credit_service.py   # NEW in Assignment 2
│   └── service.py
│
├── tests/
│   ├── __init__.py
│   ├── test_service.py     # Assignment 1 tests (unchanged)
│   └── test_service_credit.py   # NEW tests for Assignment 2
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
        self.mock_repo = Mock(spec=LoanRepository)
        self.mock_credit = Mock(spec=CreditScoreService)
        self.service = LoanProcessingService(repository=self.mock_repo, credit_service=self.mock_credit)

    def test_accept_when_good_credit_score(self):
        # Test Case 1: Accept application if credit score is above threshold
        app = LoanApplication("John Doe", 5000, 12)
        self.mock_credit.get_credit_score.return_value = 750

        result = self.service.accept_application(app)
        self.assertTrue(result)
        self.mock_repo.save.assert_called_once_with(app)

    def test_reject_when_low_credit_score(self):
        # Test Case 2: Reject if credit score is low
        app = LoanApplication("Jane Doe", 8000, 24)
        self.mock_credit.get_credit_score.return_value = 500

        result = self.service.accept_application(app)
        self.assertFalse(result)
        self.mock_repo.save.assert_not_called()

    def test_handle_credit_service_failure(self):
        # Test Case 3: Reject on credit service failure
        app = LoanApplication("Error Person", 5000, 12)
        self.mock_credit.get_credit_score.side_effect = Exception("Service Down")

        result = self.service.accept_application(app)
        self.assertFalse(result)
        self.mock_repo.save.assert_not_called()

if __name__ == "__main__":
    unittest.main()
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
        raise NotImplementedError("Implement in subclass or mock for testing")
```


***

## **2️⃣ loan_service/service.py** (Updated for Assignment 2)

```python
from .models import LoanApplication
from .repository import LoanRepository
from .credit_service import CreditScoreService  # NEW

class LoanProcessingService:
    """
    Loan processing service — extended for Assignment 2.
    Now includes a credit score eligibility check.
    """

    def __init__(self, repository: LoanRepository, credit_service: CreditScoreService = None):
        self.repository = repository
        self.credit_service = credit_service  # NEW

    def accept_application(self, application: LoanApplication) -> bool:
        """Accept and validate a loan application."""
        if not self._is_valid(application):
            return False

        # ========================
        # Assignment 2: Credit Score Check
        # ========================
        if self.credit_service:
            try:
                score = self.credit_service.get_credit_score(application.applicant_name)
            except Exception:
                # Simulate handling external credit service failure
                return False

            if score < 600:  # Example threshold
                return False

        # Save if valid and passes credit score check
        self.repository.save(application)
        return True

    def _is_valid(self, application: LoanApplication) -> bool:
        """Basic field validation from Assignment 1 (unchanged)."""
        if not application.applicant_name:
            return False
        if application.amount is None or application.amount <= 0:
            return False
        if application.term_months is None or application.term_months <= 0:
            return False
        return True
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
