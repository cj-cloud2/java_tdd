class LoanApplication:
    """Represents a customer's loan application."""
    def __init__(self, applicant_name: str, amount: float, term_months: int):
        self.applicant_name = applicant_name
        self.amount = amount
        self.term_months = term_months
