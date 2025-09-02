
# Assignment 3: Integrate Credit Score Check

## Business Requirements

The loan processing service must now incorporate credit score verification to determine loan eligibility. The system should:

1. Fetch the applicant's credit score from an external credit bureau service
2. Accept applications only if the credit score meets the minimum threshold (650 or above)
3. Reject applications with credit scores below the minimum threshold
4. Handle scenarios where the credit bureau service is unavailable or fails to respond
5. Only proceed with credit checks after basic validation and document validation are complete

## Testable Requirements

From the business requirements, we derive the following testable requirements:

1. **TR7**: The service should accept applications when the applicant's credit score is above the minimum threshold (using credit bureau service)
2. **TR8**: The service should reject applications when the applicant's credit score is below the minimum threshold
3. **TR9**: The service should handle credit bureau service failures gracefully (timeout, unavailable service)

## Dependencies

Ensure your project still includes the same dependencies from previous assignments:

```xml
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.9.2</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <version>5.18.2</version>
    <scope>test</scope>
</dependency>
```


## Test Function 1: Accept Application if Credit Score Above Threshold

### RED Phase - Write Failing Test

Create a new test class for credit score validation tests:

```java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoanProcessingServiceCreditScoreTest {
    
    private LoanProcessingService loanProcessingService;
    
    @Mock
    private LoanRepository loanRepository;
    
    @Mock
    private DocumentValidationService documentValidationService;
    
    @Mock
    private CreditBureauService creditBureauService;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        loanProcessingService = new LoanProcessingService(loanRepository, documentValidationService, creditBureauService);
    }
    
    @Test
    void shouldAcceptApplicationWithHighCreditScore() {
        // Arrange
        List<Document> documents = Arrays.asList(
            new Document("ID_PROOF", "valid-id-doc.pdf"),
            new Document("INCOME_PROOF", "salary-slip.pdf"),
            new Document("ADDRESS_PROOF", "utility-bill.pdf")
        );
        
        LoanApplication application = new LoanApplication(
            "John Doe",
            "john.doe@email.com", 
            "1234567890",
            50000.0,
            "Home Purchase",
            documents
        );
        
        when(documentValidationService.validateDocuments(documents))
            .thenReturn(new DocumentValidationResult(true, "All documents are valid", null));
        when(creditBureauService.getCreditScore("1234567890"))
            .thenReturn(new CreditScoreResult(true, 720, "Credit score retrieved successfully"));
        
        // Act
        LoanProcessingResult result = loanProcessingService.processApplication(application);
        
        // Assert
        assertEquals(LoanProcessingStatus.ACCEPTED, result.getStatus());
        assertNull(result.getErrorMessage());
        verify(creditBureauService, times(1)).getCreditScore("1234567890");
        verify(loanRepository, times(1)).save(application);
    }
}
```

**Run the test** - It should fail because the new classes and updated constructor don't exist yet.

### GREEN Phase - Make Test Pass

First, create the new classes needed:

**CreditScoreResult.java**

```java
public class CreditScoreResult {
    private boolean isSuccessful;
    private Integer creditScore;
    private String message;
    
    public CreditScoreResult(boolean isSuccessful, Integer creditScore, String message) {
        this.isSuccessful = isSuccessful;
        this.creditScore = creditScore;
        this.message = message;
    }
    
    public boolean isSuccessful() { return isSuccessful; }
    public Integer getCreditScore() { return creditScore; }
    public String getMessage() { return message; }
}
```

**CreditBureauService.java**

```java
public interface CreditBureauService {
    CreditScoreResult getCreditScore(String phoneNumber);
}
```

Update **LoanProcessingService.java** to include credit score validation:

