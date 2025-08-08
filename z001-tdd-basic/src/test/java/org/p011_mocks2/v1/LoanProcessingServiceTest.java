package org.p011_mocks2.v1;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoanProcessingServiceTest {
    private LoanRepository loanRepository;
    private LoanProcessingService loanProcessingService;

    @BeforeEach
    void setUp() {
        loanRepository = mock(LoanRepository.class);
        loanProcessingService = new LoanProcessingService(loanRepository);
    }

    // Test Case 1: Validate application with all required fields present.
    @Test
    void shouldAcceptValidLoanApplication() {
        LoanApplication application = new LoanApplication("John Doe", "1234567890", 50000);
        LoanProcessingResult result = loanProcessingService.processApplication(application);

        assertTrue(result.isAccepted());
        verify(loanRepository, times(1)).save(application);
    }

    // Test Case 2: Reject application missing mandatory field(s).
    @Test
    void shouldRejectApplicationMissingFields() {
        LoanApplication incompleteApplication = new LoanApplication(null, "1234567890", 50000);
        LoanProcessingResult result = loanProcessingService.processApplication(incompleteApplication);

        assertFalse(result.isAccepted());
        verify(loanRepository, never()).save(any());
    }

    // Test Case 3: Ensure newly accepted applications are persisted (mock repository).
    @Test
    void shouldPersistAcceptedApplication() {
        LoanApplication validApplication = new LoanApplication("Jane Doe", "0987654321", 75000);
        loanProcessingService.processApplication(validApplication);

        ArgumentCaptor<LoanApplication> captor = ArgumentCaptor.forClass(LoanApplication.class);
        verify(loanRepository).save(captor.capture());
        assertEquals("Jane Doe", captor.getValue().getApplicantName());
        assertEquals("0987654321", captor.getValue().getApplicantId());
        assertEquals(75000, captor.getValue().getAmount());
    }
}