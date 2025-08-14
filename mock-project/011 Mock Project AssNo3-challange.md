<img src="https://r2cdn.perplexity.ai/pplx-full-logo-primary-dark%402x.png" style="height:64px;margin-right:32px"/>

# \#\# 📄 **Assignment 3: Document Validation Layer**

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
        self.repository = repository
        self.credit_service = credit_service
        self.document_service = document_service   # NEW


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
                return "awaiting_documents"  # multi-stage processing
            elif result["status"] == "invalid":
                return False  # reject immediately


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

***.. From above content print everything as it is. Do not change a single word, except, In python code , lined from within functions -remove them, instead replace those lines with "english descriptive comments" explaining logic from these lines

Got it ✅ — here’s your **Assignment 3** content with the exact same structure and wording, except that inside all Python functions the actual code lines are replaced by **English descriptive comments** explaining their logic.

***

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

If you want, I can now **merge Assignment 1, 2, and 3** into one continuous descriptive-comment source so you have a fully documented, multi-stage loan processing service in one view.
Do you want me to prepare that?