```java
import java.util.ArrayList;
import java.util.List;

public class LoanProcessingService {
    private static final int MINIMUM_CREDIT_SCORE = 650;
    
    private LoanRepository loanRepository;
    private DocumentValidationService documentValidationService;
    private CreditBureauService creditBureauService;
    
    public LoanProcessingService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
        this.documentValidationService = null;
        this.creditBureauService = null;
    }
    
    public LoanProcessingService(LoanRepository loanRepository, DocumentValidationService documentValidationService) {
        this.loanRepository = loanRepository;
        this.documentValidationService = documentValidationService;
        this.creditBureauService = null;
    }
    
    public LoanProcessingService(LoanRepository loanRepository, DocumentValidationService documentValidationService, CreditBureauService creditBureauService) {
        this.loanRepository = loanRepository;
        this.documentValidationService = documentValidationService;
        this.creditBureauService = creditBureauService;
    }
    
    public LoanProcessingResult processApplication(LoanApplication application) {
        List<String> validationErrors = validateApplication(application);
        
        if (!validationErrors.isEmpty()) {
            return new LoanProcessingResult(LoanProcessingStatus.REJECTED, 
                String.join(", ", validationErrors));
        }
        
        // Document validation
        LoanProcessingResult documentValidationResult = validateDocuments(application);
        if (documentValidationResult != null) {
            return documentValidationResult;
        }
        
        // Credit score validation
        LoanProcessingResult creditScoreResult = validateCreditScore(application);
        if (creditScoreResult != null) {
            return creditScoreResult;
        }
        
        // Persist the valid application
        loanRepository.save(application);
        
        return new LoanProcessingResult(LoanProcessingStatus.ACCEPTED, null);
    }
    
    private LoanProcessingResult validateCreditScore(LoanApplication application) {
        if (creditBureauService != null) {
            CreditScoreResult creditResult = creditBureauService.getCreditScore(application.getPhone());
            
            if (!creditResult.isSuccessful()) {
                return new LoanProcessingResult(LoanProcessingStatus.REJECTED, creditResult.getMessage());
            }
            
            if (creditResult.getCreditScore() < MINIMUM_CREDIT_SCORE) {
                return new LoanProcessingResult(LoanProcessingStatus.REJECTED, 
                    "Credit score " + creditResult.getCreditScore() + " is below minimum required score of " + MINIMUM_CREDIT_SCORE);
            }
        }
        return null; // No credit score validation issues
    }
    
    private LoanProcessingResult validateDocuments(LoanApplication application) {
        if (documentValidationService != null && application.getDocuments() != null) {
            DocumentValidationResult documentResult = documentValidationService.validateDocuments(application.getDocuments());
            
            if (!documentResult.isValid()) {
                if (documentResult.getMissingDocuments() != null && !documentResult.getMissingDocuments().isEmpty()) {
                    return new LoanProcessingResult(LoanProcessingStatus.AWAITING_DOCUMENTS, documentResult.getMessage());
                } else {
                    return new LoanProcessingResult(LoanProcessingStatus.REJECTED, documentResult.getMessage());
                }
            }
        }
        return null; // No document validation issues
    }
    
    private List<String> validateApplication(LoanApplication application) {
        List<String> errors = new ArrayList<>();
        
        if (isNullOrEmpty(application.getApplicantName())) {
            errors.add("Applicant name is required");
        }
        
        if (isNullOrEmpty(application.getEmail())) {
            errors.add("Email is required");
        }
        
        if (isNullOrEmpty(application.getPhone())) {
            errors.add("Phone number is required");
        }
        
        if (application.getLoanAmount() == null || application.getLoanAmount() <= 0) {
            errors.add("Valid loan amount is required");
        }
        
        if (isNullOrEmpty(application.getLoanPurpose())) {
            errors.add("Loan purpose is required");
        }
        
        return errors;
    }
    
    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}
```

**Run the test** - It should now pass.

### REFACTOR Phase

The code is working well with clear separation of concerns. Let's move to the next test to drive more functionality.

## Test Function 2: Reject Application for Low Credit Score

### RED Phase - Write Failing Test

Add the second test to the credit score validation test class:

```java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoanProcessingServiceCreditScoreTest {
    
    private LoanProcessingService loanProcessingService;
    
    @Mock
    private LoanRepository loanRepository;
    
    @Mock
    private DocumentValidationService documentValidationService;
    
    @Mock
    private CreditBureauService creditBureauService;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        loanProcessingService = new LoanProcessingService(loanRepository, documentValidationService, creditBureauService);
    }
    
    @Test
    void shouldAcceptApplicationWithHighCreditScore() {
        // Arrange
        List<Document> documents = Arrays.asList(
            new Document("ID_PROOF", "valid-id-doc.pdf"),
            new Document("INCOME_PROOF", "salary-slip.pdf"),
            new Document("ADDRESS_PROOF", "utility-bill.pdf")
        );
        
        LoanApplication application = new LoanApplication(
            "John Doe",
            "john.doe@email.com", 
            "1234567890",
            50000.0,
            "Home Purchase",
            documents
        );
        
        when(documentValidationService.validateDocuments(documents))
            .thenReturn(new DocumentValidationResult(true, "All documents are valid", null));
        when(creditBureauService.getCreditScore("1234567890"))
            .thenReturn(new CreditScoreResult(true, 720, "Credit score retrieved successfully"));
        
        // Act
        LoanProcessingResult result = loanProcessingService.processApplication(application);
        
        // Assert
        assertEquals(LoanProcessingStatus.ACCEPTED, result.getStatus());
        assertNull(result.getErrorMessage());
        verify(creditBureauService, times(1)).getCreditScore("1234567890");
        verify(loanRepository, times(1)).save(application);
    }
    
    @Test
    void shouldRejectApplicationWithLowCreditScore() {
        // Arrange
        List<Document> documents = Arrays.asList(
            new Document("ID_PROOF", "valid-id-doc.pdf"),
            new Document("INCOME_PROOF", "salary-slip.pdf"),
            new Document("ADDRESS_PROOF", "utility-bill.pdf")
        );
        
        LoanApplication applicationWithLowCredit = new LoanApplication(
            "Jane Smith",
            "jane.smith@email.com", 
            "0987654321",
            25000.0,
            "Personal Loan",
            documents
        );
        
        when(documentValidationService.validateDocuments(documents))
            .thenReturn(new DocumentValidationResult(true, "All documents are valid", null));
        when(creditBureauService.getCreditScore("0987654321"))
            .thenReturn(new CreditScoreResult(true, 580, "Credit score retrieved successfully"));
        
        // Act
        LoanProcessingResult result = loanProcessingService.processApplication(applicationWithLowCredit);
        
        // Assert
        assertEquals(LoanProcessingStatus.REJECTED, result.getStatus());
        assertTrue(result.getErrorMessage().contains("Credit score 580 is below minimum required score of 650"));
        verify(creditBureauService, times(1)).getCreditScore("0987654321");
        verify(loanRepository, never()).save(any());
    }
}
```

**Run both tests** - The new test should pass because our current implementation already handles low credit scores.

### GREEN Phase - Already Passing

The test is already passing with our current implementation! This demonstrates that our credit score validation logic correctly rejects applications with scores below the threshold.

### REFACTOR Phase

The implementation is working correctly. No refactoring needed at this point.

## Test Function 3: Handle Credit Bureau Service Failures

### RED Phase - Write Failing Test

Add the third test to handle service failures:

