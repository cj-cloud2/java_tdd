
## **Assignment 1: Basic Loan Application Intake**


### **1) Complete Assignment Description**


**Service goal:**
Accept and validate raw loan applications.

**Test Cases:**

1. Validate application with all required fields present.
2. Reject application missing mandatory field(s).
3. Ensure newly accepted applications are persisted (mock repository).

**Service class after this step:**
Handles intake and simple validation, persists loan applications.

***

***

## **📂 Project Structure for Assignment 1**

```
loan_app/
│
├── loan_service/
│   ├── __init__.py
│   ├── models.py
│   ├── repository.py
│   └── service.py
│
├── tests/
│   ├── __init__.py
│   └── test_service.py
│
└── main.py   # (optional entry point for manual runs)
```


## **4️⃣ tests/test_service.py**

```python
import unittest
from unittest.mock import Mock
from loan_service.service import LoanProcessingService
from loan_service.models import LoanApplication
from loan_service.repository import LoanRepository


class LoanProcessingServiceTest(unittest.TestCase):
    
    def setUp(self):
        # Create a mock repository
        self.mock_repo = Mock(spec=LoanRepository)
        self.service = LoanProcessingService(repository=self.mock_repo)

    def test_validate_application_with_all_required_fields(self):
        # Test Case 1: Validate application with all required fields present
        application = LoanApplication(
            applicant_name="John Doe",
            amount=5000,
            term_months=12
        )

        result = self.service.accept_application(application)

        self.assertTrue(result)
        self.mock_repo.save.assert_called_once_with(application)

    def test_reject_application_missing_mandatory_fields(self):
        # Test Case 2: Reject application missing mandatory field(s)
        application = LoanApplication(
            applicant_name="",  # Missing name
            amount=5000,
            term_months=12
        )

        result = self.service.accept_application(application)

        self.assertFalse(result)
        self.mock_repo.save.assert_not_called()

    def test_persist_newly_accepted_applications(self):
        # Test Case 3: Ensure newly accepted applications are persisted
        application = LoanApplication(
            applicant_name="Alice Smith",
            amount=10000,
            term_months=24
        )

        result = self.service.accept_application(application)

        self.assertTrue(result)
        self.mock_repo.save.assert_called_once_with(application)


if __name__ == "__main__":
    unittest.main()
```


***

## **1️⃣ loan_service/models.py**

```python
# ========================
# Assignment 1 — New Code
# ========================

class LoanApplication:
    """Represents a customer's loan application."""
    def __init__(self, applicant_name: str, amount: float, term_months: int):
        self.applicant_name = applicant_name
        self.amount = amount
        self.term_months = term_months
```


***

## **2️⃣ loan_service/repository.py**

```python
# ========================
# Assignment 1 — New Code
# ========================

from .models import LoanApplication

class LoanRepository:
    """Interface for saving loan applications (to be mocked)."""
    def save(self, application: LoanApplication):
        raise NotImplementedError("Subclasses or mocks must implement this method.")
```


***

## **3️⃣ loan_service/service.py**

```python
# ========================
# Assignment 1 — New Code
# ========================

from .models import LoanApplication
from .repository import LoanRepository

class LoanProcessingService:
    """Handles basic loan intake and validation (Assignment 1)."""
    
    def __init__(self, repository: LoanRepository):
        self.repository = repository

    def accept_application(self, application: LoanApplication) -> bool:
        """
        Accept and validate a loan application.
        Ensure required fields are filled and add persistence.
        """
        if not self._is_valid(application):
            return False

        # Persist application in repository
        self.repository.save(application)
        return True

    def _is_valid(self, application: LoanApplication) -> bool:
        """Basic field validation."""
        if not application.applicant_name:
            return False
        if application.amount is None or application.amount <= 0:
            return False
        if application.term_months is None or application.term_months <= 0:
            return False
        return True
```


***

***

## **5️⃣ Optional: main.py**

(This is only for manual testing, not part of unittests)

```python
from loan_service.models import LoanApplication
from loan_service.repository import LoanRepository
from loan_service.service import LoanProcessingService


class InMemoryLoanRepository(LoanRepository):
    def __init__(self):
        self._db = []

    def save(self, application: LoanApplication):
        self._db.append(application)
        print(f"Saved application for {application.applicant_name}")


if __name__ == "__main__":
    repo = InMemoryLoanRepository()
    service = LoanProcessingService(repo)

    app = LoanApplication("John Doe", 5000, 12)
    result = service.accept_application(app)
    print("Application accepted:", result)
```


***

## **Run Tests**

From the **project root**:

```bash
python -m unittest discover tests
```


***