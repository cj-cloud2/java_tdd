
# Assignment 4: Employment Verification Integration

## Business Requirements

The loan processing service must now include employment verification as the final step before loan approval. The system should:

1. Verify applicant's employment status through an HR verification service
2. Approve applications only when employment is confirmed and active
3. Reject applications when employment cannot be verified or is inactive
4. Mark applications as "verification pending" when the HR verification system is unavailable
5. Only proceed with employment verification after all previous validations (basic, documents, credit score) pass

## Testable Requirements

From the business requirements, we derive the following testable requirements:

1. **TR10**: The service should approve applications when employment is confirmed as active (using HR verification service)
2. **TR11**: The service should reject applications when employment verification fails or shows inactive status
3. **TR12**: The service should mark applications as "verification pending" when the HR verification system is unavailable

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


## Test Function 1: Approve When Employment is Confirmed

### RED Phase - Write Failing Test

Create a new test class for employment verification tests:

```java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoanProcessingServiceEmploymentVerificationTest {
    
    private LoanProcessingService loanProcessingService;
    
    @Mock
    private LoanRepository loanRepository;
    
    @Mock
    private DocumentValidationService documentValidationService;
    
    @Mock
    private CreditBureauService creditBureauService;
    
    @Mock
    private EmploymentVerificationService employmentVerificationService;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        loanProcessingService = new LoanProcessingService(
            loanRepository, 
            documentValidationService, 
            creditBureauService,
            employmentVerificationService
        );
    }
    
    @Test
    void shouldApproveApplicationWithConfirmedEmployment() {
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
        when(employmentVerificationService.verifyEmployment("john.doe@email.com"))
            .thenReturn(new EmploymentVerificationResult(true, true, "Employment confirmed - Active", "TechCorp Inc."));
        
        // Act
        LoanProcessingResult result = loanProcessingService.processApplication(application);
        
        // Assert
        assertEquals(LoanProcessingStatus.ACCEPTED, result.getStatus());
        assertNull(result.getErrorMessage());
        verify(employmentVerificationService, times(1)).verifyEmployment("john.doe@email.com");
        verify(loanRepository, times(1)).save(application);
    }
}
```

**Run the test** - It should fail because the new classes and updated constructor don't exist yet.

### GREEN Phase - Make Test Pass

First, create the new classes needed:

**EmploymentVerificationResult.java**

```java
public class EmploymentVerificationResult {
    private boolean isSuccessful;
    private boolean isEmployed;
    private String message;
    private String employer;
    
    public EmploymentVerificationResult(boolean isSuccessful, boolean isEmployed, String message, String employer) {
        this.isSuccessful = isSuccessful;
        this.isEmployed = isEmployed;
        this.message = message;
        this.employer = employer;
    }
    
    public boolean isSuccessful() { return isSuccessful; }
    public boolean isEmployed() { return isEmployed; }
    public String getMessage() { return message; }
    public String getEmployer() { return employer; }
}
```

**EmploymentVerificationService.java**

```java
public interface EmploymentVerificationService {
    EmploymentVerificationResult verifyEmployment(String email);
}
```

Add new status to **LoanProcessingStatus.java**:

```java
public enum LoanProcessingStatus {
    ACCEPTED,
    REJECTED,
    AWAITING_DOCUMENTS,
    VERIFICATION_PENDING
}
```

Update **LoanProcessingService.java** to include employment verification:

