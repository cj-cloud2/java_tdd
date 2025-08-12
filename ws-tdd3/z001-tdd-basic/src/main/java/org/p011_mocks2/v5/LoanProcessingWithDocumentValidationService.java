package org.p011_mocks2.v5;


// New service class for Assignment 4: extends previous service to add document validation
public class LoanProcessingWithDocumentValidationService {
    private final LoanRepository loanRepository;
    private final CreditBureauService creditBureauService;
    private final HrVerificationService hrVerificationService;
    private final DocumentValidationService documentValidationService;
    private static final int CREDIT_SCORE_THRESHOLD = 700; // example threshold

    public LoanProcessingWithDocumentValidationService(LoanRepository loanRepository,
                                                       CreditBureauService creditBureauService,
                                                       HrVerificationService hrVerificationService,
                                                       DocumentValidationService documentValidationService) {
        this.loanRepository = loanRepository;
        this.creditBureauService = creditBureauService;
        this.hrVerificationService = hrVerificationService;
        this.documentValidationService = documentValidationService;
    }

    // New code for Assignment 4: process application with document validation
    public LoanProcessingResult processApplication(LoanApplication application) {
        // Basic validation from previous assignments
        if (application.getApplicantName() == null || application.getApplicantName().isBlank()
                || application.getApplicantId() == null || application.getApplicantId().isBlank()
                || application.getAmount() == null || application.getAmount() <= 0) {
            return new LoanProcessingResult(false, "Mandatory field missing or invalid.");
        }

        int creditScore;
        try {
            creditScore = creditBureauService.fetchCreditScore(application.getApplicantId());
        } catch (Exception e) {
            return new LoanProcessingResult(false, "Failed to retrieve credit score.");
        }

        if (creditScore < CREDIT_SCORE_THRESHOLD) {
            return new LoanProcessingResult(false, "Credit score below threshold.");
        }

        EmploymentStatus employmentStatus;
        try {
            employmentStatus = hrVerificationService.verifyEmployment(application.getApplicantId());
        } catch (Exception e) {
            application.setStatus(LoanStatus.VERIFICATION_PENDING);
            loanRepository.save(application);
            return new LoanProcessingResult(false, "Employment verification pending due to system unavailability.");
        }

        if (employmentStatus == EmploymentStatus.UNVERIFIED) {
            application.setStatus(LoanStatus.REJECTED);
            loanRepository.save(application);
            return new LoanProcessingResult(false, "Employment could not be verified.");
        } else if (employmentStatus == EmploymentStatus.UNAVAILABLE) {
            application.setStatus(LoanStatus.VERIFICATION_PENDING);
            loanRepository.save(application);
            return new LoanProcessingResult(false, "Employment verification pending due to HR system unavailability.");
        }

        // Check documents only after employment verified and credit score passed
        DocumentValidationStatus docStatus;
        try {
            docStatus = documentValidationService.validateDocuments(application.getApplicantId());
        } catch (Exception e) {
            application.setStatus(LoanStatus.VERIFICATION_PENDING);
            loanRepository.save(application);
            return new LoanProcessingResult(false, "Document validation pending due to system unavailability.");
        }

        switch (docStatus) {
            case VALID:
                application.setStatus(LoanStatus.APPROVED);
                loanRepository.save(application);
                return new LoanProcessingResult(true, "Application approved with valid documents.");
            case MISSING:
                application.setStatus(LoanStatus.VERIFICATION_PENDING);
                loanRepository.save(application);
                return new LoanProcessingResult(false, "Awaiting required documents.");
            case INVALID:
                application.setStatus(LoanStatus.REJECTED);
                loanRepository.save(application);
                return new LoanProcessingResult(false, "Application rejected due to invalid or expired documents.");
            default:
                application.setStatus(LoanStatus.VERIFICATION_PENDING);
                loanRepository.save(application);
                return new LoanProcessingResult(false, "Document validation pending.");
        }
    }
}

// Reuse the updated LoanApplication with status field from Assignment 3:
// public class LoanApplication {
//     private String applicantName;
//     private String applicantId;
//     private Integer amount;
//     private LoanStatus status;
//     // ... constructor, getters, setters
// }

// Reuse enums EmploymentStatus and LoanStatus from Assignment 3

// Reuse interfaces and classes from previous assignments:
// CreditBureauService, HrVerificationService, LoanRepository, LoanProcessingResult
