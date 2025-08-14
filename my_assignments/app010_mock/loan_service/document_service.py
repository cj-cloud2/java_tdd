class DocumentService:
    """Interface to verify applicant documents."""
    def verify_documents(self, applicant_name: str) -> dict:
        """
        Returns:
        {
            "status": "valid" | "missing" | "invalid",
            "details": "Reason or info"
        }
        """
        raise NotImplementedError("Implement in subclass or mock for testing")