```java
import java.util.ArrayList;
import java.util.List;

public class LoanProcessingService {
    private static final int MINIMUM_CREDIT_SCORE = 650;
    
    private LoanRepository loanRepository;
    private DocumentValidationService documentValidationService;
    private CreditBureauService creditBureauService;
    private EmploymentVerificationService employmentVerificationService;
    
    public LoanProcessingService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
        this.documentValidationService = null;
        this.creditBureauService = null;
        this.employmentVerificationService = null;
    }
    
    public LoanProcessingService(LoanRepository loanRepository, DocumentValidationService documentValidationService) {
        this.loanRepository = loanRepository;
        this.documentValidationService = documentValidationService;
        this.creditBureauService = null;
        this.employmentVerificationService = null;
    }
    
    public LoanProcessingService(LoanRepository loanRepository, DocumentValidationService documentValidationService, CreditBureauService creditBureauService) {
        this.loanRepository = loanRepository;
        this.documentValidationService = documentValidationService;
        this.creditBureauService = creditBureauService;
        this.employmentVerificationService = null;
    }
    
    public LoanProcessingService(LoanRepository loanRepository, DocumentValidationService documentValidationService, CreditBureauService creditBureauService, EmploymentVerificationService employmentVerificationService) {
        this.loanRepository = loanRepository;
        this.documentValidationService = documentValidationService;
        this.creditBureauService = creditBureauService;
        this.employmentVerificationService = employmentVerificationService;
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
        
        // Employment verification
        LoanProcessingResult employmentVerificationResult = verifyEmployment(application);
        if (employmentVerificationResult != null) {
            return employmentVerificationResult;
        }
        
        // Persist the valid application
        loanRepository.save(application);
        
        return new LoanProcessingResult(LoanProcessingStatus.ACCEPTED, null);
    }
    
    private LoanProcessingResult verifyEmployment(LoanApplication application) {
        if (employmentVerificationService != null) {
            EmploymentVerificationResult employmentResult = employmentVerificationService.verifyEmployment(application.getEmail());
            
            if (!employmentResult.isSuccessful()) {
                return new LoanProcessingResult(LoanProcessingStatus.VERIFICATION_PENDING, employmentResult.getMessage());
            }
            
            if (!employmentResult.isEmployed()) {
                return new LoanProcessingResult(LoanProcessingStatus.REJECTED, 
                    "Employment verification failed: " + employmentResult.getMessage());
            }
        }
        return null; // No employment verification issues
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

The code is working well with clean separation. Let's move to the next test to drive more functionality.

## Test Function 2: Reject When Employment is Unverified

### RED Phase - Write Failing Test

Add the second test to the employment verification test class:

```java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoanProcessingServiceEmploymentVerificationTest {
    
    private LoanProcessingService loanProcessingService;
    
    @Mock
    private LoanRepository loanRepository;
    
    @Mock
    private DocumentValidationService documentValidationService;
    
    @Mock
    private CreditBureauService creditBureauService;
    
    @Mock
    private EmploymentVerificationService employmentVerificationService;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        loanProcessingService = new LoanProcessingService(
            loanRepository, 
            documentValidationService, 
            creditBureauService,
            employmentVerificationService
        );
    }
    
    @Test
    void shouldApproveApplicationWithConfirmedEmployment() {
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
        when(employmentVerificationService.verifyEmployment("john.doe@email.com"))
            .thenReturn(new EmploymentVerificationResult(true, true, "Employment confirmed - Active", "TechCorp Inc."));
        
        // Act
        LoanProcessingResult result = loanProcessingService.processApplication(application);
        
        // Assert
        assertEquals(LoanProcessingStatus.ACCEPTED, result.getStatus());
        assertNull(result.getErrorMessage());
        verify(employmentVerificationService, times(1)).verifyEmployment("john.doe@email.com");
        verify(loanRepository, times(1)).save(application);
    }
    
    @Test
    void shouldRejectApplicationWhenEmploymentUnverified() {
        // Arrange
        List<Document> documents = Arrays.asList(
            new Document("ID_PROOF", "valid-id-doc.pdf"),
            new Document("INCOME_PROOF", "salary-slip.pdf"),
            new Document("ADDRESS_PROOF", "utility-bill.pdf")
        );
        
        LoanApplication applicationWithUnverifiedEmployment = new LoanApplication(
            "Jane Smith",
            "jane.smith@email.com", 
            "0987654321",
            35000.0,
            "Personal Loan",
            documents
        );
        
        when(documentValidationService.validateDocuments(documents))
            .thenReturn(new DocumentValidationResult(true, "All documents are valid", null));
        when(creditBureauService.getCreditScore("0987654321"))
            .thenReturn(new CreditScoreResult(true, 680, "Credit score retrieved successfully"));
        when(employmentVerificationService.verifyEmployment("jane.smith@email.com"))
            .thenReturn(new EmploymentVerificationResult(true, false, "Employment not found or inactive", null));
        
        // Act
        LoanProcessingResult result = loanProcessingService.processApplication(applicationWithUnverifiedEmployment);
        
        // Assert
        assertEquals(LoanProcessingStatus.REJECTED, result.getStatus());
        assertTrue(result.getErrorMessage().contains("Employment verification failed"));
        assertTrue(result.getErrorMessage().contains("Employment not found or inactive"));
        verify(employmentVerificationService, times(1)).verifyEmployment("jane.smith@email.com");
        verify(loanRepository, never()).save(any());
    }
}
```

**Run both tests** - The new test should pass because our current implementation already handles unverified employment.

### GREEN Phase - Already Passing

The test passes with our current implementation! Our service correctly rejects applications when employment verification fails.

### REFACTOR Phase

The implementation is working correctly. No refactoring needed at this point.

## Test Function 3: Handle HR System Unavailability

### RED Phase - Write Failing Test

Add the third test to handle HR system unavailability:

```java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoanProcessingServiceEmploymentVerificationTest {
    
    private LoanProcessingService loanProcessingService;
    
    @Mock
    private LoanRepository loanRepository;
    
    @Mock
    private DocumentValidationService documentValidationService;
    
    @Mock
    private CreditBureauService creditBureauService;
    
    @Mock
    private EmploymentVerificationService employmentVerificationService;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        loanProcessingService = new LoanProcessingService(
            loanRepository, 
            documentValidationService, 
            creditBureauService,
            employmentVerificationService
        );
    }
    
    @Test
    void shouldApproveApplicationWithConfirmedEmployment() {
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
        when(employmentVerificationService.verifyEmployment("john.doe@email.com"))
            .thenReturn(new EmploymentVerificationResult(true, true, "Employment confirmed - Active", "TechCorp Inc."));
        
        // Act
        LoanProcessingResult result = loanProcessingService.processApplication(application);
        
        // Assert
        assertEquals(LoanProcessingStatus.ACCEPTED, result.getStatus());
        assertNull(result.getErrorMessage());
        verify(employmentVerificationService, times(1)).verifyEmployment("john.doe@email.com");
        verify(loanRepository, times(1)).save(application);
    }
    
    @Test
    void shouldRejectApplicationWhenEmploymentUnverified() {
        // Arrange
        List<Document> documents = Arrays.asList(
            new Document("ID_PROOF", "valid-id-doc.pdf"),
            new Document("INCOME_PROOF", "salary-slip.pdf"),
            new Document("ADDRESS_PROOF", "utility-bill.pdf")
        );
        
        LoanApplication applicationWithUnverifiedEmployment = new LoanApplication(
            "Jane Smith",
            "jane.smith@email.com", 
            "0987654321",
            35000.0,
            "Personal Loan",
            documents
        );
        
        when(documentValidationService.validateDocuments(documents))
            .thenReturn(new DocumentValidationResult(true, "All documents are valid", null));
        when(creditBureauService.getCreditScore("0987654321"))
            .thenReturn(new CreditScoreResult(true, 680, "Credit score retrieved successfully"));
        when(employmentVerificationService.verifyEmployment("jane.smith@email.com"))
            .thenReturn(new EmploymentVerificationResult(true, false, "Employment not found or inactive", null));
        
        // Act
        LoanProcessingResult result = loanProcessingService.processApplication(applicationWithUnverifiedEmployment);
        
        // Assert
        assertEquals(LoanProcessingStatus.REJECTED, result.getStatus());
        assertTrue(result.getErrorMessage().contains("Employment verification failed"));
        assertTrue(result.getErrorMessage().contains("Employment not found or inactive"));
        verify(employmentVerificationService, times(1)).verifyEmployment("jane.smith@email.com");
        verify(loanRepository, never()).save(any());
    }
    
    @Test
    void shouldMarkApplicationAsVerificationPendingWhenHRSystemUnavailable() {
        // Arrange
        List<Document> documents = Arrays.asList(
            new Document("ID_PROOF", "valid-id-doc.pdf"),
            new Document("INCOME_PROOF", "salary-slip.pdf"),
            new Document("ADDRESS_PROOF", "utility-bill.pdf")
        );
        
        LoanApplication applicationWithHRSystemDown = new LoanApplication(
            "Bob Wilson",
            "bob.wilson@email.com", 
            "5555555555",
            60000.0,
            "Business Loan",
            documents
        );
        
        when(documentValidationService.validateDocuments(documents))
            .thenReturn(new DocumentValidationResult(true, "All documents are valid", null));
        when(creditBureauService.getCreditScore("5555555555"))
            .thenReturn(new CreditScoreResult(true, 720, "Credit score retrieved successfully"));
        when(employmentVerificationService.verifyEmployment("bob.wilson@email.com"))
            .thenReturn(new EmploymentVerificationResult(false, false, "HR verification system is temporarily unavailable", null));
        
        // Act
        LoanProcessingResult result = loanProcessingService.processApplication(applicationWithHRSystemDown);
        
        // Assert
        assertEquals(LoanProcessingStatus.VERIFICATION_PENDING, result.getStatus());
        assertTrue(result.getErrorMessage().contains("HR verification system is temporarily unavailable"));
        verify(employmentVerificationService, times(1)).verifyEmployment("bob.wilson@email.com");
        verify(loanRepository, never()).save(any());
    }
}
```

**Run all tests** - The new test should pass because our current implementation already handles system failures by checking `isSuccessful()`.

### GREEN Phase - Already Passing

The test passes with our current implementation! Our service correctly marks applications as "verification pending" when the HR system is unavailable.

### REFACTOR Phase

Our implementation is complete and handles all the required scenarios correctly. Let's do a final refactor to clean up the constructor overloading:

```java
import java.util.ArrayList;
import java.util.List;

