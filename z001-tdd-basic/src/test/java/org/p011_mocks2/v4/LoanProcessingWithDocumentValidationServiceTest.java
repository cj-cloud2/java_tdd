package org.p011_mocks2.v4;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoanProcessingWithDocumentValidationServiceTest {
    private LoanRepository loanRepository;
    private CreditBureauService creditBureauService;
    private HrVerificationService hrVerificationService;
    private DocumentValidationService documentValidationService;
    private LoanProcessingWithDocumentValidationService loanService;

    @BeforeEach
    void setUp() {
        loanRepository = mock(LoanRepository.class);
        creditBureauService = mock(CreditBureauService.class);
        hrVerificationService = mock(HrVerificationService.class);
        documentValidationService = mock(DocumentValidationService.class);
        loanService = new LoanProcessingWithDocumentValidationService(
                loanRepository, creditBureauService, hrVerificationService, documentValidationService);
    }

    // Test Case 1: Accept with all required documents present (mock document service).
    @Test
    void shouldAcceptIfAllDocumentsValid() {
        LoanApplication application = new LoanApplication("Diana", "777888999", 85000);
        when(creditBureauService.fetchCreditScore(application.getApplicantId())).thenReturn(720);
        when(hrVerificationService.verifyEmployment(application.getApplicantId())).thenReturn(EmploymentStatus.VERIFIED);
        when(documentValidationService.validateDocuments(application.getApplicantId())).thenReturn(DocumentValidationStatus.VALID);

        LoanProcessingResult result = loanService.processApplication(application);

        assertTrue(result.isAccepted());
        assertEquals("Application approved with valid documents.", result.getMessage());
        ArgumentCaptor<LoanApplication> captor = ArgumentCaptor.forClass(LoanApplication.class);
        verify(loanRepository).save(captor.capture());
        assertEquals(LoanStatus.APPROVED, captor.getValue().getStatus());
    }
}
