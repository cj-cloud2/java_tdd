
## 📄 **Assignment 3: Document Validation Layer**

**Service goal:**
Add a missing/invalid documents verification workflow.

**Test Cases:**

1. Accept with all required documents present (**mock document service**).
2. Mark application as `"awaiting_documents"` if any are missing.
3. Reject application with invalid/expired documents.

**After this step:**
Service can perform **multi‑stage conditional processing** and produce more nuanced statuses.

***

## 📂 Updated Project Structure

```
loan_app/
│
├── loan_service/
│   ├── __init__.py
│   ├── models.py
│   ├── repository.py
│   ├── credit_service.py
│   ├── document_service.py      # NEW in Assignment 3
│   └── service.py               # Updated for Assignment 3
│
├── tests/
│   ├── __init__.py
│   ├── test_service.py
│   ├── test_service_credit.py
│   └── test_service_documents.py  # NEW for Assignment 3
│
└── main.py
```


## **3️⃣ tests/test_service_documents.py** (NEW — Assignment 3 tests)

```python
import unittest
from unittest.mock import Mock
from loan_service.service import LoanProcessingService
from loan_service.models import LoanApplication
from loan_service.repository import LoanRepository
from loan_service.credit_service import CreditScoreService
from loan_service.document_service import DocumentService


class LoanProcessingServiceDocumentTest(unittest.TestCase):


    def setUp(self):
        # Create mock repository
        # Create mock credit service
        # Create mock document service
        # Configure mock credit service to return good score by default
        # Initialize LoanProcessingService with the mocks


    def test_accept_with_all_documents_valid(self):
        # Create loan application with valid details
        # Configure document service to return status "valid"
        # Call accept_application and store result
        # Assert True returned and verify repository save called with app


    def test_mark_as_awaiting_when_documents_missing(self):
        # Create loan application
        # Configure document service to return "missing" status with reason
        # Call accept_application
        # Assert result equals "awaiting_documents" and save not called


    def test_reject_when_documents_invalid(self):
        # Create loan application
        # Configure document service to return "invalid" with details
        # Call accept_application
        # Assert False returned and that save not called


if __name__ == "__main__":
    # Run these unit tests when executed directly
```


***

***

## **1️⃣ loan_service/document_service.py** (NEW)

```python
# ========================
# Assignment 3 — New Code
# ========================


class DocumentService:
    """Interface to verify applicant documents."""
    def verify_documents(self, applicant_name: str) -> dict:
        """
        Returns a dict like:
        {
            "status": "valid" | "missing" | "invalid",
            "details": "Reason or info for status"
        }
        """
        # Abstract method to be implemented in concrete service or mocked in tests
```


***

## **2️⃣ loan_service/service.py** (Updated to integrate Document Validation)

```python
from .models import LoanApplication
from .repository import LoanRepository
from .credit_service import CreditScoreService
from .document_service import DocumentService  # NEW


class LoanProcessingService:
    """
    Loan processing service — Assignment 1, 2 & 3
    """


    def __init__(
        self,
        repository: LoanRepository,
        credit_service: CreditScoreService = None,
        document_service: DocumentService = None   # NEW
    ):
        # Store repository reference
        # Store credit service (optional)
        # Store document service (optional)


    def accept_application(self, application: LoanApplication):
        """
        Accepts and validates loan applications.
        Returns:
          - True if fully valid and persisted
          - "awaiting_documents" if documents missing
          - False if rejected
        """
        # Step 1 (Assignment 1): Check required fields; if invalid, return False

        # Step 2 (Assignment 2): If credit_service present, try getting credit score
        # Handle exceptions (service failure) by rejecting (False)
        # Reject if score < 600

        # Step 3 (Assignment 3): If document_service present, verify documents
        # If status "missing", return "awaiting_documents"
        # If status "invalid", return False

        # Step 4: Save application to repository
        # Return True to signal acceptance


    def _is_valid(self, application: LoanApplication) -> bool:
        # Check non-empty applicant name
        # Verify amount is positive
        # Verify term_months is positive
        # Return True if all checks pass, else False
```


***

## **4️⃣ Running All Tests**

From the project root:

```bash
python -m unittest discover tests
```

Expected:

- Assignment 1 tests ✅
- Assignment 2 tests ✅
- Assignment 3 new tests ✅

***