from .models import LoanApplication

class LoanRepository:
    """Interface for saving loan applications (to be mocked)."""
    def save(self, application: LoanApplication):
        raise NotImplementedError("Subclasses or mocks must implement this method.")
