# Assignment 4: Employment Verification Integration - Challenge Document

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

## Supporting Classes (Provided)

The following supporting classes are provided as-is to support your TDD implementation:

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

**Updated LoanProcessingStatus.java** (Enhanced with new status)
```java
public enum LoanProcessingStatus {
    ACCEPTED,
    REJECTED,
    AWAITING_DOCUMENTS,
    VERIFICATION_PENDING
}
```

**All Previous Classes** (From Assignments 1, 2 & 3)
- Complete `Document` class
- Complete `DocumentValidationResult` class  
- Complete `DocumentValidationService` interface
- Complete `CreditScoreResult` class
- Complete `CreditBureauService` interface
- Complete `LoanApplication` class with document support
- Complete `LoanProcessingResult` class
- Complete `LoanRepository` interface

## Your Challenge: Implement Employment Verification Using TDD

Follow the TDD lifecycle (RED-GREEN-REFACTOR) to enhance the existing LoanProcessingService and implement the following test functions.

### Test Function 1: Approve Application When Employment is Confirmed

#### RED Phase - Write Failing Test

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
        // TODO: Initialize mockito annotations and create service instance with all four dependencies
    }
    
    @Test
    void shouldApproveApplicationWithConfirmedEmployment() {
        // TODO: Write test that:
        // 1. Creates a list of valid documents (ID_PROOF, INCOME_PROOF, ADDRESS_PROOF)
        // 2. Creates a LoanApplication with all required fields and documents
        // 3. Mocks documentValidationService.validateDocuments() to return valid result
        // 4. Mocks creditBureauService.getCreditScore() to return successful result with score >= 650
        // 5. Mocks employmentVerificationService.verifyEmployment() to return successful result with employed = true
        // 6. Calls processApplication method on the service
        // 7. Asserts that result status is ACCEPTED
        // 8. Asserts that error message is null
        // 9. Verifies that employmentVerificationService.verifyEmployment was called once with email
        // 10. Verifies that loanRepository.save was called once with application
    }
}
```

#### GREEN Phase - Make Test Pass

**Updated LoanProcessingService.java**
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
        // TODO: Initialize repository field and set other services to null
    }
    
    public LoanProcessingService(LoanRepository loanRepository, DocumentValidationService documentValidationService) {
        // TODO: Initialize repository and documentValidationService fields, set others to null
    }
    
    public LoanProcessingService(LoanRepository loanRepository, DocumentValidationService documentValidationService, CreditBureauService creditBureauService) {
        // TODO: Initialize repository, documentValidationService, and creditBureauService fields, set employmentVerificationService to null
    }
    
    public LoanProcessingService(LoanRepository loanRepository, DocumentValidationService documentValidationService, CreditBureauService creditBureauService, EmploymentVerificationService employmentVerificationService) {
        // TODO: Initialize all four service fields
    }
    
    public LoanProcessingResult processApplication(LoanApplication application) {
        // TODO: Implement complete processing logic:
        // 1. Validate basic application fields (reuse existing logic)
        // 2. If basic validation fails, return REJECTED with error messages
        // 3. Validate documents if documentValidationService is available
        // 4. If document validation fails, return appropriate result (AWAITING_DOCUMENTS or REJECTED)
        // 5. Validate credit score if creditBureauService is available
        // 6. If credit score validation fails, return REJECTED with error message
        // 7. Verify employment if employmentVerificationService is available
        // 8. If employment verification fails, return appropriate result (VERIFICATION_PENDING or REJECTED)
        // 9. If all validations pass, save application and return ACCEPTED
    }
    
    private LoanProcessingResult verifyEmployment(LoanApplication application) {
        // TODO: Implement employment verification:
        // 1. Check if employmentVerificationService is available
        // 2. Call employmentVerificationService.verifyEmployment() with applicant's email
        // 3. If service call was not successful, return VERIFICATION_PENDING with service error message
        // 4. If employment is not verified (isEmployed = false), return REJECTED with appropriate message
        // 5. Return null if no employment verification issues (employment is confirmed)
    }
    
    private LoanProcessingResult validateCreditScore(LoanApplication application) {
        // TODO: Reimplement credit score validation from Assignment 3:
        // 1. Check if creditBureauService is available
        // 2. Call creditBureauService.getCreditScore() with applicant's phone number
        // 3. If service call was not successful, return REJECTED with service error message
        // 4. If credit score is below MINIMUM_CREDIT_SCORE, return REJECTED with appropriate message
        // 5. Return null if no credit score validation issues
    }
    
    private LoanProcessingResult validateDocuments(LoanApplication application) {
        // TODO: Reimplement document validation from Assignment 2:
        // 1. Check if documentValidationService is available and application has documents
        // 2. Call documentValidationService.validateDocuments()
        // 3. If documents are invalid:
        //    a. Check if missingDocuments list exists (AWAITING_DOCUMENTS)
        //    b. Otherwise return REJECTED for invalid documents
        // 4. Return null if no document validation issues
    }
    
    private List<String> validateApplication(LoanApplication application) {
        // TODO: Reimplement basic validation from Assignment 1:
        // 1. Create empty list of errors
        // 2. Check each required field (name, email, phone, loan amount, loan purpose)
        // 3. Add appropriate error messages for missing/invalid fields
        // 4. Return the list of errors
    }
    
    private boolean isNullOrEmpty(String value) {
        // TODO: Helper method to check if string is null or empty after trimming
    }
}
```

