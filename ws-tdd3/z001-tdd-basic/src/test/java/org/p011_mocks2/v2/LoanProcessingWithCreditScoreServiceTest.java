package org.p011_mocks2.v2;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoanProcessingWithCreditScoreServiceTest {
    private LoanRepository loanRepository;
    private CreditBureauService creditBureauService;
    private LoanProcessingWithCreditScoreService loanService;

    @BeforeEach
    void setUp() {
        loanRepository = mock(LoanRepository.class);
        creditBureauService = mock(CreditBureauService.class);
        loanService = new LoanProcessingWithCreditScoreService(loanRepository, creditBureauService);
    }

    // Test Case 1: Accept application if credit score is above threshold (mock credit bureau).
    @Test
    void shouldAcceptIfCreditScoreAboveThreshold() {
        LoanApplication application = new LoanApplication("Alice", "111223333", 40000);
        when(creditBureauService.fetchCreditScore(application.getApplicantId())).thenReturn(750);

        LoanProcessingResult result = loanService.processApplication(application);

        assertTrue(result.isAccepted());
        verify(loanRepository).save(application);
    }

    // Test Case 2: Reject application for low credit score.
    @Test
    void shouldRejectIfCreditScoreBelowThreshold() {
        LoanApplication application = new LoanApplication("Bob", "999887777", 30000);
        when(creditBureauService.fetchCreditScore(application.getApplicantId())).thenReturn(550);

        LoanProcessingResult result = loanService.processApplication(application);

        assertFalse(result.isAccepted());
        verify(loanRepository, never()).save(any());
    }

    // Test Case 3: Handle failure to fetch credit score (mock failure or timeout).
    @Test
    void shouldRejectIfCreditScoreFetchFails() {
        LoanApplication application = new LoanApplication("Eve", "555666444", 20000);
        when(creditBureauService.fetchCreditScore(application.getApplicantId()))
                .thenThrow(new RuntimeException("Credit bureau timeout"));

        LoanProcessingResult result = loanService.processApplication(application);

        assertFalse(result.isAccepted());
        verify(loanRepository, never()).save(any());
    }
}