public class LoanProcessingService {
    private static final int MINIMUM_CREDIT_SCORE = 650;
    
    private final LoanRepository loanRepository;
    private final DocumentValidationService documentValidationService;
    private final CreditBureauService creditBureauService;
    private final EmploymentVerificationService employmentVerificationService;
    
    public LoanProcessingService(LoanRepository loanRepository) {
        this(loanRepository, null, null, null);
    }
    
    public LoanProcessingService(LoanRepository loanRepository, DocumentValidationService documentValidationService) {
        this(loanRepository, documentValidationService, null, null);
    }
    
    public LoanProcessingService(LoanRepository loanRepository, DocumentValidationService documentValidationService, CreditBureauService creditBureauService) {
        this(loanRepository, documentValidationService, creditBureauService, null);
    }
    
    public LoanProcessingService(LoanRepository loanRepository, DocumentValidationService documentValidationService, CreditBureauService creditBureauService, EmploymentVerificationService employmentVerificationService) {
        this.loanRepository = loanRepository;
        this.documentValidationService = documentValidationService;
        this.creditBureauService = creditBureauService;
        this.employmentVerificationService = employmentVerificationService;
    }
    
    public LoanProcessingResult processApplication(LoanApplication application) {
        // Basic validation
        LoanProcessingResult basicValidationResult = performBasicValidation(application);
        if (basicValidationResult != null) {
            return basicValidationResult;
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
        
        // Employment verification
        LoanProcessingResult employmentVerificationResult = verifyEmployment(application);
        if (employmentVerificationResult != null) {
            return employmentVerificationResult;
        }
        
        // All validations passed - persist and approve
        loanRepository.save(application);
        return new LoanProcessingResult(LoanProcessingStatus.ACCEPTED, null);
    }
    
    private LoanProcessingResult performBasicValidation(LoanApplication application) {
        List<String> validationErrors = validateApplication(application);
        
        if (!validationErrors.isEmpty()) {
            return new LoanProcessingResult(LoanProcessingStatus.REJECTED, 
                String.join(", ", validationErrors));
        }
        return null;
    }
    
    private LoanProcessingResult verifyEmployment(LoanApplication application) {
        if (employmentVerificationService != null) {
            EmploymentVerificationResult employmentResult = employmentVerificationService.verifyEmployment(application.getEmail());
            
            if (!employmentResult.isSuccessful()) {
                return new LoanProcessingResult(LoanProcessingStatus.VERIFICATION_PENDING, employmentResult.getMessage());
            }
            
            if (!employmentResult.isEmployed()) {
                return new LoanProcessingResult(LoanProcessingStatus.REJECTED, 
                    "Employment verification failed: " + employmentResult.getMessage());
            }
        }
        return null; // No employment verification issues
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

**Run all tests** - They should all still pass after refactoring.

## Assignment 4 Complete

Congratulations! You have successfully completed Assignment 4 using Test-Driven Development. Your `LoanProcessingService` is now a comprehensive, production-ready loan processing system that includes:

1. ✅ **Basic loan application validation** (from Assignment 1)
2. ✅ **Document validation layer** (from Assignment 2)
3. ✅ **Credit score verification** (from Assignment 3)
4. ✅ **Employment verification integration** with HR systems
5. ✅ **Complete workflow orchestration** with proper status handling

### Summary of What We Added

**New Classes:**

- **EmploymentVerificationResult**: Result object containing employment status and details
- **EmploymentVerificationService**: Interface for HR system integration
- **LoanProcessingStatus.VERIFICATION_PENDING**: New status for pending verifications

**Enhanced Classes:**

- **LoanProcessingService**: Added employment verification as the final validation step


### Current Service Capabilities

The service now provides a complete end-to-end loan processing workflow:

**Processing Pipeline:**

1. **Basic Validation** → Required fields validation
2. **Document Verification** → Complete document validation workflow
3. **Credit Assessment** → Credit score verification (minimum 650)
4. **Employment Verification** → Active employment confirmation
5. **Final Approval** → Persistence and approval notification

**Status Management:**

- **ACCEPTED**: All validations passed, loan approved
- **REJECTED**: Failed validation at any step
- **AWAITING_DOCUMENTS**: Missing required documents
- **VERIFICATION_PENDING**: External system unavailable


### Production-Ready Features

- **Fail-Fast Processing**: Stops at first validation failure
- **External Service Integration**: Graceful handling of service unavailability
- **Clear Status Communication**: Detailed error messages for each failure type
- **Backward Compatibility**: All constructors maintained for different integration scenarios
- **Separation of Concerns**: Each validation step is isolated and testable

**Key TDD Learnings from Assignment 4:**

- Built a complete production-ready service through iterative TDD cycles
- Successfully integrated multiple external dependencies without breaking existing functionality
- Implemented robust error handling and status management
- Maintained clean architecture with single responsibility principle
- Demonstrated how TDD enables confident refactoring even in complex systems

Your `LoanProcessingService` is now ready for production use and can handle the complete loan approval process with proper validation, external service integration, and comprehensive error handling!

