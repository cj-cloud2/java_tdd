package org.p011_mocks2.v3;

// LoanProcessingService handles intake and validation
public class LoanProcessingService {
    private final LoanRepository loanRepository;

    public LoanProcessingService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    // New code for Assignment 1: Intake, validate, and persist application
    public LoanProcessingResult processApplication(LoanApplication application) {
        if (application.getApplicantName() == null || application.getApplicantName().isBlank()
                || application.getApplicantId() == null || application.getApplicantId().isBlank()
                || application.getAmount() == null || application.getAmount() <= 0) {
            return new LoanProcessingResult(false, "Mandatory field missing or invalid.");
        }
        loanRepository.save(application);
        return new LoanProcessingResult(true, "Application accepted.");
    }
}