### Test Function 2: Reject Application When Employment is Unverified

#### RED Phase - Write Failing Test

Add the second test to the employment verification test class:

```java
@Test
void shouldRejectApplicationWhenEmploymentUnverified() {
    // TODO: Write test that:
    // 1. Creates a list of valid documents
    // 2. Creates a LoanApplication with all required fields and documents
    // 3. Mocks documentValidationService.validateDocuments() to return valid result
    // 4. Mocks creditBureauService.getCreditScore() to return successful result with score >= 650
    // 5. Mocks employmentVerificationService.verifyEmployment() to return successful result with employed = false
    // 6. Calls processApplication method on the service
    // 7. Asserts that result status is REJECTED
    // 8. Asserts that error message contains information about employment verification failure
    // 9. Verifies that employmentVerificationService.verifyEmployment was called once with email
    // 10. Verifies that loanRepository.save was never called
}
```

#### GREEN Phase - Make Test Pass

Update the `verifyEmployment` method in LoanProcessingService:

```java
private LoanProcessingResult verifyEmployment(LoanApplication application) {
    // TODO: Enhanced implementation:
    // 1. Check if employmentVerificationService is available
    // 2. Call employmentVerificationService.verifyEmployment() with applicant's email
    // 3. If service call was not successful, return VERIFICATION_PENDING with service error message
    // 4. If employment is not verified (isEmployed = false):
    //    a. Create error message: "Employment verification failed: {service message}"
    //    b. Return REJECTED with this error message
    // 5. Return null if no employment verification issues (employment is confirmed and active)
}
```

### Test Function 3: Handle HR System Unavailability

#### RED Phase - Write Failing Test

Add the third test to handle HR system failures:

```java
@Test
void shouldMarkApplicationAsVerificationPendingWhenHRSystemUnavailable() {
    // TODO: Write test that:
    // 1. Creates a list of valid documents
    // 2. Creates a LoanApplication with all required fields and documents
    // 3. Mocks documentValidationService.validateDocuments() to return valid result
    // 4. Mocks creditBureauService.getCreditScore() to return successful result with score >= 650
    // 5. Mocks employmentVerificationService.verifyEmployment() to return unsuccessful result 
    //    (isSuccessful = false, isEmployed = false, message = system unavailable error)
    // 6. Calls processApplication method on the service
    // 7. Asserts that result status is VERIFICATION_PENDING
    // 8. Asserts that error message contains the system unavailability message
    // 9. Verifies that employmentVerificationService.verifyEmployment was called once with email
    // 10. Verifies that loanRepository.save was never called
}
```

#### GREEN Phase - Make Test Pass

The current implementation should already handle this case correctly if the `verifyEmployment` method checks `isSuccessful()` first.

#### REFACTOR Phase

