from .models import LoanApplication
from .repository import LoanRepository
from .credit_service import CreditScoreService
from .document_service import DocumentService
from .notification_service import NotificationService
from .audit_service import AuditService

class LoanProcessingService:
    """
    Loan processing service — Assignment 1, 2, 3 & 4
    """

    def __init__(self, repository: LoanRepository,
                 credit_service: CreditScoreService = None,
                 document_service: DocumentService = None,
                 notification_service: NotificationService = None,
                 audit_service: AuditService = None):
        self.repository = repository
        self.credit_service = credit_service
        self.document_service = document_service
        self.notification_service = notification_service
        self.audit_service = audit_service

    def accept_application(self, application: LoanApplication):
        """
        Process a loan application end-to-end.
        Returns:
            True  → Approved
            "awaiting_documents" → Pending docs
            False → Rejected
        """
        outcome = None
        reason = ""

        # Assignment 1 — Basic field validation
        if not self._is_valid(application):
            outcome, reason = "rejected", "Missing mandatory fields"
            self._notify_and_audit(application.applicant_name, outcome, reason)
            return False

        # Assignment 2 — Credit score check
        if self.credit_service:
            try:
                score = self.credit_service.get_credit_score(application.applicant_name)
            except Exception:
                outcome, reason = "rejected", "Credit service failure"
                self._notify_and_audit(application.applicant_name, outcome, reason)
                return False
            if score < 600:
                outcome, reason = "rejected", "Low credit score"
                self._notify_and_audit(application.applicant_name, outcome, reason)
                return False

        # Assignment 3 — Document verification
        if self.document_service:
            doc_result = self.document_service.verify_documents(application.applicant_name)
            if doc_result["status"] == "missing":
                outcome, reason = "pending", "Awaiting documents"
                self._notify_and_audit(application.applicant_name, outcome, reason)
                return "awaiting_documents"
            elif doc_result["status"] == "invalid":
                outcome, reason = "rejected", "Invalid documents"
                self._notify_and_audit(application.applicant_name, outcome, reason)
                return False

        # Passed all checks → approve
        self.repository.save(application)
        outcome, reason = "approved", "Application approved successfully"
        self._notify_and_audit(application.applicant_name, outcome, reason)
        return True

    def _is_valid(self, application: LoanApplication) -> bool:
        if not application.applicant_name:
            return False
        if application.amount is None or application.amount <= 0:
            return False
        if application.term_months is None or application.term_months <= 0:
            return False
        return True

    def _notify_and_audit(self, applicant_name: str, outcome: str, details: str):
        """Send notification and write to audit trail."""
        if self.notification_service:
            self.notification_service.send(applicant_name, f"Your application is {outcome}. {details}")
        if self.audit_service:
            self.audit_service.log(applicant_name, outcome, details)
