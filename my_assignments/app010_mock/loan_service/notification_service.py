class NotificationService:
    """Interface to send notifications to applicants."""
    def send(self, applicant_name: str, message: str):
        raise NotImplementedError("Implement in subclass or mock for testing")
