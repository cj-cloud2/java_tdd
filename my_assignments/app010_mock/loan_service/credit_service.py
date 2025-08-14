class CreditScoreService:
    """Interface to fetch applicant's credit score."""
    def get_credit_score(self, applicant_name: str) -> int:
        raise NotImplementedError("Implement in subclass or mock for testing")
