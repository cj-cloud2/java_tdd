class AuditService:
    """Interface to record audit logs for loan application processing."""
    def log(self, applicant_name: str, outcome: str, details: str = ""):
        raise NotImplementedError("Implement in subclass or mock for testing")
