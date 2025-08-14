
## 📄 **Assignment 4 – Outcome Notification \& Persistence (Audit Trail)**

**Service goal:**

- Deliver application outcome (approval/rejection/pending) via a notification service.
- Log all application outcomes to an **Audit Service**.

**Test Cases:**

1. Send approval notification on successful approval (**mock notification service**).
2. Send rejection notification with reason.
3. Audit trail is created for every application outcome (**mock audit service**).

**After this step:**
The service orchestrates:

- Application intake ✅
- Credit score check ✅
- Document verification ✅
- Final notification \& audit logging ✅

***

## 📂 Updated Project Structure (new files in bold)

```
loan_app/
│
├── loan_service/
│   ├── __init__.py
│   ├── models.py
│   ├── repository.py
│   ├── credit_service.py
│   ├── document_service.py
│   ├── notification_service.py   # NEW in Assignment 4
│   ├── audit_service.py          # NEW in Assignment 4
│   └── service.py
│
├── tests/
│   ├── __init__.py
│   ├── test_service.py
│   ├── test_service_credit.py
│   ├── test_service_documents.py
│   └── test_service_notification_audit.py  # NEW for Assignment 4
│
└── main.py
```


***

## **4️⃣ tests/test_service_notification_audit.py** (NEW)

```python
import unittest
from unittest.mock import Mock
from loan_service.service import LoanProcessingService
from loan_service.models import LoanApplication
from loan_service.repository import LoanRepository
from loan_service.credit_service import CreditScoreService
from loan_service.document_service import DocumentService
from loan_service.notification_service import NotificationService
from loan_service.audit_service import AuditService


class LoanProcessingServiceNotificationAuditTest(unittest.TestCase):


    def setUp(self):
        # Create mocks for repository, credit service, document service, notification service, and audit service
        # Configure credit service mock to return good score by default
        # Configure document service mock to return valid status by default
        # Create LoanProcessingService instance with all mocks injected


    def test_send_approval_notification_and_audit(self):
        # Create a valid loan application
        # Call accept_application
        # Assert True result
        # Check notification send called once
        # Check audit log called once and verify outcome "approved"


    def test_send_rejection_notification_and_audit(self):
        # Create valid loan application but configure credit score below threshold
        # Call accept_application
        # Assert False result
        # Verify notification send was called
        # Verify audit log called and outcome "rejected"


    def test_audit_for_pending_due_to_missing_docs(self):
        # Create valid loan application
        # Configure document service to return "missing" status
        # Call accept_application
        # Assert result equals "awaiting_documents"
        # Verify notification send called
        # Verify audit log outcome is "pending"


if __name__ == "__main__":
    # Run these unit tests when executed directly
```


***

## **1️⃣ loan_service/notification_service.py** (NEW)

```python
# ========================
# Assignment 4 — New Code
# ========================


class NotificationService:
    """Interface to send notifications to applicants."""
    def send(self, applicant_name: str, message: str):
        # Abstract method to be implemented by concrete notification handler or mocked during testing
```


***

## **2️⃣ loan_service/audit_service.py** (NEW)

```python
# ========================
# Assignment 4 — New Code
# ========================


class AuditService:
    """Interface to record audit logs for loan application processing."""
    def log(self, applicant_name: str, outcome: str, details: str = ""):
        # Abstract method to log processing outcome; implemented in subclasses or mocks
```


***

## **3️⃣ loan_service/service.py** (Updated to handle notifications + audit)

```python
from .models import LoanApplication
from .repository import LoanRepository
from .credit_service import CreditScoreService
from .document_service import DocumentService
from .notification_service import NotificationService  # NEW
from .audit_service import AuditService  # NEW


class LoanProcessingService:
    """
    Loan processing service — Assignment 1, 2, 3 & 4
    """


    def __init__(self, repository: LoanRepository,
                 credit_service: CreditScoreService = None,
                 document_service: DocumentService = None,
                 notification_service: NotificationService = None,  # NEW
                 audit_service: AuditService = None):  # NEW
        # Store repository, credit service, document service, notification service, and audit service for later use


    def accept_application(self, application: LoanApplication):
        """
        Process loan application and send outcome notifications + audit logs.
        """
        # Initialize outcome and reason variables

        # Step 1: Basic validation (Assignment 1) — if fails, set outcome to "rejected", reason to "Missing mandatory fields", notify & audit, return False

        # Step 2: Credit check (Assignment 2) — if credit service available, attempt to get score; failure triggers rejection with "Credit service failure"
        # If score below 600, reject with "Low credit score"; in both cases notify & audit before returning

        # Step 3: Document check (Assignment 3) — if document service available, verify documents
        # If status "missing", set outcome to "pending", reason "Awaiting documents", notify & audit, return "awaiting_documents"
        # If status "invalid", set outcome "rejected", reason "Invalid documents", notify & audit, return False

        # Step 4: If all checks pass, persist application in repository
        # Set outcome to "approved", reason to "Application approved successfully"
        # Notify and audit before returning True


    def _is_valid(self, application: LoanApplication) -> bool:
        # Check applicant name is not empty
        # Check loan amount is positive
        # Check loan term is positive
        # Return True if all checks pass, else False


    def _notify_and_audit(self, applicant_name: str, outcome: str, details: str):
        """Internal helper to send notification and log audit."""
        # If notification service present, send message with outcome and details to applicant
        # If audit service present, log the applicant name, outcome, and details
```


***

## ▶ How to Run All Tests

From **project root**:

```bash
python -m unittest discover loan_app/tests
```

You should see tests from:

- Assignment 1 ✅
- Assignment 2 ✅
- Assignment 3 ✅
- Assignment 4 ✅

***
