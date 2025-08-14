import unittest
from unittest.mock import Mock
from loan_service.service import LoanProcessingService
from loan_service.models import LoanApplication
from loan_service.repository import LoanRepository
from loan_service.credit_service import CreditScoreService
from loan_service.document_service import DocumentService


# Command to run testcases.
# Go to app010_mock folder
# !python -m unittest discover -s ./tests -p "*.py"




class LoanProcessingServiceDocumentTest(unittest.TestCase):
    def setUp(self):
        self.mock_repo = Mock(spec=LoanRepository)
        self.mock_credit = Mock(spec=CreditScoreService)
        self.mock_docs = Mock(spec=DocumentService)
        self.mock_credit.get_credit_score.return_value = 700  # good score

        self.service = LoanProcessingService(
            repository=self.mock_repo,
            credit_service=self.mock_credit,
            document_service=self.mock_docs
        )

    def test_accept_with_all_documents_valid(self):
        app = LoanApplication("John Valid", 5000, 12)
        self.mock_docs.verify_documents.return_value = {"status": "valid", "details": ""}
        result = self.service.accept_application(app)
        self.assertTrue(result)
        self.mock_repo.save.assert_called_once_with(app)

    def test_mark_as_awaiting_when_documents_missing(self):
        app = LoanApplication("Jane MissingDocs", 7000, 18)
        self.mock_docs.verify_documents.return_value = {"status": "missing", "details": "ID Proof missing"}
        result = self.service.accept_application(app)
        self.assertEqual(result, "awaiting_documents")
        self.mock_repo.save.assert_not_called()

    def test_reject_when_documents_invalid(self):
        app = LoanApplication("Tom InvalidDocs", 8000, 24)
        self.mock_docs.verify_documents.return_value = {"status": "invalid", "details": "Expired passport"}
        result = self.service.accept_application(app)
        self.assertFalse(result)
        self.mock_repo.save.assert_not_called()

if __name__ == "__main__":
    unittest.main()
