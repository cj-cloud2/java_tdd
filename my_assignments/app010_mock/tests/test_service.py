import unittest
from unittest.mock import Mock
from loan_service.service import LoanProcessingService
from loan_service.models import LoanApplication
from loan_service.repository import LoanRepository

# Command to run testcases.
# Go to app010_mock folder
# !python -m unittest discover -s ./tests -p "*.py"


class LoanProcessingServiceTest(unittest.TestCase):
    
    def setUp(self):
        self.mock_repo = Mock(spec=LoanRepository)
        self.service = LoanProcessingService(repository=self.mock_repo)

    def test_validate_application_with_all_required_fields(self):
        # Test Case 1: Validate application with all required fields present
        application = LoanApplication("John Doe", 5000, 12)
        result = self.service.accept_application(application)
        self.assertTrue(result)
        self.mock_repo.save.assert_called_once_with(application)

    def test_reject_application_missing_mandatory_fields(self):
        # Test Case 2: Reject application missing mandatory field(s)
        application = LoanApplication("", 5000, 12)  # Missing name
        result = self.service.accept_application(application)
        self.assertFalse(result)
        self.mock_repo.save.assert_not_called()

    def test_persist_newly_accepted_applications(self):
        # Test Case 3: Ensure newly accepted applications are persisted
        application = LoanApplication("Alice Smith", 10000, 24)
        result = self.service.accept_application(application)
        self.assertTrue(result)
        self.mock_repo.save.assert_called_once_with(application)

if __name__ == "__main__":
    unittest.main()
