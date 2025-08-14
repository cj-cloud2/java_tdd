import unittest
from unittest.mock import Mock
from loan_service.service import LoanProcessingService
from loan_service.models import LoanApplication
from loan_service.repository import LoanRepository
from loan_service.credit_service import CreditScoreService


# Command to run testcases.
# Go to app010_mock folder
# !python -m unittest discover -s ./tests -p "*.py"




class LoanProcessingServiceCreditTest(unittest.TestCase):

    def setUp(self):
        self.mock_repo = Mock(spec=LoanRepository)
        self.mock_credit = Mock(spec=CreditScoreService)
        self.service = LoanProcessingService(repository=self.mock_repo, credit_service=self.mock_credit)

    def test_accept_when_good_credit_score(self):
        # Test Case 1
        app = LoanApplication("John Doe", 5000, 12)
        self.mock_credit.get_credit_score.return_value = 750
        result = self.service.accept_application(app)
        self.assertTrue(result)
        self.mock_repo.save.assert_called_once_with(app)

    def test_reject_when_low_credit_score(self):
        # Test Case 2
        app = LoanApplication("Jane Doe", 8000, 24)
        self.mock_credit.get_credit_score.return_value = 500
        result = self.service.accept_application(app)
        self.assertFalse(result)
        self.mock_repo.save.assert_not_called()

    def test_handle_credit_service_failure(self):
        # Test Case 3
        app = LoanApplication("Error Person", 5000, 12)
        self.mock_credit.get_credit_score.side_effect = Exception("Service Down")
        result = self.service.accept_application(app)
        self.assertFalse(result)
        self.mock_repo.save.assert_not_called()

if __name__ == "__main__":
    unittest.main()
