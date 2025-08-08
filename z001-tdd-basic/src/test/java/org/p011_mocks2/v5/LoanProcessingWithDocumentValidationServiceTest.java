package org.p011_mocks2.v5;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.p011_mocks2.v4.*;
import org.p011_mocks2.v4.CreditBureauService;
import org.p011_mocks2.v4.DocumentValidationService;
import org.p011_mocks2.v4.DocumentValidationStatus;
import org.p011_mocks2.v4.EmploymentStatus;
import org.p011_mocks2.v4.HrVerificationService;
import org.p011_mocks2.v4.LoanApplication;
import org.p011_mocks2.v4.LoanProcessingResult;
import org.p011_mocks2.v4.LoanProcessingWithDocumentValidationService;
import org.p011_mocks2.v4.LoanRepository;
import org.p011_mocks2.v4.LoanStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class LoanProcessingWithDocumentValidationServiceTest {
    private org.p011_mocks2.v4.LoanRepository loanRepository;
    private org.p011_mocks2.v4.CreditBureauService creditBureauService;
    private org.p011_mocks2.v4.HrVerificationService hrVerificationService;
    private org.p011_mocks2.v4.DocumentValidationService documentValidationService;
    private org.p011_mocks2.v4.LoanProcessingWithDocumentValidationService loanService;

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
        org.p011_mocks2.v4.LoanApplication application = new org.p011_mocks2.v4.LoanApplication("Diana", "777888999", 85000);
        when(creditBureauService.fetchCreditScore(application.getApplicantId())).thenReturn(720);
        when(hrVerificationService.verifyEmployment(application.getApplicantId())).thenReturn(EmploymentStatus.VERIFIED);
        when(documentValidationService.validateDocuments(application.getApplicantId())).thenReturn(DocumentValidationStatus.VALID);

        LoanProcessingResult result = loanService.processApplication(application);

        assertTrue(result.isAccepted());
        assertEquals("Application approved with valid documents.", result.getMessage());
        ArgumentCaptor<org.p011_mocks2.v4.LoanApplication> captor = ArgumentCaptor.forClass(LoanApplication.class);
        verify(loanRepository).save(captor.capture());
        assertEquals(LoanStatus.APPROVED, captor.getValue().getStatus());
    }
}
