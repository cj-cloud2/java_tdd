package org.p011_mocks2.v2;


// New Service class for Assignment 2: Intakes, credit checks, and persists if eligible
public class LoanProcessingWithCreditScoreService {
    private final LoanRepository loanRepository;
    private final CreditBureauService creditBureauService;
    private static final int CREDIT_SCORE_THRESHOLD = 700; // example threshold

    public LoanProcessingWithCreditScoreService(LoanRepository loanRepository, CreditBureauService creditBureauService) {
        this.loanRepository = loanRepository;
        this.creditBureauService = creditBureauService;
    }

    // New code for Assignment 2: validate credit score before saving
    public LoanProcessingResult processApplication(LoanApplication application) {
        // Basic validation reused from Assignment 1
        if (application.getApplicantName() == null || application.getApplicantName().isBlank()
                || application.getApplicantId() == null || application.getApplicantId().isBlank()
                || application.getAmount() == null || application.getAmount() <= 0) {
            return new LoanProcessingResult(false, "Mandatory field missing or invalid.");
        }

        int creditScore;
        try {
            creditScore = creditBureauService.fetchCreditScore(application.getApplicantId());
        } catch (Exception e) {
            // Handle credit bureau failures gracefully
            return new LoanProcessingResult(false, "Failed to retrieve credit score.");
        }

        if (creditScore < CREDIT_SCORE_THRESHOLD) {
            return new LoanProcessingResult(false, "Credit score below threshold.");
        }

        loanRepository.save(application);
        return new LoanProcessingResult(true, "Application accepted.");
    }
}