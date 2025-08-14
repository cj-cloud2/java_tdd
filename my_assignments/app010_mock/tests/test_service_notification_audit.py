import unittest
from unittest.mock import Mock
from loan_service.service import LoanProcessingService
from loan_service.models import LoanApplication
from loan_service.repository import LoanRepository
from loan_service.credit_service import CreditScoreService
from loan_service.document_service import DocumentService
from loan_service.notification_service import NotificationService
from loan_service.audit_service import AuditService


# Command to run testcases.
# Go to app010_mock folder
# !python -m unittest discover -s ./tests -p "*.py"


class LoanProcessingServiceNotificationAuditTest(unittest.TestCase):

    def setUp(self):
        self.mock_repo = Mock(spec=LoanRepository)
        self.mock_credit = Mock(spec=CreditScoreService)
        self.mock_docs = Mock(spec=DocumentService)
        self.mock_notify = Mock(spec=NotificationService)
        self.mock_audit = Mock(spec=AuditService)

        self.mock_credit.get_credit_score.return_value = 700
        self.mock_docs.verify_documents.return_value = {"status": "valid", "details": ""}

        self.service = LoanProcessingService(
            repository=self.mock_repo,
            credit_service=self.mock_credit,
            document_service=self.mock_docs,
            notification_service=self.mock_notify,
            audit_service=self.mock_audit
        )

    def test_send_approval_notification_and_audit(self):
        app = LoanApplication("John Approved", 5000, 12)
        result = self.service.accept_application(app)
        self.assertTrue(result)
        self.mock_notify.send.assert_called_once()
        self.mock_audit.log.assert_called_once()
        self.assertEqual(self.mock_audit.log.call_args[0][1], "approved")

    def test_send_rejection_notification_and_audit(self):
        app = LoanApplication("Jane LowScore", 8000, 24)
        self.mock_credit.get_credit_score.return_value = 550
        result = self.service.accept_application(app)
        self.assertFalse(result)
        self.mock_notify.send.assert_called_once()
        self.mock_audit.log.assert_called_once()
        self.assertEqual(self.mock_audit.log.call_args[0][1], "rejected")

    def test_audit_for_pending_due_to_missing_docs(self):
        app = LoanApplication("Tom MissingDocs", 6000, 18)
        self.mock_docs.verify_documents.return_value = {"status": "missing", "details": "ID Proof missing"}
        result = self.service.accept_application(app)
        self.assertEqual(result, "awaiting_documents")
        self.mock_notify.send.assert_called_once()
        self.mock_audit.log.assert_called_once()
        self.assertEqual(self.mock_audit.log.call_args[0][1], "pending")

if __name__ == "__main__":
    unittest.main()