```java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoanProcessingServiceCreditScoreTest {
    
    private LoanProcessingService loanProcessingService;
    
    @Mock
    private LoanRepository loanRepository;
    
    @Mock
    private DocumentValidationService documentValidationService;
    
    @Mock
    private CreditBureauService creditBureauService;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        loanProcessingService = new LoanProcessingService(loanRepository, documentValidationService, creditBureauService);
    }
    
    @Test
    void shouldAcceptApplicationWithHighCreditScore() {
        // Arrange
        List<Document> documents = Arrays.asList(
            new Document("ID_PROOF", "valid-id-doc.pdf"),
            new Document("INCOME_PROOF", "salary-slip.pdf"),
            new Document("ADDRESS_PROOF", "utility-bill.pdf")
        );
        
        LoanApplication application = new LoanApplication(
            "John Doe",
            "john.doe@email.com", 
            "1234567890",
            50000.0,
            "Home Purchase",
            documents
        );
        
        when(documentValidationService.validateDocuments(documents))
            .thenReturn(new DocumentValidationResult(true, "All documents are valid", null));
        when(creditBureauService.getCreditScore("1234567890"))
            .thenReturn(new CreditScoreResult(true, 720, "Credit score retrieved successfully"));
        
        // Act
        LoanProcessingResult result = loanProcessingService.processApplication(application);
        
        // Assert
        assertEquals(LoanProcessingStatus.ACCEPTED, result.getStatus());
        assertNull(result.getErrorMessage());
        verify(creditBureauService, times(1)).getCreditScore("1234567890");
        verify(loanRepository, times(1)).save(application);
    }
    
    @Test
    void shouldRejectApplicationWithLowCreditScore() {
        // Arrange
        List<Document> documents = Arrays.asList(
            new Document("ID_PROOF", "valid-id-doc.pdf"),
            new Document("INCOME_PROOF", "salary-slip.pdf"),
            new Document("ADDRESS_PROOF", "utility-bill.pdf")
        );
        
        LoanApplication applicationWithLowCredit = new LoanApplication(
            "Jane Smith",
            "jane.smith@email.com", 
            "0987654321",
            25000.0,
            "Personal Loan",
            documents
        );
        
        when(documentValidationService.validateDocuments(documents))
            .thenReturn(new DocumentValidationResult(true, "All documents are valid", null));
        when(creditBureauService.getCreditScore("0987654321"))
            .thenReturn(new CreditScoreResult(true, 580, "Credit score retrieved successfully"));
        
        // Act
        LoanProcessingResult result = loanProcessingService.processApplication(applicationWithLowCredit);
        
        // Assert
        assertEquals(LoanProcessingStatus.REJECTED, result.getStatus());
        assertTrue(result.getErrorMessage().contains("Credit score 580 is below minimum required score of 650"));
        verify(creditBureauService, times(1)).getCreditScore("0987654321");
        verify(loanRepository, never()).save(any());
    }
    
    @Test
    void shouldHandleCreditBureauServiceFailure() {
        // Arrange
        List<Document> documents = Arrays.asList(
            new Document("ID_PROOF", "valid-id-doc.pdf"),
            new Document("INCOME_PROOF", "salary-slip.pdf"),
            new Document("ADDRESS_PROOF", "utility-bill.pdf")
        );
        
        LoanApplication applicationWithServiceFailure = new LoanApplication(
            "Bob Wilson",
            "bob.wilson@email.com", 
            "5555555555",
            80000.0,
            "Business Loan",
            documents
        );
        
        when(documentValidationService.validateDocuments(documents))
            .thenReturn(new DocumentValidationResult(true, "All documents are valid", null));
        when(creditBureauService.getCreditScore("5555555555"))
            .thenReturn(new CreditScoreResult(false, null, "Credit bureau service is currently unavailable"));
        
        // Act
        LoanProcessingResult result = loanProcessingService.processApplication(applicationWithServiceFailure);
        
        // Assert
        assertEquals(LoanProcessingStatus.REJECTED, result.getStatus());
        assertTrue(result.getErrorMessage().contains("Credit bureau service is currently unavailable"));
        verify(creditBureauService, times(1)).getCreditScore("5555555555");
        verify(loanRepository, never()).save(any());
    }
}
```

**Run all tests** - The new test should pass because our current implementation already handles service failures by checking `isSuccessful()`.

### GREEN Phase - Already Passing

The test passes with our current implementation! Our service correctly handles credit bureau failures by rejecting applications when the service is unavailable.

### REFACTOR Phase

Our current implementation is working well, but we might want to consider if service failures should be handled differently (perhaps with a different status like "PENDING"). However, for this assignment, rejecting due to service unavailability is appropriate. No refactoring needed.

## Assignment 3 Complete

Congratulations! You have successfully completed Assignment 3 using Test-Driven Development. Your `LoanProcessingService` now includes:

1. ✅ **Basic loan application validation** (from Assignment 1)
2. ✅ **Document validation layer** (from Assignment 2)
3. ✅ **Credit score verification** with minimum threshold enforcement
4. ✅ **Credit bureau service integration** with failure handling
5. ✅ **Proper rejection for low credit scores**

### Summary of What We Added

**New Classes:**

- **CreditScoreResult**: Result object containing credit score and status
- **CreditBureauService**: Interface for credit bureau integration

**Enhanced Classes:**

- **LoanProcessingService**: Added credit score validation workflow with configurable minimum threshold (650)


### Current Service Capabilities

The service now provides comprehensive loan application processing:

- **Basic Validation**: Required fields validation
- **Document Verification**: Complete document validation workflow
- **Credit Assessment**: Credit score verification with minimum threshold
- **Multi-step Processing**: Applications flow through validation → documents → credit check → approval
- **Failure Handling**: Graceful handling of external service failures


### Processing Flow

1. **Basic Validation**: Check required application fields
2. **Document Validation**: Verify all documents are present and valid
3. **Credit Score Check**: Verify applicant meets minimum credit requirements (650+)
4. **Persistence**: Save approved applications to repository

**Key TDD Learnings from Assignment 3:**

- Successfully integrated a third external service without breaking existing functionality
- Maintained clean separation of concerns with dedicated validation methods
- Applied consistent error handling patterns across all validation layers
- Used dependency injection to maintain testability with multiple dependencies