Consider refactoring the service to have cleaner flow and method organization:

```java
public LoanProcessingResult processApplication(LoanApplication application) {
    // TODO: Final implementation should follow this flow:
    // 1. Perform basic validation (call separate method)
    // 2. If basic validation fails, return REJECTED
    // 3. Validate documents (call separate method)
    // 4. If document validation fails, return appropriate result
    // 5. Validate credit score (call separate method)
    // 6. If credit score validation fails, return REJECTED
    // 7. Verify employment (call separate method)
    // 8. If employment verification fails, return appropriate result
    // 9. If all validations pass, save application and return ACCEPTED
}

private LoanProcessingResult performBasicValidation(LoanApplication application) {
    // TODO: Basic validation method should:
    // 1. Call validateApplication() to get list of errors
    // 2. If errors exist, return REJECTED with joined error messages
    // 3. Return null if no basic validation issues
}

private LoanProcessingResult verifyEmployment(LoanApplication application) {
    // TODO: Employment verification method should:
    // 1. Check if employment verification service is available
    // 2. Call external employment verification service with email
    // 3. Handle service failures (return VERIFICATION_PENDING)
    // 4. Check employment status (return REJECTED if not employed)
    // 5. Return null if employment is confirmed
}

private LoanProcessingResult validateCreditScore(LoanApplication application) {
    // TODO: Credit score validation method (same as Assignment 3)
}

private LoanProcessingResult validateDocuments(LoanApplication application) {
    // TODO: Document validation method (same as Assignment 2)
}

private List<String> validateApplication(LoanApplication application) {
    // TODO: Basic field validation method (same as Assignment 1)
}

private boolean isNullOrEmpty(String value) {
    // TODO: Helper method for string validation
}
```

## Challenge Instructions

1. **Start with RED**: Write each failing test first
2. **Move to GREEN**: Write minimal production code to make the test pass
3. **Apply REFACTOR**: Improve code quality without breaking tests
4. **Maintain backward compatibility**: Ensure Assignments 1, 2 & 3 functionality still works
5. **Repeat the cycle** for each test function

## Success Criteria

Your implementation should:

- ✅ Pass all three new test functions
- ✅ Maintain backward compatibility with Assignments 1, 2 & 3 functionality
- ✅ Handle employment verification through external HR verification service
- ✅ Distinguish between system unavailable (VERIFICATION_PENDING) and employment not found (REJECTED)
- ✅ Properly integrate with mocked EmploymentVerificationService
- ✅ Follow clean code principles with proper separation of concerns
- ✅ Provide comprehensive end-to-end loan processing workflow

## Key TDD Learning Objectives

By completing this challenge, you will practice:

- Building a complete production-ready service through iterative TDD cycles
- Managing multiple external service dependencies in a single system
- Implementing complex business workflows with proper status management
- Handling external service failures gracefully across multiple integration points
- Maintaining backward compatibility while adding significant new features
- Refactoring complex validation logic while preserving all functionality

## Testing Scenarios Covered

1. **Confirmed Employment**: Valid fields, valid documents, good credit score, active employment → ACCEPTED
2. **Unverified Employment**: Valid fields, valid documents, good credit score, inactive employment → REJECTED
3. **HR System Unavailable**: Valid fields, valid documents, good credit score, HR system down → VERIFICATION_PENDING
4. **Complete Pipeline**: Maintains all existing validation from Assignments 1, 2 & 3

## Processing Flow

The final enhanced service provides comprehensive loan application processing:

1. **Basic Validation**: Check required application fields
2. **Document Validation**: Verify all documents are present and valid  
3. **Credit Score Check**: Verify applicant meets minimum credit requirements (650+)
4. **Employment Verification**: Confirm active employment status through HR systems
5. **Final Approval**: Save approved applications to repository

## Status Management

- **ACCEPTED**: All validations passed, loan approved
- **REJECTED**: Failed validation at any step
- **AWAITING_DOCUMENTS**: Missing required documents
- **VERIFICATION_PENDING**: External system unavailable

Good luck with your TDD implementation! By completing this assignment, you'll have built a comprehensive, production-ready loan processing service using proper TDD methodology.