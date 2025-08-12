package org.p011_mocks2.v3;

// New service class for Assignment 3: integrates employment verification with previous checks
public class LoanProcessingWithEmploymentVerificationService {
    private final LoanRepository loanRepository;
    private final CreditBureauService creditBureauService;
    private final HrVerificationService hrVerificationService;
    private static final int CREDIT_SCORE_THRESHOLD = 700; // example threshold

    public LoanProcessingWithEmploymentVerificationService(LoanRepository loanRepository,
                                                           CreditBureauService creditBureauService,
                                                           HrVerificationService hrVerificationService) {
        this.loanRepository = loanRepository;
        this.creditBureauService = creditBureauService;
        this.hrVerificationService = hrVerificationService;
    }

    // New code for Assignment 3: process application with employment verification
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
            // HR service unavailable
            application.setStatus(LoanStatus.VERIFICATION_PENDING);  // mark status accordingly
            loanRepository.save(application);
            return new LoanProcessingResult(false, "Employment verification pending due to system unavailability.");
        }

        switch (employmentStatus) {
            case VERIFIED:
                application.setStatus(LoanStatus.APPROVED);
                loanRepository.save(application);
                return new LoanProcessingResult(true, "Application approved with employment verified.");
            case UNVERIFIED:
                // Reject application if employment unverified
                application.setStatus(LoanStatus.REJECTED);
                loanRepository.save(application);
                return new LoanProcessingResult(false, "Employment could not be verified.");
            case UNAVAILABLE:
                application.setStatus(LoanStatus.VERIFICATION_PENDING);
                loanRepository.save(application);
                return new LoanProcessingResult(false, "Employment verification pending due to HR system unavailability.");
            default:
                // Defensive fallback
                application.setStatus(LoanStatus.VERIFICATION_PENDING);
                loanRepository.save(application);
                return new LoanProcessingResult(false, "Employment verification pending.");
        }
    }
}
