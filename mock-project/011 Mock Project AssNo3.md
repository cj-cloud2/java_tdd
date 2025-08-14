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
│   ├── __init__.py
│   ├── models.py
│   ├── repository.py
│   ├── credit_service.py
│   ├── document_service.py      # NEW in Assignment 3
│   └── service.py               # Updated for Assignment 3
│
├── tests/
│   ├── __init__.py
│   ├── test_service.py
│   ├── test_service_credit.py
│   └── test_service_documents.py  # NEW for Assignment 3
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
        self.mock_repo = Mock(spec=LoanRepository)
        self.mock_credit = Mock(spec=CreditScoreService)
        self.mock_docs = Mock(spec=DocumentService)

        # Default: good credit score
        self.mock_credit.get_credit_score.return_value = 700

        self.service = LoanProcessingService(
            repository=self.mock_repo,
            credit_service=self.mock_credit,
            document_service=self.mock_docs
        )

    def test_accept_with_all_documents_valid(self):
        # Test Case 1
        app = LoanApplication("John Valid", 5000, 12)
        self.mock_docs.verify_documents.return_value = {"status": "valid", "details": ""}
        result = self.service.accept_application(app)
        self.assertTrue(result)
        self.mock_repo.save.assert_called_once_with(app)

    def test_mark_as_awaiting_when_documents_missing(self):
        # Test Case 2
        app = LoanApplication("Jane MissingDocs", 7000, 18)
        self.mock_docs.verify_documents.return_value = {"status": "missing", "details": "ID Proof missing"}
        result = self.service.accept_application(app)
        self.assertEqual(result, "awaiting_documents")
        self.mock_repo.save.assert_not_called()

    def test_reject_when_documents_invalid(self):
        # Test Case 3
        app = LoanApplication("Tom InvalidDocs", 8000, 24)
        self.mock_docs.verify_documents.return_value = {"status": "invalid", "details": "Expired passport"}
        result = self.service.accept_application(app)
        self.assertFalse(result)
        self.mock_repo.save.assert_not_called()


if __name__ == "__main__":
    unittest.main()
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
        raise NotImplementedError("Implement in subclass or mock for testing")
```


***

## **2️⃣ loan_service/service.py** (Updated to integrate Document Validation)

```python
from .models import LoanApplication
from .repository import LoanRepository
from .credit_service import CreditScoreService
from .document_service import DocumentService  # NEW

class LoanProcessingService:
    """
    Loan processing service — Assignment 1, 2 & 3
    """

    def __init__(
        self,
        repository: LoanRepository,
        credit_service: CreditScoreService = None,
        document_service: DocumentService = None   # NEW
    ):
        self.repository = repository
        self.credit_service = credit_service
        self.document_service = document_service   # NEW

    def accept_application(self, application: LoanApplication):
        """
        Accepts and validates loan applications.
        Returns:
          - True if fully valid and persisted
          - "awaiting_documents" if documents missing
          - False if rejected
        """
        # Assignment 1: Basic required fields check
        if not self._is_valid(application):
            return False

        # Assignment 2: Credit score check
        if self.credit_service:
            try:
                score = self.credit_service.get_credit_score(application.applicant_name)
            except Exception:
                return False
            if score < 600:
                return False

        # ========================
        # Assignment 3: Document Validation
        # ========================
        if self.document_service:
            result = self.document_service.verify_documents(application.applicant_name)

            if result["status"] == "missing":
                return "awaiting_documents"  # multi-stage processing
            elif result["status"] == "invalid":
                return False  # reject immediately

        # Save application if passed all checks
        self.repository.save(application)
        return True

    def _is_valid(self, application: LoanApplication) -> bool:
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

From the project root:

```bash
python -m unittest discover tests
```

Expected:

- Assignment 1 tests ✅
- Assignment 2 tests ✅
- Assignment 3 new tests ✅

***
