package org.p011_mocks2.v5;

// New service class for Assignment 5: adds notifications and audit logging to previous services
public class LoanProcessingWithOutcomeNotificationService {
    private final LoanRepository loanRepository;
    private final CreditBureauService creditBureauService;
    private final HrVerificationService hrVerificationService;
    private final DocumentValidationService documentValidationService;
    private final NotificationService notificationService;
    private final AuditService auditService;
    private static final int CREDIT_SCORE_THRESHOLD = 700;

    public LoanProcessingWithOutcomeNotificationService(
            LoanRepository loanRepository,
            CreditBureauService creditBureauService,
            HrVerificationService hrVerificationService,
            DocumentValidationService documentValidationService,
            NotificationService notificationService,
            AuditService auditService) {
        this.loanRepository = loanRepository;
        this.creditBureauService = creditBureauService;
        this.hrVerificationService = hrVerificationService;
        this.documentValidationService = documentValidationService;
        this.notificationService = notificationService;
        this.auditService = auditService;
    }

    // New code for Assignment 5: process application with notifications and audit logging
    public LoanProcessingResult processApplication(LoanApplication application) {
        // Basic validation from previous assignments
        if (application.getApplicantName() == null || application.getApplicantName().isBlank()
                || application.getApplicantId() == null || application.getApplicantId().isBlank()
                || application.getAmount() == null || application.getAmount() <= 0) {
            return rejectApplication(application, "Mandatory field missing or invalid.");
        }

        int creditScore;
        try {
            creditScore = creditBureauService.fetchCreditScore(application.getApplicantId());
        } catch (Exception e) {
            return rejectApplication(application, "Failed to retrieve credit score.");
        }

        if (creditScore < CREDIT_SCORE_THRESHOLD) {
            return rejectApplication(application, "Credit score below threshold.");
        }

        EmploymentStatus employmentStatus;
        try {
            employmentStatus = hrVerificationService.verifyEmployment(application.getApplicantId());
        } catch (Exception e) {
            application.setStatus(LoanStatus.VERIFICATION_PENDING);
            loanRepository.save(application);
            notificationService.sendPendingNotification(application, "Employment verification pending due to system unavailability.");
            auditService.logOutcome(application, "Employment verification pending");
            return new LoanProcessingResult(false, "Employment verification pending due to system unavailability.");
        }

        if (employmentStatus == EmploymentStatus.UNVERIFIED) {
            return rejectApplicationWithStatus(application, "Employment could not be verified.", LoanStatus.REJECTED);
        } else if (employmentStatus == EmploymentStatus.UNAVAILABLE) {
            application.setStatus(LoanStatus.VERIFICATION_PENDING);
            loanRepository.save(application);
            notificationService.sendPendingNotification(application, "Employment verification pending due to HR system unavailability.");
            auditService.logOutcome(application, "Employment verification pending");
            return new LoanProcessingResult(false, "Employment verification pending due to HR system unavailability.");
        }

        // Document validation
        DocumentValidationStatus docStatus;
        try {
            docStatus = documentValidationService.validateDocuments(application.getApplicantId());
        } catch (Exception e) {
            application.setStatus(LoanStatus.VERIFICATION_PENDING);
            loanRepository.save(application);
            notificationService.sendPendingNotification(application, "Document validation pending due to system unavailability.");
            auditService.logOutcome(application, "Document validation pending");
            return new LoanProcessingResult(false, "Document validation pending due to system unavailability.");
        }

        switch (docStatus) {
            case VALID:
                return approveApplication(application, "Application approved with valid documents.");
            case MISSING:
                application.setStatus(LoanStatus.VERIFICATION_PENDING);
                loanRepository.save(application);
                notificationService.sendPendingNotification(application, "Awaiting required documents.");
                auditService.logOutcome(application, "Awaiting required documents");
                return new LoanProcessingResult(false, "Awaiting required documents.");
            case INVALID:
                return rejectApplicationWithStatus(application, "Application rejected due to invalid or expired documents.", LoanStatus.REJECTED);
            default:
                application.setStatus(LoanStatus.VERIFICATION_PENDING);
                loanRepository.save(application);
                notificationService.sendPendingNotification(application, "Document validation pending.");
                auditService.logOutcome(application, "Document validation pending");
                return new LoanProcessingResult(false, "Document validation pending.");
        }
    }

    // Helper methods to standardize approval and rejection handling
    private LoanProcessingResult approveApplication(LoanApplication application, String message) {
        application.setStatus(LoanStatus.APPROVED);
        loanRepository.save(application);
        notificationService.sendApprovalNotification(application);
        auditService.logOutcome(application, "Application approved");
        return new LoanProcessingResult(true, message);
    }

    private LoanProcessingResult rejectApplication(LoanApplication application, String reason) {
        return rejectApplicationWithStatus(application, reason, LoanStatus.REJECTED);
    }

    private LoanProcessingResult rejectApplicationWithStatus(LoanApplication application, String reason, LoanStatus status) {
        application.setStatus(status);
        loanRepository.save(application);
        notificationService.sendRejectionNotification(application, reason);
        auditService.logOutcome(application, "Application rejected: " + reason);
        return new LoanProcessingResult(false, reason);
    }
}

// Reuse enums EmploymentStatus, LoanStatus, DocumentValidationStatus

// Updated LoanApplication class with status from previous assignments
// public class LoanApplication {
//     private String applicantName;
//     private String applicantId;
//     private Integer amount;
//     private LoanStatus status;
//     // Constructor, getters, and setters ...
// }

// Reuse from previous assignments:
// CreditBureauService, HrVerificationService, DocumentValidationService, LoanRepository, LoanProcessingResult
